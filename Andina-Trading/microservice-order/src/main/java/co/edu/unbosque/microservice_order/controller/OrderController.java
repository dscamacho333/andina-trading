package co.edu.unbosque.microservice_order.controller;


import co.edu.unbosque.microservice_order.exception.CustomAlpacaException;
import co.edu.unbosque.microservice_order.model.dto.OrderDTO;
import co.edu.unbosque.microservice_order.model.dto.OrderRequestDTO;
import co.edu.unbosque.microservice_order.model.dto.OrderSummaryDTO;
import co.edu.unbosque.microservice_order.model.entity.Order;
import co.edu.unbosque.microservice_order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/buy")
    public ResponseEntity<Order> createBuyOrder(@RequestBody OrderRequestDTO requestDTO) {
        Order newOrder = orderService.createBuyOrder(requestDTO);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @GetMapping("/summary/{orderId}")
    public ResponseEntity<OrderSummaryDTO> getOrderSummaryById(
            @RequestParam String accountId,
            @PathVariable String orderId
    ) {
        OrderSummaryDTO dto = orderService.getOrderSummaryById(accountId, orderId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrders(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @GetMapping("filtered")
    public ResponseEntity<List<OrderDTO>> getFilteredOrders(
            @RequestParam Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime sentToAlpacaAt
    ){

        List<OrderDTO> filteredOrders = orderService.getTodayOrders(userId);
        return ResponseEntity.ok(filteredOrders);
    }

    // Manejo de errores específicos de Alpaca
    @PostMapping("/sell")
    public ResponseEntity<Order> createSellOrder(@RequestBody OrderRequestDTO requestDTO) {
        Order newOrder = orderService.createSellOrder(requestDTO);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @GetMapping("/update-pending/{userId}")
    public ResponseEntity<String> updatePendingOrders(@PathVariable Integer userId) {
        try {
            orderService.updatePendingOrders(userId);
            return ResponseEntity.ok("Recargas pendientes revisadas y actualizadas correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar órdenes pendientes: " + e.getMessage());
        }
    }

    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelarOrden(@PathVariable Integer orderId) {
        try {
            orderService.cancelOrder(orderId);
            return ResponseEntity.ok("Orden cancelada exitosamente.");
        } catch (CustomAlpacaException e) {
            return handleAlpacaException(e);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al cancelar la orden.");
        }
    }


    @ExceptionHandler(CustomAlpacaException.class)
    public ResponseEntity<String> handleAlpacaException(CustomAlpacaException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body("{Error_Alpaca: " + ex.getAlpacaMessage()+"}");
    }

}
