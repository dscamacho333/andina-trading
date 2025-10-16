package co.edu.unbosque.microservice_system_management.controller.interfaces;

import co.edu.unbosque.microservice_system_management.dto.request.CityCreateDTO;
import co.edu.unbosque.microservice_system_management.dto.request.CityUpdateDTO;
import co.edu.unbosque.microservice_system_management.dto.response.CityResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/city")
public interface ICityController {

    @PostMapping
    ResponseEntity<CityResponseDTO> createCity(
            @RequestBody @Valid CityCreateDTO cityCreateDTO
    );

    @GetMapping("/{cityId}")
    ResponseEntity<CityResponseDTO> findCityById(
            @PathVariable Integer cityId
    );

    @PutMapping("/{cityId}")
    ResponseEntity<CityResponseDTO> updateCityById(
            @PathVariable Integer cityId,
            @RequestBody @Valid CityUpdateDTO cityUpdateDTO
    );

    @DeleteMapping("/{cityId}")
    ResponseEntity<Void> deleteCityById(
            @PathVariable Integer cityId
    );

    @GetMapping
    ResponseEntity<List<CityResponseDTO>> findAllActiveCities();

    @GetMapping("/situation/{economySituation}")
    ResponseEntity<List<CityResponseDTO>> findAllCitiesByEconomySituation(
            @PathVariable String economySituation
    );

}
