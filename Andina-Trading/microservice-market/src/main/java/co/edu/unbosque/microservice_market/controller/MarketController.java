package co.edu.unbosque.microservice_market.controller;

import co.edu.unbosque.microservice_market.model.dto.StockDTO;
import co.edu.unbosque.microservice_market.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
//@CrossOrigin(origins = "*")
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

}
