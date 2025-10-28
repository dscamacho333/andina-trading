package co.edu.unbosque.microservice_investor.model.dto;

import java.util.List;

public class PortfolioDTO {
    private String accountId;
    private String cash;
    private String buyingPower;
    private String portfolioValue;
    private List<PositionDTO> positions;

    public PortfolioDTO() {}

    public PortfolioDTO(String accountId, String cash, String buyingPower, String portfolioValue, List<PositionDTO> positions) {
        this.accountId = accountId;
        this.cash = cash;
        this.buyingPower = buyingPower;
        this.portfolioValue = portfolioValue;
        this.positions = positions;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getBuyingPower() {
        return buyingPower;
    }

    public void setBuyingPower(String buyingPower) {
        this.buyingPower = buyingPower;
    }

    public String getPortfolioValue() {
        return portfolioValue;
    }

    public void setPortfolioValue(String portfolioValue) {
        this.portfolioValue = portfolioValue;
    }

    public List<PositionDTO> getPositions() {
        return positions;
    }

    public void setPositions(List<PositionDTO> positions) {
        this.positions = positions;
    }
}
