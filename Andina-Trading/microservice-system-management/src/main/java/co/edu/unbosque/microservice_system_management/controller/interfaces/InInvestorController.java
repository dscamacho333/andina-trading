package co.edu.unbosque.microservice_system_management.controller.interfaces;

import co.edu.unbosque.microservice_system_management.dto.response.InvestorResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/investor")
public interface InInvestorController {

    @GetMapping("/{investorId}")
    ResponseEntity<InvestorResponseDTO> findInvestorById(
            @PathVariable Integer investorId
    );

    @DeleteMapping("/{investorId}")
    ResponseEntity<Void> deleteInvestorById(
            @PathVariable Integer investorId
    );

    @GetMapping("/status/{accountStatus}")
    ResponseEntity<List<InvestorResponseDTO>> findAllInvestorsByAccountStatus(
            @PathVariable String accountStatus
    );
}