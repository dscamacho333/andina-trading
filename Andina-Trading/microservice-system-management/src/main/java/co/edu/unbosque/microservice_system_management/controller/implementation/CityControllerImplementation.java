package co.edu.unbosque.microservice_system_management.controller.implementation;

import co.edu.unbosque.microservice_system_management.controller.interfaces.ICityController;
import co.edu.unbosque.microservice_system_management.dto.request.CityCreateDTO;
import co.edu.unbosque.microservice_system_management.dto.request.CityUpdateDTO;
import co.edu.unbosque.microservice_system_management.dto.response.CityResponseDTO;
import co.edu.unbosque.microservice_system_management.service.interfaces.ICityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CityControllerImplementation implements ICityController {

    private final ICityService cityService;

    @Override
    public ResponseEntity<CityResponseDTO> createCity(CityCreateDTO cityCreateDTO) {

        CityResponseDTO cityResponseDTO = this.cityService
                .createCity(cityCreateDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cityResponseDTO);
    }

    @Override
    public ResponseEntity<CityResponseDTO> findCityById(Integer cityId) {

        CityResponseDTO cityResponseDTO = this.cityService
                .findCityById(cityId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cityResponseDTO);
    }

    @Override
    public ResponseEntity<CityResponseDTO> updateCityById(Integer cityId, CityUpdateDTO cityUpdateDTO) {

        CityResponseDTO cityResponseDTO = this.cityService
                .updateCityById(
                        cityId,
                        cityUpdateDTO
                );

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(cityResponseDTO);
    }

    @Override
    public ResponseEntity<Void> deleteCityById(Integer cityId) {

        this.cityService
                .deleteCityById(cityId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Override
    public ResponseEntity<List<CityResponseDTO>> findAllActiveCities() {

        List<CityResponseDTO> citiesResponseDTO = this.cityService
                .findAllActiveCities();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(citiesResponseDTO);
    }

    @Override
    public ResponseEntity<List<CityResponseDTO>> findAllCitiesByEconomySituation(String economySituation) {

        List<CityResponseDTO> citiesResponseDTO = this.cityService
                .findAllCitiesByEconomySituation(economySituation);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(citiesResponseDTO);
    }
}
