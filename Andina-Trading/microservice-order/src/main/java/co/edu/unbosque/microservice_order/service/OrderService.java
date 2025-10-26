package co.edu.unbosque.microservice_order.service;


import co.edu.unbosque.microservice_order.client.InvestorClient;
import co.edu.unbosque.microservice_order.client.MailClient;
import co.edu.unbosque.microservice_order.exception.CustomAlpacaException;
import co.edu.unbosque.microservice_order.model.dto.*;
import co.edu.unbosque.microservice_order.model.entity.Order;
import co.edu.unbosque.microservice_order.model.enums.*;
import co.edu.unbosque.microservice_order.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final ModelMapper modelMapper;
    @Value("${alpaca.broker.api-key-id}")
    private String alpacaApiKey;

    @Value("${alpaca.broker.api-secret}")
    private String alpacaApiSecret;

    @Value("${alpaca.broker.base-url}")
    private String alpacaApiUrl;

    @Value("${platform.commission.percentage}")
    private float commissionPercentage;

    private final RestTemplate restTemplate;
    private final OrderRepository orderRepository;

    private final InvestorClient investorClient;   // <— nuevo
    private final TransactionService transactionService;
    private final MailClient mailClient;


    public OrderService(
            @Qualifier("brokerRestTemplate") RestTemplate restTemplate,
            OrderRepository orderRepository,            // <— nuevo
            TransactionService transactionService,
            ModelMapper modelMapper,
            MailClient mailClient,                     // <— nuevo
            InvestorClient investorClient) {
        this.restTemplate = restTemplate;
        this.orderRepository = orderRepository;
        this.investorClient = investorClient;
        this.transactionService = transactionService;
        this.modelMapper = modelMapper;
        this.mailClient = mailClient;
    }


    public Order createBuyOrder(OrderRequestDTO request) {

        StockDTO stock = request.getStock();
        UserSessionDTO user = request.getUser();

        float pricePerStock = stock.getCurrentPrice();
        int quantity = request.getQuantity();
        float total = pricePerStock * quantity;
        float platformCommission = Math.round(total * commissionPercentage * 100.0f) / 100.0f;
        float totalWithCommission  = total + platformCommission;
        float stockbrokerCommission = 0f;
        OrderStatus status = OrderStatus.SENDED;
        Integer stockbrokerId = null;
        Role initiatedBy = Role.INVESTOR;
        TransactionStatus transactionStatus = TransactionStatus.PENDING;


        // Validar balance disponible
        investorClient.validateBalance(user.getUserId(), totalWithCommission);

        // Validar si requiere firma (Lo envio un usuario y debe aprobar comisionista)
        if (Boolean.TRUE.equals(request.isRequiresSignature())) {
            // ToDo: definir aqui stockbroker comision
            return savePendingApprovalOrder(request, OrderStatus.PENDING_BROKER, initiatedBy, null, total, totalWithCommission, platformCommission,stockbrokerCommission);

        } //valida si es enviada por comisionista
        else if(request.getBrokerId() != null){
            //ToDo: definir aqui comision
            return savePendingApprovalOrder(request, OrderStatus.PENDING_INVESTOR, Role.STOCKBROKER, request.getBrokerId(), total, totalWithCommission, platformCommission,stockbrokerCommission);
        }
        // Si no requiere ninguna aprobacion
        else {

            Map<String, Object> bodyOrder = buildAlpacaOrderBody(request, "buy");
            HttpHeaders headers = buildAlpacaHeaders();

            try {

                // Enviar orden a Alpaca

                Map<String, Object> responseBodyOrder = sendOrderToAlpaca(user.getAlpacaAccountId(), new HttpEntity<>(bodyOrder, headers));
                String alpacaOrderId = (String) responseBodyOrder.get("id");

                //Obtener ultimo estado de la orden
                Map<String, Object> updatedOrder = getAlpacaOrderStatus(user.getAlpacaAccountId(), alpacaOrderId, headers);
                String updatedStatus = (String) updatedOrder.get("status");

                // Si se ejecuto al instante, se generan transacciones y descuento de saldo

                if ("filled".equalsIgnoreCase(updatedStatus)) {
                    float filledPricePerStock = Optional.ofNullable(updatedOrder.get("filled_avg_price"))
                            .map(Object::toString)
                            .map(Float::parseFloat)
                            .orElse(pricePerStock);

                    total = filledPricePerStock * quantity;
                    platformCommission = Math.round(total * commissionPercentage * 100.0f) / 100.0f;
                    totalWithCommission = total + platformCommission;
                    transactionStatus = TransactionStatus.CONFIRMED;

                }

                investorClient.subtractFromBalance(user.getUserId(), totalWithCommission);

                Order orderSaved = buildAndSaveOrder(
                        true,
                        LocalDateTime.now(),
                        status,
                        request,
                        alpacaOrderId,
                        updatedStatus.toUpperCase(),
                        total,
                        totalWithCommission ,
                        platformCommission,
                        0f,
                        initiatedBy,
                        stockbrokerId
                );

                handleTransactions(orderSaved, total, platformCommission, transactionStatus, stock.getSymbol(), quantity);


                // Guardar orden no ejecutada al instante
                return orderSaved;

            } catch (HttpClientErrorException | HttpServerErrorException ex) {
                String responseMessage = ex.getResponseBodyAsString();
                throw new CustomAlpacaException(ex.getRawStatusCode(), responseMessage);
            } catch (RestClientException ex) {
                throw new CustomAlpacaException(500, "Error al conectar con Alpaca: " + ex.getMessage());
            }
        }


    }



    private Map<String, Object> buildAlpacaOrderBody(OrderRequestDTO request, String side) {
        Map<String, Object> bodyOrder = new HashMap<>();
        bodyOrder.put("symbol", request.getStock().getSymbol());
        bodyOrder.put("qty", request.getQuantity());
        bodyOrder.put("side", side);
        bodyOrder.put("type", request.getOrderType().name().toLowerCase());
        bodyOrder.put("time_in_force", request.getTimeInForce().name().toLowerCase());

        if (request.getOrderType() == OrderType.LIMIT || request.getOrderType() == OrderType.STOP_LIMIT) {
            bodyOrder.put("limit_price", request.getLimitPrice());
        }

        if (request.getOrderType() == OrderType.STOP || request.getOrderType() == OrderType.STOP_LIMIT) {
            if (request.getStopPrice() <= 0) {
                throw new CustomAlpacaException(400, "stop_price must be greater than 0");
            }
            bodyOrder.put("stop_price", request.getStopPrice());
        }

        return bodyOrder;
    }

    public Order createSellOrder(OrderRequestDTO request) {
        StockDTO stock = request.getStock();
        UserSessionDTO user = request.getUser();

        float pricePerStock = stock.getCurrentPrice();
        int quantity = request.getQuantity();
        float total = pricePerStock * quantity;
        float platformCommission = Math.round(total * commissionPercentage * 100.0f) / 100.0f;
        float totalAfterCommission = total - platformCommission;

        OrderStatus status = OrderStatus.SENDED;
        TransactionStatus transactionStatus = TransactionStatus.PENDING;
        Integer stockbrokerId = null;
        Role initiatedBy = Role.INVESTOR;

        // Si requiere aprobaciones, no se envía aún a Alpaca
        if (Boolean.TRUE.equals(request.isRequiresSignature())) {
            return savePendingApprovalOrder(request, OrderStatus.PENDING_BROKER, initiatedBy, null, total, total, 0f, 0f);
        } else if (request.getBrokerId() != null) {
            return savePendingApprovalOrder(request, OrderStatus.PENDING_INVESTOR, Role.STOCKBROKER, request.getBrokerId(), total, total, 0f, 0f);
        }

        // Construir body y headers para Alpaca
        Map<String, Object> bodyOrder = buildAlpacaOrderBody(request, "sell");
        HttpHeaders headers = buildAlpacaHeaders();

        try {
            Map<String, Object> responseBodyOrder = sendOrderToAlpaca(user.getAlpacaAccountId(), new HttpEntity<>(bodyOrder, headers));
            String alpacaOrderId = (String) responseBodyOrder.get("id");

            Map<String, Object> updatedOrder = getAlpacaOrderStatus(user.getAlpacaAccountId(), alpacaOrderId, headers);
            String updatedStatus = (String) updatedOrder.get("status");

            float filledPricePerStock = pricePerStock;
            if ("filled".equalsIgnoreCase(updatedStatus)) {
                transactionStatus = TransactionStatus.CONFIRMED;
                filledPricePerStock = Optional.ofNullable(updatedOrder.get("filled_avg_price"))
                        .map(Object::toString)
                        .map(Float::parseFloat)
                        .orElse(pricePerStock);

                total = filledPricePerStock * quantity;
                platformCommission = Math.round(total * commissionPercentage * 100.0f) / 100.0f;
                totalAfterCommission = total - platformCommission;

                // Sumar al balance el valor neto de la venta
                investorClient.addToBalance(user.getUserId(), totalAfterCommission);
            }

            Order orderSaved = new Order(
                    OrderSide.SELL,
                    null,
                    user.getUserId(),
                    stock.getSymbol(),
                    quantity,
                    request.getOrderType(),
                    alpacaOrderId,
                    status,
                    request.getLimitPrice(),
                    request.isRequiresSignature(),
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    true,
                    total,
                    request.getTimeInForce(),
                    request.getStopPrice(),
                    platformCommission,
                    0f,
                    totalAfterCommission,
                    updatedStatus.toUpperCase(),
                    initiatedBy,
                    stockbrokerId
            );

            orderSaved = orderRepository.save(orderSaved);

            // Transacción por la venta (positiva)
            transactionService.createTransaction(new TransactionDTO(
                    null,
                    user.getUserId(),
                    orderSaved.getOrderId(),
                    total,
                    TransactionType.SELL,
                    "Venta de " + quantity + " acciones de " + stock.getSymbol(),
                    LocalDateTime.now(),
                    transactionStatus,
                    null
            ));

            transactionService.createTransaction(new TransactionDTO(
                    null,
                    user.getUserId(),
                    orderSaved.getOrderId(),
                    -platformCommission,
                    TransactionType.COMMISSION_FT,
                    "Comisión de la plataforma por venta de acciones",
                    LocalDateTime.now(),
                    transactionStatus,
                    null
            ));


            return orderSaved;

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new CustomAlpacaException(ex.getRawStatusCode(), ex.getResponseBodyAsString());
        } catch (RestClientException ex) {
            throw new CustomAlpacaException(500, "Error al conectar con Alpaca: " + ex.getMessage());
        }
    }


    private HttpHeaders buildAlpacaHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(alpacaApiKey, alpacaApiSecret);
        return headers;
    }


    private Map<String, Object> sendOrderToAlpaca(String accountId, HttpEntity<Map<String, Object>> entity) {
        ResponseEntity<Map> response = restTemplate.postForEntity(
                alpacaApiUrl + "/v1/trading/accounts/" + accountId + "/orders",
                entity,
                Map.class
        );
        return response.getBody();
    }

    private Map<String, Object> getAlpacaOrderStatus(String accountId, String orderId, HttpHeaders headers) {
        return restTemplate.exchange(
                alpacaApiUrl + "/v1/trading/accounts/" + accountId + "/orders/" + orderId,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Map.class
        ).getBody();
    }


    private Order buildAndSaveOrder(boolean sendToAlpaca, LocalDateTime sendToAlpacaAt, OrderStatus orderStatus, OrderRequestDTO request, String alpacaOrderId, String alpacaStatus, float filledPrice, float finalAmountPaid, float platformCommission, Float brokerCommission, Role initiatedBy, Integer stockbrokerId) {
        StockDTO stock = request.getStock();
        int quantity = request.getQuantity();

        Order order = new Order(OrderSide.BUY,null, request.getUser().getUserId(), stock.getSymbol(), quantity, request.getOrderType(), alpacaOrderId, orderStatus, request.getLimitPrice(), request.isRequiresSignature(), null, LocalDateTime.now(),  sendToAlpacaAt, sendToAlpaca, filledPrice, request.getTimeInForce(), request.getStopPrice(), platformCommission, brokerCommission, finalAmountPaid, alpacaStatus, initiatedBy, stockbrokerId );
        return orderRepository.save(order);
    }

    private void handleTransactions(Order order, float total, float platformCommission, TransactionStatus status, String symbol, int quantity) {

        transactionService.createTransaction(new TransactionDTO(
                null, order.getUserId(), order.getOrderId(), -total,
                TransactionType.BUY, "Compra de " + quantity + " acciones de " + symbol,
                LocalDateTime.now(), status, null
        ));

        transactionService.createTransaction(new TransactionDTO(
                null, order.getUserId(), order.getOrderId(), -platformCommission,
                TransactionType.COMMISSION_FT, "Comisión de la plataforma por compra de acciones",
                LocalDateTime.now(), status, null
        ));
    }

    private Order savePendingApprovalOrder(OrderRequestDTO request, OrderStatus status, Role initiatedBy, Integer brokerId,
                                           float total, float totalWithCommission, float platformCommission, float brokerCommission) {
        return buildAndSaveOrder(false, null, status, request, null, null, total, totalWithCommission,
                platformCommission, brokerCommission, initiatedBy, brokerId);
    }

    public List<OrderDTO> getOrdersByUserId(Long userId) {

        return orderRepository.findByUserId(userId)
                .stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .toList();
    }
    public List<OrderDTO> getTodayOrders(Long userId) {
        List<Order> userOrders = orderRepository.findByUserId(userId);

        LocalDate today = LocalDate.now(); // Fecha actual
        return userOrders.stream()
                .filter(order -> order.getSentToAlpacaAt().toLocalDate().equals(today))
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    public OrderSummaryDTO getOrderSummaryById(String accountId, String orderId) {
        HttpHeaders headers = buildAlpacaHeaders();
        try {
            String url = alpacaApiUrl + "/v1/trading/accounts/" + accountId + "/orders/" + orderId;
            System.out.println("Llamando a Alpaca para obtener orden específica: " + url);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            Map<String, Object> order = response.getBody();

            if (order == null) {
                throw new CustomAlpacaException(404, "Orden no encontrada.");
            }

            return new OrderSummaryDTO(
                    (String) order.get("id"),
                    (String) order.get("symbol"),
                    Integer.parseInt(order.get("qty").toString()),
                    order.get("filled_avg_price") != null ? Float.parseFloat(order.get("filled_avg_price").toString()) : 0f,
                    (String) order.get("status"),
                    (String) order.get("type"),
                    LocalDateTime.parse((String) order.get("created_at")),
                    order.get("filled_at") != null ? LocalDateTime.parse((String) order.get("filled_at")) : null
            );

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            System.err.println("Error HTTP al llamar Alpaca: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString());
            throw new CustomAlpacaException(ex.getRawStatusCode(), ex.getResponseBodyAsString());
        } catch (RestClientException ex) {
            System.err.println("Error de conexión general: " + ex.getMessage());
            throw new CustomAlpacaException(500, "Error al conectar con Alpaca: " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("Error inesperado: " + ex.getMessage());
            ex.printStackTrace();
            throw new CustomAlpacaException(500, "Error inesperado al procesar la orden.");
        }
    }

    public void updatePendingOrders(Integer userId) {
        List<Order> pendingOrders = orderRepository.findByUserIdAndAlpacaStatusIn(
                userId, List.of("NEW", "SENT", "ACCEPTED", "PENDING", "PARTIALLY_FILLED")
        );
        System.out.println(pendingOrders);

        HttpHeaders headers = buildAlpacaHeaders();

        for (Order order : pendingOrders) {
            try {
                Map<String, Object> alpacaOrder = getAlpacaOrderStatus(
                        investorClient.getUserById(order.getUserId()).getAlpacaAccountId(),
                        order.getAlpacaOrderId(),
                        headers
                );
                System.out.println("ORDEN DE ALPACA");
                System.out.println(alpacaOrder);

                String newStatus = ((String) alpacaOrder.get("status")).toUpperCase();

                if (newStatus.equals(order.getAlpacaStatus())) continue;

                order.setAlpacaStatus(newStatus);

                float filledPricePerStock = Optional.ofNullable(alpacaOrder.get("filled_avg_price"))
                        .map(Object::toString)
                        .map(Float::parseFloat)
                        .orElse(order.getFilledPrice() / order.getQuantity());

                float newTotal = filledPricePerStock * order.getQuantity();
                float newCommission = Math.round(newTotal * commissionPercentage * 100.0f) / 100.0f;

                if ("FILLED".equals(newStatus)) {

                    order.setAlpacaStatus(newStatus);
                    order.setFilledPrice(newTotal);
                    String emailBody = String.format(
                            "Hola %s,\n\nTu orden de %s %d acciones de %s fue completada exitosamente a un precio total de $%.2f.\n\nSaludos,\nForestTrade",
                            investorClient.getUserById(order.getUserId()).getName(), order.getOrderSide().name().toLowerCase(), order.getQuantity(), order.getSymbol(), order.getFilledPrice()
                    );
                    MailRequestDTO mail = new MailRequestDTO(investorClient.getUserById(order.getUserId()).getEmail(),"✅ Orden completada", emailBody);
                    mailClient.send(mail);

                    if (order.getOrderSide() == OrderSide.BUY) {
                        float newTotalWithCommission = newTotal + newCommission;
                        float oldTotalWithCommission = order.getTotalAmountPaid();
                        float diff = oldTotalWithCommission - newTotalWithCommission;

                        order.setTotalAmountPaid(newTotalWithCommission);
                        order.setPlatformCommission(newCommission);

                        transactionService.updateTransactionAmountsAndStatus(order.getOrderId(), -newTotal, TransactionType.BUY, TransactionStatus.CONFIRMED);
                        transactionService.updateTransactionAmountsAndStatus(order.getOrderId(), -newCommission, TransactionType.COMMISSION_FT, TransactionStatus.CONFIRMED);

                        investorClient.addToBalance(order.getUserId(), diff);

                    } else {
                        float newNetGain = newTotal - newCommission;
                        order.setFilledPrice(newTotal);
                        order.setTotalAmountPaid(newNetGain);
                        order.setPlatformCommission(newCommission);

                        transactionService.updateTransactionAmountsAndStatus(order.getOrderId(), newTotal, TransactionType.SELL, TransactionStatus.CONFIRMED);
                        transactionService.updateTransactionAmountsAndStatus(order.getOrderId(), -newCommission, TransactionType.COMMISSION_FT, TransactionStatus.CONFIRMED);

                        investorClient.addToBalance(order.getUserId(), newNetGain);
                    }

                } else if (List.of("CANCELED", "REJECTED", "EXPIRED").contains(newStatus)) {
                    order.setAlpacaStatus(newStatus);
                    transactionService.cancelTransactionsByOrder(order.getOrderId());

                    UserDTO user = investorClient.getUserById(order.getUserId());
                    String reason = switch (newStatus) {
                        case "CANCELED" -> "cancelada";
                        case "REJECTED" -> "rechazada";
                        case "EXPIRED" -> "expirada";
                        default -> "finalizada";
                    };

                    String emailBody = String.format(
                            "Hola %s,\n\nTu orden de %s %d acciones de %s fue %s por Alpaca.\n\nSi no reconoces esta acción, por favor contacta con soporte.\n\nForestTrade",
                            user.getName(), order.getOrderSide().name().toLowerCase(), order.getQuantity(), order.getSymbol(), reason
                    );

                    MailRequestDTO mailRequestDTO = new MailRequestDTO(user.getEmail(), "❌ Orden " + reason, emailBody);
                    mailClient.send(mailRequestDTO);


                    if (order.getOrderSide() == OrderSide.BUY) {
                        investorClient.addToBalance(order.getUserId(), order.getTotalAmountPaid());
                    }
                }

                orderRepository.save(order);

            } catch (Exception e) {
                System.out.println(" Error actualizando orden pendiente: " + order.getAlpacaOrderId() + ": " + e.getMessage());
            }
        }
    }

    public void cancelOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        if (!order.isSentToAlpaca() || order.getAlpacaOrderId() == null) {
            throw new RuntimeException("La orden no fue enviada a Alpaca o no tiene ID externo.");
        }

        if (!List.of("NEW", "SENT", "ACCEPTED", "PENDING", "PARTIALLY_FILLED").contains(order.getAlpacaStatus())) {
            throw new RuntimeException("La orden no está en un estado cancelable.");
        }

        UserDTO user = investorClient.getUserById(order.getUserId());

        HttpHeaders headers = buildAlpacaHeaders();
        String url = alpacaApiUrl + "/v1/trading/accounts/" + user.getAlpacaAccountId()
                + "/orders/" + order.getAlpacaOrderId();

        try {
            restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);

            order.setAlpacaStatus("CANCELED");
            orderRepository.save(order);

            transactionService.cancelTransactionsByOrder(orderId);

            if (order.getOrderSide() == OrderSide.BUY) {
                investorClient.addToBalance(order.getUserId(), order.getTotalAmountPaid());
            }

            String reason = "cancelada";


            String emailBody = String.format(
                    "Hola %s,\n\nTu orden de %s %d acciones de %s fue %s por Alpaca.\n\nSi no reconoces esta acción, por favor contacta con soporte.\n\nForestTrade",
                    user.getName(), order.getOrderSide().name().toLowerCase(), order.getQuantity(), order.getSymbol(), reason
            );

            MailRequestDTO mailRequestDTO = new MailRequestDTO(user.getEmail(), "❌ Orden " + reason, emailBody);

            mailClient.send(mailRequestDTO);
            System.out.println("📬 Enviando correo a: " + user.getEmail());



        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new CustomAlpacaException(ex.getRawStatusCode(), ex.getResponseBodyAsString());
        } catch (Exception e) {
            System.out.println("❌ Error al cancelar la orden o enviar correo: " + e.getMessage());
        }
    }

}