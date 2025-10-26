package co.edu.unbosque.microservice_order.model.dto;


import java.time.LocalTime;
import java.util.List;

public class StockDTO {



    public class MarketDTO {

        private Integer id;
        private String marketCode;
        private LocalTime openTime;
        private LocalTime closeTime;
        private List<String> businessDays;
        private Integer editedByUserId;

        public MarketDTO() {
        }

        public MarketDTO(Integer id, String marketCode, LocalTime openTime, LocalTime closeTime, List<String> businessDays, Integer editedByUserId) {
            this.id = id;
            this.marketCode = marketCode;
            this.openTime = openTime;
            this.closeTime = closeTime;
            this.businessDays = businessDays;
            this.editedByUserId = editedByUserId;
        }

        public MarketDTO(Integer id, String marketCode) {
            this.id = id;
            this.marketCode = marketCode;
        }

        // Getters and Setters

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getMarketCode() {
            return marketCode;
        }

        public void setMarketCode(String marketCode) {
            this.marketCode = marketCode;
        }

        public LocalTime getOpenTime() {
            return openTime;
        }

        public void setOpenTime(LocalTime openTime) {
            this.openTime = openTime;
        }

        public LocalTime getCloseTime() {
            return closeTime;
        }

        public void setCloseTime(LocalTime closeTime) {
            this.closeTime = closeTime;
        }

        public List<String> getBusinessDays() {
            return businessDays;
        }

        public void setBusinessDays(List<String> businessDays) {
            this.businessDays = businessDays;
        }

        public Integer getEditedByUserId() {
            return editedByUserId;
        }

        public void setEditedByUserId(Integer editedByUserId) {
            this.editedByUserId = editedByUserId;
        }
    }





    private String symbol;
    private String stockName;
    private float change;
    private String sector;
    private String industry;
    private float currentPrice;
    private float volume;
    private float marketCapitalization;
    private String status;
    private MarketDTO market;


    public StockDTO() {}


    public StockDTO(String symbol, String stockName, float change, String sector, String industry, float currentPrice, float volume, float marketCapitalization, String status, MarketDTO market) {
        this.symbol = symbol;
        this.stockName = stockName;
        this.change = change;
        this.sector = sector;
        this.industry = industry;
        this.currentPrice = currentPrice;
        this.volume = volume;
        this.marketCapitalization = marketCapitalization;
        this.status = status;
        this.market = market;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(float currentPrice) {
        this.currentPrice = currentPrice;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getMarketCapitalization() {
        return marketCapitalization;
    }

    public void setMarketCapitalization(float marketCapitalization) {
        this.marketCapitalization = marketCapitalization;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MarketDTO getMarket() {
        return market;
    }

    public void setMarket(MarketDTO market) {
        this.market = market;
    }

    public float getChange() {
        return change;
    }

    public void setChange(float change) {
        this.change = change;
    }
}