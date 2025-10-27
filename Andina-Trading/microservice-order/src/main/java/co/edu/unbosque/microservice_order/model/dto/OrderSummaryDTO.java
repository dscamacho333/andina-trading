package co.edu.unbosque.microservice_order.model.dto;

import java.time.LocalDateTime;

public class OrderSummaryDTO {

    private String orderId;
    private String symbol;
    private int qty;
    private float filledAvgPrice;
    private String status;
    private String orderType;
    private LocalDateTime createdAt;
    private LocalDateTime executedAt;

    public OrderSummaryDTO() {}

    // Constructor completo
    public OrderSummaryDTO(String orderId, String symbol, int qty, float filledAvgPrice,
                           String status, String orderType, LocalDateTime createdAt, LocalDateTime executedAt) {
        this.orderId = orderId;
        this.symbol = symbol;
        this.qty = qty;
        this.filledAvgPrice = filledAvgPrice;
        this.status = status;
        this.orderType = orderType;
        this.createdAt = createdAt;
        this.executedAt = executedAt;
    }

    // Getters y Setters

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public float getFilledAvgPrice() {
        return filledAvgPrice;
    }

    public void setFilledAvgPrice(float filledAvgPrice) {
        this.filledAvgPrice = filledAvgPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(LocalDateTime executedAt) {
        this.executedAt = executedAt;
    }
}