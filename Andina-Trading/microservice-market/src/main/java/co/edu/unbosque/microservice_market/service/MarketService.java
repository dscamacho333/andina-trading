package co.edu.unbosque.microservice_market.service;


import co.edu.unbosque.microservice_market.model.dto.BarDTO;
import co.edu.unbosque.microservice_market.model.dto.MarketDTO;
import co.edu.unbosque.microservice_market.model.dto.StockDTO;
import co.edu.unbosque.microservice_market.model.entity.Market;
import co.edu.unbosque.microservice_market.model.external.AlpacaBarsResponse;
import co.edu.unbosque.microservice_market.repository.MarketRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class  MarketService {


    @Value("${alpaca.broker.base-url}")
    private String brokerBaseUrl;

    @Value("${alpaca.financial.url}")
    private String fmpApiUrl;

    @Value("${alpaca.financial.api-key}")
    private String fmpApiKey;

    @Value("${alpaca.market.base-url}")
    private String marketBaseUrl;

    @Value("${alpaca.market.feed:iex}")
    private String defaultFeed;

    @Value("${alpaca.market.default-watchlist:AAPL,MSFT,AMZN}")
    private String defaultWatchlistCsv;

    private final RestTemplate brokerRestTemplate;
    private final RestTemplate marketRestTemplate;
    private final MarketRepository marketRepository;
    private final RestTemplate fmpRestTemplate;
    private final ObjectMapper objectMapper;

    public MarketService(@Qualifier("brokerRestTemplate") RestTemplate brokerRestTemplate, @Qualifier("marketRestTemplate") RestTemplate marketRestTemplate,
                        MarketRepository marketRepository, ObjectMapper objectMapper) {
        this.brokerRestTemplate = brokerRestTemplate;
        this.marketRestTemplate = marketRestTemplate;
        this.marketRepository = marketRepository;
        this.objectMapper = objectMapper;
        this.fmpRestTemplate = new RestTemplate();
    }

    public List<StockDTO> getAllStocks() throws Exception {
        String assetsUrl = brokerBaseUrl + "/v1/assets?asset_class=us_equity";
        String response = brokerRestTemplate.getForObject(assetsUrl, String.class);

        if (response == null) {
            throw new IllegalStateException("Error: broker API returned null response");
        }


        List<Map<String, Object>> assets = objectMapper.readValue(response, new TypeReference<>() {
        });

        // Filtrar assets
        List<Map<String, Object>> filteredAssets = assets.stream()
                .filter(asset -> "active".equals(asset.get("status"))
                        && Boolean.TRUE.equals(asset.get("tradable"))
                        && Arrays.asList("NASDAQ", "NYSE", "AMEX").contains(asset.get("exchange")))
                .limit(36)
                .toList();

        List<String> symbols = filteredAssets.stream()
                .map(asset -> (String) asset.get("symbol"))
                .toList();


        // 🔹 2. Obtener datos de FMP (una sola vez)
        Map<String, Object>[] profiles = fmpRestTemplate.getForObject(
                fmpApiUrl + String.join(",", symbols) + "?&apikey=" + fmpApiKey,
                Map[].class
        );

        // Mapear perfiles por símbolo
        Map<String, Map<String, Object>> profileMap = new HashMap<>();
        if (profiles != null) {
            for (Map<String, Object> p : profiles) {
                profileMap.put((String) p.get("symbol"), p);
            }
        }

        // 🔹 3. Construir DTOs
        List<StockDTO> result = new ArrayList<>();
        for (Map<String, Object> asset : filteredAssets) {
            String symbol = (String) asset.get("symbol");
            String name = (String) asset.get("name");
            String status = (String) asset.get("status");
            String exchange = (String) asset.get("exchange");

            // Market
            Optional<Market> marketOpt = marketRepository.findByMarketCode(exchange);
            MarketDTO marketDTO = marketOpt.map(m -> new MarketDTO(m.getId(), m.getMarketCode())).orElse(null);


            // FMP data
            float price = 0, volume = 0, marketCap = 0, change = 0;
            String sector = "", industry = "";
            Map<String, Object> profile = profileMap.get(symbol);
            if (profile != null) {
                price = profile.get("price") != null ? Float.parseFloat(profile.get("price").toString()) : 0;
                volume = profile.get("volAvg") != null ? Float.parseFloat(profile.get("volAvg").toString()) : 0;
                marketCap = profile.get("mktCap") != null ? Float.parseFloat(profile.get("mktCap").toString()) : 0;
                change = profile.get("changes") != null ? Float.parseFloat(profile.get("changes").toString()) : 0;
                sector = profile.get("sector") != null ? profile.get("sector").toString() : "";
                industry = profile.get("industry") != null ? profile.get("industry").toString() : "";
            }

            StockDTO dto = new StockDTO(symbol, name, change, sector, industry, price, volume, marketCap, status, marketDTO);

            if (profile == null || profile.get("price") == null) {
                continue;
            }

            result.add(dto);
        }

        return result;
    }


    public Map<String, List<BarDTO>> getBarsForSymbols(
            List<String> symbols,
            String timeframe,
            String feed,
            String startIso,
            String endIso,
            Integer limit
    ) {
        String tf = (timeframe == null || timeframe.isBlank()) ? "1Day" : timeframe;
        String fd = (feed == null || feed.isBlank()) ? defaultFeed : feed;
        int lim = (limit == null || limit <= 0) ? 1000 : limit;

        List<String> syms = (symbols == null || symbols.isEmpty())
                ? Arrays.asList(defaultWatchlistCsv.split("\\s*,\\s*"))
                : symbols;

        Instant end = (endIso == null || endIso.isBlank()) ? Instant.now() : Instant.parse(endIso);
        Instant start = (startIso == null || startIso.isBlank()) ? end.minus(10, ChronoUnit.DAYS) : Instant.parse(startIso);

        Map<String, List<BarDTO>> acc = new LinkedHashMap<>();
        String pageToken = null;

        do {

            String url = marketBaseUrl + UriComponentsBuilder
                    .fromPath("/v2/stocks/bars")
                    .queryParam("symbols", String.join(",", syms))
                    .queryParam("timeframe", tf)
                    .queryParam("feed", fd)
                    .queryParam("limit", lim)
                    .queryParam("start", start.toString())
                    .queryParam("end", end.toString())
                    .queryParamIfPresent("page_token", Optional.ofNullable(pageToken))
                    .build(true)          // encode
                    .toUriString();       // <-- IMPORTANTE: String, no URI

            ResponseEntity<AlpacaBarsResponse> resp = marketRestTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    AlpacaBarsResponse.class
            );


            AlpacaBarsResponse body = resp.getBody();
            if (body == null || body.getBars() == null || body.getBars().isNull()) break;

            JsonNode barsNode = body.getBars();

            if (barsNode.isArray()) {
                for (JsonNode b : barsNode) {
                    String sym = b.hasNonNull("S") ? b.get("S").asText() : "(unknown)";
                    String t = b.path("t").asText();
                    double o = b.path("o").asDouble();
                    double h = b.path("h").asDouble();
                    double l = b.path("l").asDouble();
                    double c = b.path("c").asDouble();
                    long v = b.path("v").asLong();
                    Integer n = b.hasNonNull("n") ? b.get("n").asInt() : null;
                    Double vw = b.hasNonNull("vw") ? b.get("vw").asDouble() : null;

                    acc.computeIfAbsent(sym, k -> new ArrayList<>())
                            .add(new BarDTO(t, o, h, l, c, v, n, vw));
                }
            } else if (barsNode.isObject()) {
                barsNode.fields().forEachRemaining(entry -> {
                    String sym = entry.getKey();
                    JsonNode arr = entry.getValue();
                    if (arr != null && arr.isArray()) {
                        for (JsonNode b : arr) {
                            String t = b.path("t").asText();
                            double o = b.path("o").asDouble();
                            double h = b.path("h").asDouble();
                            double l = b.path("l").asDouble();
                            double c = b.path("c").asDouble();
                            long v = b.path("v").asLong();
                            Integer n = b.hasNonNull("n") ? b.get("n").asInt() : null;
                            Double vw = b.hasNonNull("vw") ? b.get("vw").asDouble() : null;

                            acc.computeIfAbsent(sym, k -> new ArrayList<>())
                                    .add(new BarDTO(t, o, h, l, c, v, n, vw));
                        }
                    }
                });
            } else {
                // Forma inesperada: log y corta
                // log.warn("Formato inesperado para 'bars': {}", barsNode);
                break;
            }

            pageToken = body.getNext_page_token();
        } while (pageToken != null);

        // Ordena por fecha cada lista (la UI lo agradecerá)
        acc.values().forEach(list -> list.sort(Comparator.comparing(BarDTO::getTime)));
        return acc;
    }

}
