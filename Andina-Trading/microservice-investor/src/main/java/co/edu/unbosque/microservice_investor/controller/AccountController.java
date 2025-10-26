package co.edu.unbosque.microservice_investor.controller;

import co.edu.unbosque.microservice_investor.exception.CustomAlpacaException;
import co.edu.unbosque.microservice_investor.model.dto.AccountRequestDTO;
import co.edu.unbosque.microservice_investor.model.dto.BankRelationCreatedDTO;
import co.edu.unbosque.microservice_investor.model.dto.BankRelationStatus;
import co.edu.unbosque.microservice_investor.model.dto.RechargeDTO;
import co.edu.unbosque.microservice_investor.security.JwtUtil;
import co.edu.unbosque.microservice_investor.service.AccountService;
import co.edu.unbosque.microservice_investor.service.SyncService;
import co.edu.unbosque.microservice_investor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/investor/account")
public class AccountController {

    private final UserService service;
    private final AccountService accountService;
    private final SyncService userAccountSyncService;


    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    public AccountController(UserService service, AccountService accountService, SyncService userAccountSyncService) {
        this.service = service;
        this.userAccountSyncService = userAccountSyncService;
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody AccountRequestDTO request) {
        var result = accountService.createAccount(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/bank/validate/{userId}")
    public ResponseEntity<BankRelationStatus> checkBankStatus(@PathVariable Integer userId) {
        BankRelationStatus response = accountService.checkBankRelationshipStatus(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/bank/register")
    public ResponseEntity<BankRelationStatus> createACH(@RequestBody BankRelationCreatedDTO request) {
        BankRelationStatus response = accountService.createAchRelationship(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recharge")
    public ResponseEntity<String> recharge(@RequestBody Map<String, Object> payload) {
        accountService.createRecharge(payload);
        return ResponseEntity.ok("Recarga creada con éxito, pendiente por confirmación.");
    }

    @GetMapping("/recharges/pending/check/{userId}")
    public ResponseEntity<String> checkPendingRecharges(@PathVariable Integer userId) {
        accountService.checkPendingRecharges(userId);
        return ResponseEntity.ok("Recargas pendientes revisadas y actualizadas correctamente.");
    }

    @GetMapping("/recharges/{userId}")
    public ResponseEntity<List<RechargeDTO>> getAllRechargesByUser(@PathVariable Integer userId) {
        List<RechargeDTO> recargas = accountService.getRechargeDTOsByUser(userId);
        return ResponseEntity.ok(recargas);
    }

    @GetMapping("/balance")
    public ResponseEntity<Map<String, Object>> getAccountBalance(@RequestHeader("Authorization") String authHeader) {
        // Extraer el token sin el "Bearer "
        String token = authHeader.replace("Bearer ", "");
        Integer userId = jwtUtil.extractUserId(token);

        float balance = service.getBalance(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("balance", balance);

        return ResponseEntity.ok(response);
    }


    @PutMapping("/sync")
    public ResponseEntity<Void> syncAccount(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String accountId
    ) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Integer userId = jwtUtil.extractUserId(token);

            userAccountSyncService.syncUserAccountData(userId, accountId);
            return ResponseEntity.ok().build();
        } catch (CustomAlpacaException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }




}
