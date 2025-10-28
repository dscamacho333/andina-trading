package co.edu.unbosque.microservice_investor.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public class PositionDTO {

    @JsonProperty("asset_id")
    private String assetId;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("exchange")
    private String exchange;

    @JsonProperty("asset_class")
    private String assetClass;

    @JsonProperty("asset_marginable")
    private boolean assetMarginable;

    private String qty;

    @JsonProperty("avg_entry_price")
    private String avgEntryPrice;

    private String side;

    @JsonProperty("market_value")
    private String marketValue;

    @JsonProperty("cost_basis")
    private String costBasis;

    @JsonProperty("unrealized_pl")
    private String unrealizedPl;

    @JsonProperty("unrealized_plpc")
    private String unrealizedPlpc;

    @JsonProperty("unrealized_intraday_pl")
    private String unrealizedIntradayPl;

    @JsonProperty("unrealized_intraday_plpc")
    private String unrealizedIntradayPlpc;

    @JsonProperty("current_price")
    private String currentPrice;

    @JsonProperty("lastday_price")
    private String lastdayPrice;

    @JsonProperty("change_today")
    private String changeToday;

    @JsonProperty("qty_available")
    private String qtyAvailable;

    public PositionDTO() {
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getAssetClass() {
        return assetClass;
    }

    public void setAssetClass(String assetClass) {
        this.assetClass = assetClass;
    }

    public boolean isAssetMarginable() {
        return assetMarginable;
    }

    public void setAssetMarginable(boolean assetMarginable) {
        this.assetMarginable = assetMarginable;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getAvgEntryPrice() {
        return avgEntryPrice;
    }

    public void setAvgEntryPrice(String avgEntryPrice) {
        this.avgEntryPrice = avgEntryPrice;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(String marketValue) {
        this.marketValue = marketValue;
    }

    public String getCostBasis() {
        return costBasis;
    }

    public void setCostBasis(String costBasis) {
        this.costBasis = costBasis;
    }

    public String getUnrealizedPl() {
        return unrealizedPl;
    }

    public void setUnrealizedPl(String unrealizedPl) {
        this.unrealizedPl = unrealizedPl;
    }

    public String getUnrealizedPlpc() {
        return unrealizedPlpc;
    }

    public void setUnrealizedPlpc(String unrealizedPlpc) {
        this.unrealizedPlpc = unrealizedPlpc;
    }

    public String getUnrealizedIntradayPl() {
        return unrealizedIntradayPl;
    }

    public void setUnrealizedIntradayPl(String unrealizedIntradayPl) {
        this.unrealizedIntradayPl = unrealizedIntradayPl;
    }

    public String getUnrealizedIntradayPlpc() {
        return unrealizedIntradayPlpc;
    }

    public void setUnrealizedIntradayPlpc(String unrealizedIntradayPlpc) {
        this.unrealizedIntradayPlpc = unrealizedIntradayPlpc;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getLastdayPrice() {
        return lastdayPrice;
    }

    public void setLastdayPrice(String lastdayPrice) {
        this.lastdayPrice = lastdayPrice;
    }

    public String getChangeToday() {
        return changeToday;
    }

    public void setChangeToday(String changeToday) {
        this.changeToday = changeToday;
    }

    public String getQtyAvailable() {
        return qtyAvailable;
    }

    public void setQtyAvailable(String qtyAvailable) {
        this.qtyAvailable = qtyAvailable;
    }
}

