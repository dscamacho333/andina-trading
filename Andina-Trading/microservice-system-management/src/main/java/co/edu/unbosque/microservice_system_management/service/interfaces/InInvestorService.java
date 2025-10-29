package co.edu.unbosque.microservice_system_management.service.interfaces;

import co.edu.unbosque.microservice_system_management.dto.response.InvestorResponseDTO;

import java.util.List;

public interface InInvestorService {


    InvestorResponseDTO findInvestorById(
            Integer investorId
    );

    void deleteInvestorById(
            Integer investorId
    );

    List<InvestorResponseDTO> findAllInvestorsByAccountStatus(
            String accountStatus
    );

}
