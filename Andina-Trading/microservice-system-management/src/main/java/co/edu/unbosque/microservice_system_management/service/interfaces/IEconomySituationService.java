package co.edu.unbosque.microservice_system_management.service.interfaces;

import co.edu.unbosque.microservice_system_management.dto.response.EconomySituationResponseDTO;

import java.util.List;

public interface IEconomySituationService {

    List<EconomySituationResponseDTO> findAllActiveEconomySituations();

}
