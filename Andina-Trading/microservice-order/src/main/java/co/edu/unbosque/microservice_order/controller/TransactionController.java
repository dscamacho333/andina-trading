package co.edu.unbosque.microservice_order.controller;


import co.edu.unbosque.microservice_order.model.dto.RechargeDTO;
import co.edu.unbosque.microservice_order.model.dto.TransactionDTO;
import co.edu.unbosque.microservice_order.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByOrder(@PathVariable Integer orderId) {
        List<TransactionDTO> transactions = transactionService.findByOrderId(orderId);

        if (transactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(transactions);
    }

    @PostMapping
    public TransactionDTO createTransaction(@RequestBody TransactionDTO transaction) {
        return transactionService.createTransaction(transaction);
    }

    @GetMapping("/recharges/{userId}")
    public List<RechargeDTO> getRechargeDTOsByUser(@PathVariable Integer userId) {
        return transactionService.getRechargeDTOsByUser(userId);
    }

    @GetMapping("/recharges/pending/{userId}")
    public List<TransactionDTO> getPendingRechargesByUser(@PathVariable Integer userId) {
        return transactionService.getPendingRechargesByUser(userId);
    }

    @PutMapping()
    public TransactionDTO updateTransaction(@RequestBody TransactionDTO transaction) {
        transactionService.updateTransaction(transaction);
        return transaction;
    }


}
