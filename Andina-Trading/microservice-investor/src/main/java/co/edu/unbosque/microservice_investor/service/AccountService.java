package co.edu.unbosque.microservice_investor.service;


import co.edu.unbosque.microservice_investor.client.TransactionClient;
import co.edu.unbosque.microservice_investor.exception.CustomAlpacaException;
import co.edu.unbosque.microservice_investor.model.dto.*;
import co.edu.unbosque.microservice_investor.model.enums.AccountStatus;
import co.edu.unbosque.microservice_investor.model.enums.Role;
import co.edu.unbosque.microservice_investor.model.enums.TransactionStatus;
import co.edu.unbosque.microservice_investor.model.enums.TransactionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class AccountService {

    @Value("${alpaca.broker.api-key-id}")
    private String alpacaApiKey;

    @Value("${alpaca.broker.api-secret}")
    private String alpacaApiSecret;

    @Value("${alpaca.broker.base-url}")
    private String alpacaApiUrl;

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserService userService;
    private final TransactionClient transactionService;
    private final RestTemplate restTemplate;

    public AccountService(@Qualifier("brokerRestTemplate") RestTemplate restTemplate, UserService userService, TransactionClient transactionService) {
        this.restTemplate = restTemplate;
        this.userService = userService;
        this.transactionService = transactionService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public AccountDTO createAccount(AccountRequestDTO accountRequestDTO) {
        if (userService.emailExists(accountRequestDTO.getContact().getEmail_address())) {
            throw new CustomAlpacaException(400, "El correo ya está registrado");
        }

        HttpHeaders headers = buildAlpacaHeaders();
        Map<String, Object> body = buildAccountRequestBody(accountRequestDTO);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            Map<String, Object> responseBody = sendAccountToAlpaca(entity);
            String accountId = (String) responseBody.get("id");
            String status = (String) responseBody.get("status");

            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(accountRequestDTO.getContact().getEmail_address());
            userDTO.setName(accountRequestDTO.getIdentity().getGiven_name() + " " + accountRequestDTO.getIdentity().getFamily_name());
            userDTO.setPhone(accountRequestDTO.getContact().getPhone_number());
            userDTO.setPasswordHash(passwordEncoder.encode(accountRequestDTO.getPassword()));
            userDTO.setAlpacaAccountId(accountId);
            userDTO.setAccountStatus(AccountStatus.ACTIVE);
            userDTO.setAlpacaStatus("SUBMITTED");
            userDTO.setRole(Role.INVESTOR);
            userDTO.setHasSubscription(false);
            userDTO.setCreatedAt(LocalDateTime.now());
            userDTO.setLastAccess(LocalDateTime.now());
            userDTO.setBalance("0");

            userService.createUser(userDTO);

            return new AccountDTO(accountId, AccountStatus.PENDING.toString(), status, true);

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new CustomAlpacaException(ex.getRawStatusCode(), ex.getResponseBodyAsString());
        } catch (RestClientException ex) {
            throw new CustomAlpacaException(500, "Error al conectar con Alpaca: " + ex.getMessage());
        }
    }

    public AccountResponseDTO getAccountInfo(String accountId) {
        HttpHeaders headers = buildAlpacaHeaders();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Map<String, Object> responseBody = getAlpacaAccountInfo(accountId, headers);
            if (responseBody == null) {
                throw new CustomAlpacaException(500, "No se pudo obtener la información de la cuenta.");
            }



            return objectMapper.convertValue(responseBody, AccountResponseDTO.class);

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

    private Map<String, Object> sendAccountToAlpaca(HttpEntity<Map<String, Object>> entity) {
        ResponseEntity<Map> response = restTemplate.postForEntity(alpacaApiUrl + "/v1/accounts", entity, Map.class);
        return response.getBody();
    }


    public BankRelationStatus checkBankRelationshipStatus(Integer userId) {
        UserDTO user = userService.getUserById(userId);

        String accountId = user.getAlpacaAccountId(); // debe existir en la entidad User
        String url = alpacaApiUrl + "/v1/accounts/" + accountId + "/ach_relationships";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(alpacaApiKey, alpacaApiSecret);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<BankRelationResponseDTO[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, BankRelationResponseDTO[].class);

        BankRelationResponseDTO[] achList = response.getBody();

        if (achList == null || achList.length == 0) {
            return new BankRelationStatus(null, null);
        }

        BankRelationResponseDTO ach = achList[0];
        String status = ach.getStatus();

        if ("APPROVED".equalsIgnoreCase(status)) {
            String relationshipId = ach.getId();

            if (user.getBankRelationshipId() == null || !user.getBankRelationshipId().equals(relationshipId)) {
                // Si el usuario no tenia aun el id del banco en la db, actualizarlo
                user.setBankRelationshipId(relationshipId);
                userService.updateUser(user);
            }

            return new BankRelationStatus(true, null);
        } else {
            return new BankRelationStatus(false, "El estado de la cuenta es: " + status);
        }
    }

    public BankRelationStatus createAchRelationship(BankRelationCreatedDTO request) {
        String url = alpacaApiUrl + "/v1/accounts/" + request.getAccountId() + "/ach_relationships";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(alpacaApiKey, alpacaApiSecret);

        Map<String, String> payload = new HashMap<>();
        payload.put("account_owner_name", request.getAccountOwnerName());
        payload.put("bank_account_type", request.getBankAccountType());
        payload.put("bank_account_number", request.getBankAccountNumber());
        payload.put("bank_routing_number", request.getBankRoutingNumber());
        payload.put("nickname", "Cuenta Bancaria creada por ForestTrade");

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<BankRelationResponseDTO> response = restTemplate.exchange(url, HttpMethod.POST, entity, BankRelationResponseDTO.class);

            BankRelationResponseDTO created = response.getBody();

            if (created == null || created.getId() == null) {
                throw new CustomAlpacaException(500, "Respuesta de Alpaca vacía o inválida.");
            }
            // Guardar en base de datos
            UserDTO user = userService.getUserById(Integer.valueOf(request.getUserId()));
            user.setBankRelationshipId(created.getId());
            userService.updateUser(user);

            return new BankRelationStatus(true, "ACH relationship created successfully");

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // Captura el mensaje real que vino desde Alpaca
            String errorMessage = ex.getResponseBodyAsString();
            throw new CustomAlpacaException(ex.getRawStatusCode(), "Error de Alpaca: " + errorMessage);
        } catch (RestClientException ex) {
            throw new CustomAlpacaException(500, "Error al conectar con Alpaca: " + ex.getMessage());
        }

    }

    private Map<String, Object> getAlpacaAccountInfo(String accountId, HttpHeaders headers) {

        return restTemplate.exchange(alpacaApiUrl + "/v1/accounts/" + accountId, HttpMethod.GET, new HttpEntity<>(headers), Map.class).getBody();
    }

    private Map<String, Object> buildAccountRequestBody(AccountRequestDTO dto) {
        Map<String, Object> body = new HashMap<>();
        body.put("contact", dto.getContact());
        body.put("identity", dto.getIdentity());
        body.put("disclosures", dto.getDisclosures());
        body.put("agreements", dto.getAgreements());

        return body;
    }

    public List<RechargeDTO> getRechargeDTOsByUser(Integer userId) {
        return transactionService.getRechargeDTOsByUser(userId);
    }


    public void createRecharge(Map<String, Object> request) {
        float amount = ((Number) request.get("amount")).floatValue();
        Object userIdRaw = request.get("userId");
        Integer userId;

        if (userIdRaw instanceof String) {
            userId = Integer.parseInt((String) userIdRaw);
        } else if (userIdRaw instanceof Number) {
            userId = ((Number) userIdRaw).intValue();
        } else {
            throw new IllegalArgumentException("userId inválido");
        }

        UserDTO user = userService.getUserById(userId);

        if (user.getBankRelationshipId() == null) {
            throw new CustomAlpacaException(400, "El usuario no tiene una relación bancaria activa");
        }
        String url = alpacaApiUrl + "/v1/accounts/" + user.getAlpacaAccountId() + "/transfers";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(alpacaApiKey, alpacaApiSecret);

        Map<String, Object> payload = new HashMap<>();
        payload.put("transfer_type", "ach");
        payload.put("relationship_id", user.getBankRelationshipId());
        payload.put("amount", amount);
        payload.put("direction", "INCOMING");
        payload.put("timing", "immediate");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

            Map<String, Object> responseBody = response.getBody();
            String alpacaTransferId = (String) responseBody.get("id");

            // Crear transacción en la DB
            TransactionDTO transaction = new TransactionDTO(null, user.getId(), null, amount, TransactionType.RECHARGE, "Recarga realizada por ForestTrade", LocalDateTime.now(), TransactionStatus.PENDING, alpacaTransferId);

            transactionService.createTransaction(transaction);

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new CustomAlpacaException(ex.getRawStatusCode(), ex.getResponseBodyAsString());
        } catch (RestClientException ex) {
            throw new CustomAlpacaException(500, "Error al conectar con Alpaca: " + ex.getMessage());
        }
    }

    public void checkPendingRecharges(Integer userId) {
        UserDTO user = userService.getUserById(userId);
        List<TransactionDTO> pendingRecharges = transactionService.getPendingRechargesByUser(userId);

        if (pendingRecharges.isEmpty()) return;

        String accountId = user.getAlpacaAccountId();
        HttpHeaders headers = buildAlpacaHeaders();
        String url = alpacaApiUrl + "/v1/accounts/" + accountId + "/transfers";

        try {
            ResponseEntity<Map[]> response = restTemplate.exchange(
                    url, HttpMethod.GET, new HttpEntity<>(headers), Map[].class
            );

            Map[] transfers = response.getBody();
            if (transfers == null || transfers.length == 0) return;

            // Indexamos transferencias por ID para búsquedas rápidas
            Map<String, Map> transferMap = Arrays.stream(transfers)
                    .collect(Collectors.toMap(
                            t -> (String) t.get("id"),
                            t -> t,
                            (existing, replacement) -> existing // evitar duplicados
                    ));

            for (TransactionDTO transaction : pendingRecharges) {
                String alpacaId = transaction.getAlpacaId();
                Map transferData = transferMap.get(alpacaId);

                if (transferData == null) continue;

                String status = ((String) transferData.get("status")).toUpperCase();

                if ("COMPLETE".equalsIgnoreCase(status)) {
                    transaction.setStatus(TransactionStatus.CONFIRMED);
                    transactionService.updateTransaction(transaction);
                    userService.addToBalance(user.getId(), transaction.getAmount());

                } else if (List.of("FAILED", "CANCELED", "REJECTED", "RETURNED").contains(status)) {
                    transaction.setStatus(TransactionStatus.CANCELED);
                    transactionService.updateTransaction(transaction);
                }
            }

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new CustomAlpacaException(ex.getRawStatusCode(), ex.getResponseBodyAsString());
        } catch (RestClientException ex) {
            throw new CustomAlpacaException(500, "Error al conectar con Alpaca: " + ex.getMessage());
        }
    }



}