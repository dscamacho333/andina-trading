package co.edu.unbosque.microservice_system_management.service.interfaces;

import co.edu.unbosque.microservice_system_management.dto.response.SectorResponseDTO;

import java.util.List;

public interface ISectorService {

    List<SectorResponseDTO> findAllActiveSectors();

}
