package co.edu.unbosque.microservice_system_management.controller.interfaces;

import co.edu.unbosque.microservice_system_management.dto.request.IssuerCreateDTO;
import co.edu.unbosque.microservice_system_management.dto.request.IssuerUpdateDTO;
import co.edu.unbosque.microservice_system_management.dto.response.IssuerResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/issuer")
public interface InIssuerController {

    @PostMapping
    ResponseEntity<IssuerResponseDTO> createIssuer(
            @RequestBody IssuerCreateDTO issuerCreateDTO
    );

    @GetMapping("/{issuerId}")
    ResponseEntity<IssuerResponseDTO> findIssuerById(
            @PathVariable Integer issuerId
    );

    @PutMapping("/{issuerId}")
    ResponseEntity<IssuerResponseDTO> updateIssuerById(
            @PathVariable Integer issuerId,
            @RequestBody IssuerUpdateDTO issuerUpdateDTO
    );

    @DeleteMapping("/{issuerId}")
    ResponseEntity<Void> deleteIssuerById(
            @PathVariable Integer issuerId
    );

    @GetMapping()
    ResponseEntity<List<IssuerResponseDTO>> findAllActiveIssuers();

    @GetMapping("/country/{countryId}")
    ResponseEntity<List<IssuerResponseDTO>> findAllIssuersByCountry(
            @PathVariable Integer countryId
    );

    @GetMapping("/industry/{industryId}")
    ResponseEntity<List<IssuerResponseDTO>> findAllIssuersByIndustry(
            @PathVariable Integer industryId
    );

    @GetMapping("/sector/{sectorId}")
    ResponseEntity<List<IssuerResponseDTO>> findAllIssuersBySector(
            @PathVariable Integer sectorId
    );

}
