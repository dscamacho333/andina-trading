package co.edu.unbosque.microservice_investor.service;


import co.edu.unbosque.microservice_investor.model.dto.PositionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class PositionService {

    @Value("${alpaca.broker.api-key-id}")
    private String alpacaApiKey;

    @Value("${alpaca.broker.api-secret}")
    private String alpacaApiSecret;

    @Value("${alpaca.broker.base-url}")
    private String alpacaApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<PositionDTO> getPositions(String accountId) {
        HttpHeaders headers = buildAlpacaHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String positionsUrl = alpacaApiUrl + "/v1/trading/accounts/" + accountId + "/positions";
        ResponseEntity<PositionDTO[]> response = restTemplate.exchange(
                positionsUrl,
                HttpMethod.GET,
                entity,
                PositionDTO[].class
        );

        return Arrays.asList(response.getBody());
    }

    private HttpHeaders buildAlpacaHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(alpacaApiKey, alpacaApiSecret);
        return headers;
    }
}