package co.edu.unbosque.microservice_investor.model.dto;


import co.edu.unbosque.microservice_investor.model.enums.TransactionStatus;

import java.time.LocalDateTime;

public class RechargeDTO {

    private float amount;
    private String description;
    private LocalDateTime createdAt;
    private TransactionStatus status;

    public RechargeDTO(float amount, String description, LocalDateTime createdAt, TransactionStatus status) {
        this.amount = amount;
        this.description = description;
        this.createdAt = createdAt;
        this.status = status;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
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

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
}
