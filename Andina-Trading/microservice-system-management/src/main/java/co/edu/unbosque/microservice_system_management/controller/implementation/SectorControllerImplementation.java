package co.edu.unbosque.microservice_system_management.controller.implementation;

import co.edu.unbosque.microservice_system_management.controller.interfaces.ISectorController;
import co.edu.unbosque.microservice_system_management.dto.response.SectorResponseDTO;
import co.edu.unbosque.microservice_system_management.service.interfaces.ISectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class SectorControllerImplementation implements ISectorController {

    private final ISectorService sectorService;

    @Override
    public ResponseEntity<List<SectorResponseDTO>> findAllActiveSectors() {

        List<SectorResponseDTO> sectorsResponseDTO = this.sectorService
                .findAllActiveSectors();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(sectorsResponseDTO);
    }
}
