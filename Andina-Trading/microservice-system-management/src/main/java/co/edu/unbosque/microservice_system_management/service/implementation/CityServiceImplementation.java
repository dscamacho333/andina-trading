package co.edu.unbosque.microservice_system_management.service.implementation;

import co.edu.unbosque.microservice_system_management.dto.request.CityCreateDTO;
import co.edu.unbosque.microservice_system_management.dto.request.CityUpdateDTO;
import co.edu.unbosque.microservice_system_management.dto.response.CityResponseDTO;
import co.edu.unbosque.microservice_system_management.exception.CityNotFoundException;
import co.edu.unbosque.microservice_system_management.model.entity.City;
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

        City city = this.modelMapper
                .map(
                        cityCreateDTO,
                        City.class
                );

        city = this.cityRepository
                .save(city);


        CityResponseDTO cityResponseDTO = this.modelMapper
                .map(
                        city,
                        CityResponseDTO.class
                );

        return cityResponseDTO;
    }

    @Override
    public CityResponseDTO findCityById(Integer cityId) {

        City city = this.cityRepository
                .findById(cityId)
                .orElseThrow(
                        () -> new CityNotFoundException("City with ID: " + cityId + " does not exist.")
                );

        CityResponseDTO cityResponseDTO = this.modelMapper
                .map(
                        city,
                        CityResponseDTO.class
                );

        return cityResponseDTO;
    }

    @Override
    public CityResponseDTO updateCityById(Integer cityId, CityUpdateDTO cityUpdateDTO) {

        this.findCityById(cityId);

        City city = this.modelMapper
                .map(
                        cityUpdateDTO,
                        City.class
                );

        city.setId(cityId);

        city = this.cityRepository
                .save(city);

        CityResponseDTO cityResponseDTO = this.modelMapper
                .map(
                        city,
                        CityResponseDTO.class
                );

        return cityResponseDTO;
    }

    @Override
    public void deleteCityById(Integer cityId) {

        this.findCityById(cityId);

        this.cityRepository
                .deleteCityById(cityId);

    }

    @Override
    public List<CityResponseDTO> findAllActiveCities() {

        List<City> cities = this.cityRepository
                .findAllActiveCities();

        List<CityResponseDTO> citiesResponseDTO = cities
                .stream()
                .map(
                        (city) -> this.modelMapper
                                .map(
                                        city,
                                        CityResponseDTO.class
                                )
                )
                .toList();

        return citiesResponseDTO;
    }

    @Override
    public List<CityResponseDTO> findAllCitiesByEconomySituation(String economySituation) {

        List<City> cities = this.cityRepository
                .findAllCitiesByEconomySituation(economySituation);

        List<CityResponseDTO> citiesResponseDTO = cities
                .stream()
                .map(
                        (city) -> this.modelMapper
                                .map(
                                        city,
                                        CityResponseDTO.class
                                )
                )
                .toList();

        return citiesResponseDTO;
    }
}
