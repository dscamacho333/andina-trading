package co.edu.unbosque.microservice_market.model.dto;

public class BarDTO {
    private String time;   // ISO-8601 UTC
    private double open;
    private double high;
    private double low;
    private double close;
    private long volume;
    private Integer trades; // n
    private Double vwap;    // vw

    public BarDTO() {}

    public BarDTO(String time, double open, double high, double low, double close, long volume, Integer trades, Double vwap) {
        this.time = time;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.trades = trades;
        this.vwap = vwap;
    }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public double getOpen() { return open; }
    public void setOpen(double open) { this.open = open; }
    public double getHigh() { return high; }
    public void setHigh(double high) { this.high = high; }
    public double getLow() { return low; }
    public void setLow(double low) { this.low = low; }
    public double getClose() { return close; }
    public void setClose(double close) { this.close = close; }
    public long getVolume() { return volume; }
    public void setVolume(long volume) { this.volume = volume; }
    public Integer getTrades() { return trades; }
    public void setTrades(Integer trades) { this.trades = trades; }
    public Double getVwap() { return vwap; }
    public void setVwap(Double vwap) { this.vwap = vwap; }
}
