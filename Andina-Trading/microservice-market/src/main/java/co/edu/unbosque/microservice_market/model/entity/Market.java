package co.edu.unbosque.microservice_market.model.entity;

import co.edu.unbosque.microservice_market.util.ListToJsonConverter;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "markets")
public class Market {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "market_id")
    private Integer id;

    @Column(name = "market_code", nullable = false, unique = true, length = 10)
    private String marketCode;

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;

    @Convert(converter = ListToJsonConverter.class)
    @Column(name = "business_days", columnDefinition = "json", nullable = false)
    private List<String> businessDays;


    // Constructors
    public Market() {
    }

    public Market(Integer id, String marketCode) {
        this.id = id;
        this.marketCode = marketCode;
    }

    public Market(Integer id, String marketCode, LocalTime openTime, LocalTime closeTime, List<String> businessDays) {
        this.id = id;
        this.marketCode = marketCode;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.businessDays = businessDays;
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

}
