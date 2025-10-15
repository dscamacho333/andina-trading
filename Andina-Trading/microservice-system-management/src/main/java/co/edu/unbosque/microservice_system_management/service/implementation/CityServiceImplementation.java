package co.edu.unbosque.microservice_system_management.service.implementation;

import co.edu.unbosque.microservice_system_management.dto.request.CityCreateDTO;
import co.edu.unbosque.microservice_system_management.dto.request.CityUpdateDTO;
import co.edu.unbosque.microservice_system_management.dto.response.CityResponseDTO;
import co.edu.unbosque.microservice_system_management.model.repository.ICityRepository;
import co.edu.unbosque.microservice_system_management.service.interfaces.ICityService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CityServiceImplementation implements ICityService {

    private final ICityRepository cityRepository;
    private final ModelMapper modelMapper;

    @Override
    public CityResponseDTO createCity(CityCreateDTO cityCreateDTO) {
        return null;
    }

    @Override
    public CityResponseDTO findCityById(Integer cityId) {
        return null;
    }

    @Override
    public CityResponseDTO updateCityById(Integer cityId, CityUpdateDTO cityUpdateDTO) {
        return null;
    }

    @Override
    public Void deleteCityById(Integer cityId) {
        return null;
    }

    @Override
    public List<CityResponseDTO> findAllActiveCities() {
        return List.of();
    }

    @Override
    public List<CityResponseDTO> findAllCitiesByEconomySituation(String economySituation) {
        return List.of();
    }
}
