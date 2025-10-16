package co.edu.unbosque.microservice_system_management.controller.implementation;

import co.edu.unbosque.microservice_system_management.controller.interfaces.IEconomySituationController;
import co.edu.unbosque.microservice_system_management.dto.response.EconomySituationResponseDTO;
import co.edu.unbosque.microservice_system_management.service.interfaces.IEconomySituationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class EconomySituationControllerImplementation implements IEconomySituationController {

    private final IEconomySituationService economySituationService;

    @Override
    public ResponseEntity<List<EconomySituationResponseDTO>> findAllActiveEconomySituations() {

        List<EconomySituationResponseDTO> economySituationsResponseDTO = this.economySituationService
                .findAllActiveEconomySituations();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(economySituationsResponseDTO);
    }
}
