package co.edu.unbosque.microservice_investor.service;


import co.edu.unbosque.microservice_investor.model.dto.PortfolioDTO;
import co.edu.unbosque.microservice_investor.model.dto.PositionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortfolioService {

    private final PositionService positionService;

    public PortfolioService( PositionService positionService) {
        this.positionService = positionService;
    }


    public PortfolioDTO getPortfolio(String accountId) {
        try {
            List<PositionDTO> positions = positionService.getPositions(accountId);

            PortfolioDTO portfolioDTO = new PortfolioDTO();
            portfolioDTO.setAccountId(accountId);
            portfolioDTO.setPositions(positions);

            return portfolioDTO;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}