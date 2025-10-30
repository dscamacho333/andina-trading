package co.edu.unbosque.microservice_market.controller;

import co.edu.unbosque.microservice_market.model.dto.BarDTO;
import co.edu.unbosque.microservice_market.model.dto.StockDTO;
import co.edu.unbosque.microservice_market.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/stocks")
public class MarketController {

    private final MarketService marketService;

    @Autowired
    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping
    public List<StockDTO> getAllStocks() throws Exception {
        return marketService.getAllStocks();
    }

    @GetMapping("/bars")
    public Map<String, List<BarDTO>> getBars(
            @RequestParam(value = "symbols", required = false) String symbolsCsv,
            @RequestParam(value = "lastDays", required = false, defaultValue = "10") Integer lastDays,
            @RequestParam(value = "timeframe", required = false, defaultValue = "1Day") String timeframe,
            @RequestParam(value = "feed", required = false, defaultValue = "iex") String feed,
            @RequestParam(value = "start", required = false) String startIso,
            @RequestParam(value = "end", required = false) String endIso
    ) {
        // Si no pasan start/end, usa lastDays
        if ((startIso == null || startIso.isBlank()) || (endIso == null || endIso.isBlank())) {
            Instant end = Instant.now();
            Instant start = end.minus(lastDays != null ? lastDays : 10, ChronoUnit.DAYS);
            endIso = end.toString();
            startIso = start.toString();
        }

        List<String> symbols = null;
        if (symbolsCsv != null && !symbolsCsv.isBlank()) {
            symbols = Arrays.asList(symbolsCsv.split("\\s*,\\s*"));
        }

        return marketService.getBarsForSymbols(symbols, timeframe, feed, startIso, endIso, 1000);
    }


}
