package co.edu.unbosque.microservice_system_management.controller.implementation;

import co.edu.unbosque.microservice_system_management.controller.interfaces.InInvestorController;
import co.edu.unbosque.microservice_system_management.dto.response.InvestorResponseDTO;
import co.edu.unbosque.microservice_system_management.service.interfaces.InInvestorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class InvestorControllerImplementation implements InInvestorController {

    private final InInvestorService investorService;

    @Override
    public ResponseEntity<InvestorResponseDTO> findInvestorById(Integer investorId) {
        InvestorResponseDTO response = this.investorService.findInvestorById(investorId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<Void> deleteInvestorById(Integer investorId) {
        this.investorService.deleteInvestorById(investorId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<List<InvestorResponseDTO>> findAllInvestorsByAccountStatus(String accountStatus) {
        List<InvestorResponseDTO> response = this.investorService.findAllInvestorsByAccountStatus(accountStatus);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
