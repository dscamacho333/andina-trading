package co.edu.unbosque.microservice_market.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Configuration
public class RestTemplateConfig {

    @Value("${alpaca.broker.api-key-id}")
    private String brokerKey;

    @Value("${alpaca.broker.api-secret}")
    private String brokerSecret;

    @Bean("brokerRestTemplate")
    public RestTemplate brokerRestTemplate(RestTemplateBuilder builder) {
        String credentials = Base64.getEncoder()
                .encodeToString((brokerKey + ":" + brokerSecret).getBytes());

        return builder
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + credentials)
                .build();
    }

    @Bean("marketRestTemplate")
    public RestTemplate marketRestTemplate(RestTemplateBuilder builder,
                                           @Value("${alpaca.market.api-key-id}") String marketKey,
                                           @Value("${alpaca.market.api-secret-key}") String marketSecret) {
        return builder
                .defaultHeader("APCA-API-KEY-ID", marketKey)
                .defaultHeader("APCA-API-SECRET-KEY", marketSecret)
                .build();
    }
}
