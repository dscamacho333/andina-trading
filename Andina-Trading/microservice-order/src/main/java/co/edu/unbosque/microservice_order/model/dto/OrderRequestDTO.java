package co.edu.unbosque.microservice_order.model.dto;


import co.edu.unbosque.microservice_order.model.enums.OrderType;
import co.edu.unbosque.microservice_order.model.enums.TimeInForceOrder;

import java.time.LocalTime;
import java.util.List;

public class OrderRequestDTO {



    private UserSessionDTO user;
    private OrderType orderType;
    private Integer quantity;
    private TimeInForceOrder timeInForce;
    private Float limitPrice;
    private Float stopPrice;
    private boolean requiresSignature;
    private StockDTO stock;
    private Integer brokerId;



    public void setStopPrice(Float stopPrice) {
        this.stopPrice = stopPrice;
    }

    public Integer getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(Integer brokerId) {
        this.brokerId = brokerId;
    }

    public UserSessionDTO getUser() {
        return user;
    }

    public void setUser(UserSessionDTO user) {
        this.user = user;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public TimeInForceOrder getTimeInForce() {
        return timeInForce;
    }

    public void setTimeInForce(TimeInForceOrder timeInForce) {
        this.timeInForce = timeInForce;
    }

    public Float getLimitPrice() {
        return limitPrice;
    }

    public void setLimitPrice(Float limitPrice) {
        this.limitPrice = limitPrice;
    }

    public Float getStopPrice() {
        return stopPrice;
    }


    public boolean isRequiresSignature() {
        return requiresSignature;
    }

    public void setRequiresSignature(boolean requiresSignature) {
        this.requiresSignature = requiresSignature;
    }

    public StockDTO getStock() {
        return stock;
    }

    public void setStock(StockDTO stock) {
        this.stock = stock;
    }
}
