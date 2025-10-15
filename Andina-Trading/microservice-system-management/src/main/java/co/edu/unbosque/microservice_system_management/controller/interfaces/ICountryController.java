package co.edu.unbosque.microservice_system_management.controller.interfaces;

import co.edu.unbosque.microservice_system_management.dto.request.CountryCreateDTO;
import co.edu.unbosque.microservice_system_management.dto.request.CountryUpdateDTO;
import co.edu.unbosque.microservice_system_management.dto.response.CountryResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/country")
public interface ICountryController {

    @PostMapping
    ResponseEntity<CountryResponseDTO> createCountry(
            @RequestBody @Valid CountryCreateDTO countryCreateDTO
            );

    @GetMapping("/{countryId}")
    ResponseEntity<CountryResponseDTO> findCountryById(
            @PathVariable Integer countryId
    );

    @PutMapping("/{countryId}")
    ResponseEntity<CountryResponseDTO> updateCountryById(
            @PathVariable Integer countryId,
            @RequestBody @Valid CountryUpdateDTO countryUpdateDTO
            );

    @DeleteMapping("/{countryId}")
    ResponseEntity<Void> deleteCountryById(
            @PathVariable Integer countryId
    );

    @GetMapping
    ResponseEntity<List<CountryResponseDTO>> findAllActiveCountries();

}
