package co.edu.unbosque.microservice_system_management.controller.implementation;

import co.edu.unbosque.microservice_system_management.controller.interfaces.InIndustryController;
import co.edu.unbosque.microservice_system_management.dto.response.IndustryResponseDTO;
import co.edu.unbosque.microservice_system_management.service.interfaces.InIndustryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class IndustryControllerImplementation implements InIndustryController {

    private final InIndustryService industryService;

    @Override
    public ResponseEntity<List<IndustryResponseDTO>> findAllActiveIndustries() {

        List<IndustryResponseDTO> industriesResponseDTO = this.industryService
                .findAllActiveIndustries();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(industriesResponseDTO);
    }
}
