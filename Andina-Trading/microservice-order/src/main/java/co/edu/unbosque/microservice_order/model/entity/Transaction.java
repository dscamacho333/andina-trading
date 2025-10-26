package co.edu.unbosque.microservice_order.model.entity;

import co.edu.unbosque.microservice_order.model.enums.TransactionStatus;
import co.edu.unbosque.microservice_order.model.enums.TransactionType;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "order_id")
    private Integer orderId; // Nullable, solo aplica si la transacción está asociada a una orden

    @Column(name = "amount", nullable = false)
    private float amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TransactionStatus status;

    @Column(name = "alpaca_id")
    private String alpacaId;


    public Transaction() {
    }

    public Transaction(Integer id, Integer userId, Integer orderId, float amount, TransactionType type, String description, LocalDateTime createdAt, TransactionStatus status, String alpacaId) {
        this.id = id;
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

    // Getters y Setters

    public Integer getTransactionId() {
        return id;
    }

    public void setTransactionId(Integer transactionId) {
        this.id = transactionId;
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
