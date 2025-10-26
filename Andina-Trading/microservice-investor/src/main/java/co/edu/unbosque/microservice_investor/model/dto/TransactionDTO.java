package co.edu.unbosque.microservice_investor.model.dto;


import co.edu.unbosque.microservice_investor.model.enums.TransactionStatus;
import co.edu.unbosque.microservice_investor.model.enums.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {

    private Integer transactionId;
    private Integer userId;
    private Integer orderId;
    private float amount;
    private TransactionType type;
    private String description;
    private LocalDateTime createdAt;
    private TransactionStatus status;
    private String alpacaId;

    public TransactionDTO() {
    }


    public TransactionDTO(Integer transactionId, Integer userId, Integer orderId, float amount, TransactionType type, String description, LocalDateTime createdAt, TransactionStatus status, String alpacaId) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.orderId = orderId;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.createdAt = createdAt;
        this.status = status;
        this.alpacaId = alpacaId;
    }

    public String getAlpacaId() {
        return alpacaId;
    }

    public void setAlpacaId(String alpacaId) {
        this.alpacaId = alpacaId;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}