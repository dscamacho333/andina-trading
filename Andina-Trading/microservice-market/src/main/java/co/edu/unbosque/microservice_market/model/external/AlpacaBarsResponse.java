package co.edu.unbosque.microservice_market.model.external;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

public class AlpacaBarsResponse {
    private JsonNode bars;           // <-- acepta array u objeto
    private String next_page_token;

    public JsonNode getBars() { return bars; }
    public void setBars(JsonNode bars) { this.bars = bars; }

    public String getNext_page_token() { return next_page_token; }
    public void setNext_page_token(String next_page_token) { this.next_page_token = next_page_token; }
}
