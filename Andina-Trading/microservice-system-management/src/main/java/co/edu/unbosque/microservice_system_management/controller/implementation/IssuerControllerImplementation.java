package co.edu.unbosque.microservice_system_management.controller.implementation;

import co.edu.unbosque.microservice_system_management.controller.interfaces.InIssuerController;
import co.edu.unbosque.microservice_system_management.dto.request.IssuerCreateDTO;
import co.edu.unbosque.microservice_system_management.dto.request.IssuerUpdateDTO;
import co.edu.unbosque.microservice_system_management.dto.response.IssuerResponseDTO;
import co.edu.unbosque.microservice_system_management.service.interfaces.InIssuerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class IssuerControllerImplementation implements InIssuerController {

    private final InIssuerService issuerService;

    @Override
    public ResponseEntity<IssuerResponseDTO> createIssuer(IssuerCreateDTO issuerCreateDTO) {

        IssuerResponseDTO issuerResponseDTO = this.issuerService
                .createIssuer(issuerCreateDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(issuerResponseDTO);
    }

    @Override
    public ResponseEntity<IssuerResponseDTO> findIssuerById(Integer issuerId) {

        IssuerResponseDTO issuerResponseDTO = this.issuerService
                .findIssuerById(issuerId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(issuerResponseDTO);
    }

    @Override
    public ResponseEntity<IssuerResponseDTO> updateIssuerById(Integer issuerId, IssuerUpdateDTO issuerUpdateDTO) {

        IssuerResponseDTO issuerResponseDTO = this.issuerService
                .updateIssuerById(
                        issuerId,
                        issuerUpdateDTO
                );

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(issuerResponseDTO);
    }

    @Override
    public ResponseEntity<Void> deleteIssuerById(Integer issuerId) {

        this.issuerService
                .deleteIssuerById(issuerId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Override
    public ResponseEntity<List<IssuerResponseDTO>> findAllActiveIssuers() {

        List<IssuerResponseDTO> issuersResponseDTO = this.issuerService
                .findAllActiveIssuers();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(issuersResponseDTO);
    }

    @Override
    public ResponseEntity<List<IssuerResponseDTO>> findAllIssuersByCountry(Integer countryId) {

        List<IssuerResponseDTO> issuersResponseDTO = this.issuerService
                .findAllIssuersByCountry(countryId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(issuersResponseDTO);
    }

    @Override
    public ResponseEntity<List<IssuerResponseDTO>> findAllIssuersByIndustry(Integer industryId) {

        List<IssuerResponseDTO> issuersResponseDTO = this.issuerService
                .findAllIssuersByIndustry(industryId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(issuersResponseDTO);
    }

    @Override
    public ResponseEntity<List<IssuerResponseDTO>> findAllIssuersBySector(Integer sectorId) {

        List<IssuerResponseDTO> issuersResponseDTO = this.issuerService
                .findAllIssuersBySector(sectorId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(issuersResponseDTO);
    }
}
