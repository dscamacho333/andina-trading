package co.edu.unbosque.microservice_investor.client;

import co.edu.unbosque.microservice_investor.model.dto.RechargeDTO;
import co.edu.unbosque.microservice_investor.model.dto.TransactionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-order")
public interface  TransactionClient {

    @PostMapping("/api/transaction")
    TransactionDTO createTransaction(@RequestBody TransactionDTO transaction);

    @GetMapping("/api/transaction/recharges/{userId}")
    List<RechargeDTO> getRechargeDTOsByUser(@PathVariable Integer userId);

    @GetMapping("/api/transaction/recharges/pending/{userId}")
    List<TransactionDTO> getPendingRechargesByUser(@PathVariable Integer userId);

    @PutMapping("/api/transaction")
    TransactionDTO updateTransaction(@RequestBody TransactionDTO transaction);

}
