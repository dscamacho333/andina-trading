package co.edu.unbosque.microservice_investor.model.dto;

import java.math.BigDecimal;


public class AccountInfoDTO {

    private String id;                   // ID real de la cuenta en Alpaca
    private BigDecimal cash;              // Efectivo disponible
    private BigDecimal buyingPower;       // Poder de compra
    private BigDecimal portfolioValue;    // Valor total del portafolio

    public AccountInfoDTO(String id, BigDecimal cash, BigDecimal buyingPower, BigDecimal portfolioValue) {
        this.id = id;
        this.cash = cash;
        this.buyingPower = buyingPower;
        this.portfolioValue = portfolioValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public BigDecimal getBuyingPower() {
        return buyingPower;
    }

    public void setBuyingPower(BigDecimal buyingPower) {
        this.buyingPower = buyingPower;
    }

    public BigDecimal getPortfolioValue() {
        return portfolioValue;
    }

    public void setPortfolioValue(BigDecimal portfolioValue) {
        this.portfolioValue = portfolioValue;
    }
}