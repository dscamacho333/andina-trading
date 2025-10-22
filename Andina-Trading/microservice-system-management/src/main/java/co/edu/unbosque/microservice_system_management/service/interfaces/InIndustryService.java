package co.edu.unbosque.microservice_system_management.service.interfaces;

import co.edu.unbosque.microservice_system_management.dto.response.IndustryResponseDTO;

import java.util.List;

public interface InIndustryService {

    List<IndustryResponseDTO> findAllActiveIndustries();

}
