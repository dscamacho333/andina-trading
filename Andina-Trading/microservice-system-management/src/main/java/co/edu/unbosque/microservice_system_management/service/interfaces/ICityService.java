package co.edu.unbosque.microservice_system_management.service.interfaces;

import co.edu.unbosque.microservice_system_management.dto.request.CityCreateDTO;
import co.edu.unbosque.microservice_system_management.dto.request.CityUpdateDTO;
import co.edu.unbosque.microservice_system_management.dto.response.CityResponseDTO;

import java.util.List;

public interface ICityService {

    CityResponseDTO createCity(
            CityCreateDTO cityCreateDTO
    );

    CityResponseDTO findCityById(
            Integer cityId
    );

    CityResponseDTO updateCityById(
            Integer cityId,
            CityUpdateDTO cityUpdateDTO
    );

    Void deleteCityById(
            Integer cityId
    );

    List<CityResponseDTO> findAllActiveCities();

    List<CityResponseDTO> findAllCitiesByEconomySituation(
            String economySituation
    );

}
