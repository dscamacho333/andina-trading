package co.edu.unbosque.microservice_order.service;

import co.edu.unbosque.microservice_order.model.dto.RechargeDTO;
import co.edu.unbosque.microservice_order.model.dto.TransactionDTO;
import co.edu.unbosque.microservice_order.model.entity.Transaction;
import co.edu.unbosque.microservice_order.model.enums.TransactionStatus;
import co.edu.unbosque.microservice_order.model.enums.TransactionType;
import co.edu.unbosque.microservice_order.repository.TransactionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, ModelMapper modelMapper) {
        this.transactionRepository = transactionRepository;
        this.modelMapper = modelMapper;
    }

    public TransactionDTO createTransaction(TransactionDTO transaction) {
        return modelMapper.map(
                transactionRepository.save(
                        modelMapper.map(transaction, Transaction.class)
                )
                , TransactionDTO.class);
    }

    public List<TransactionDTO> getPendingRechargesByUser(Integer userId) {
        return transactionRepository.findByUserIdAndTypeAndStatus(
                userId,
                TransactionType.RECHARGE,
                TransactionStatus.PENDING
        ).stream()
                .map(tx -> new TransactionDTO(
                        tx.getTransactionId(),
                        tx.getUserId(),
                        tx.getOrderId(),
                        tx.getAmount(),
                        tx.getType(),
                        tx.getDescription(),
                        tx.getCreatedAt(),
                        tx.getStatus(),
                        tx.getAlpacaId()
                ))
                .toList();
    }

    public void updateTransaction(TransactionDTO transaction) {
        transactionRepository.save(modelMapper.map(transaction,  Transaction.class));
    }

    public List<RechargeDTO> getRechargeDTOsByUser(Integer userId) {
        return transactionRepository.findByUserIdAndType(userId, TransactionType.RECHARGE)
                .stream()
                .map(tx -> new RechargeDTO(
                        tx.getAmount(),
                        tx.getDescription(),
                        tx.getCreatedAt(),
                        tx.getStatus()
                ))
                .toList();
    }

    public void updateTransactionAmountsAndStatus(Integer orderId, float amount, TransactionType type, TransactionStatus status) {
        Transaction transaction = transactionRepository.findByOrderIdAndType(orderId, type)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada para orden " + orderId + " y tipo " + type));

        transaction.setAmount(amount);
        transaction.setStatus(status);
        transactionRepository.save(transaction);
    }

    public void cancelTransactionsByOrder(Integer orderId) {
        List<Transaction> transactions = transactionRepository.findByOrderId(orderId);

        for (Transaction transaction : transactions) {
            transaction.setStatus(TransactionStatus.CANCELED);
            transactionRepository.save(transaction);
        }
    }

    public List<Transaction> findByOrderId(Integer orderId) {
        return transactionRepository.findByOrderId(orderId);
    }


}