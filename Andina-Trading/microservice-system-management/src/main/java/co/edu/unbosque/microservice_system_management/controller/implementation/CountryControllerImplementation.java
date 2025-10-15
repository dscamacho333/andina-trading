package co.edu.unbosque.microservice_system_management.controller.implementation;

import co.edu.unbosque.microservice_system_management.controller.interfaces.ICountryController;
import co.edu.unbosque.microservice_system_management.dto.request.CountryCreateDTO;
import co.edu.unbosque.microservice_system_management.dto.request.CountryUpdateDTO;
import co.edu.unbosque.microservice_system_management.dto.response.CountryResponseDTO;
import co.edu.unbosque.microservice_system_management.service.interfaces.ICountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CountryControllerImplementation implements ICountryController {

    private final ICountryService countryService;

    @Override
    public ResponseEntity<CountryResponseDTO> createCountry(CountryCreateDTO countryCreateDTO) {

        CountryResponseDTO countryResponseDTO = this.countryService
                .createCountry(countryCreateDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(countryResponseDTO);
    }

    @Override
    public ResponseEntity<CountryResponseDTO> findCountryById(Integer countryId) {

        CountryResponseDTO countryResponseDTO = this.countryService
                .findCountryById(countryId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(countryResponseDTO);
    }

    @Override
    public ResponseEntity<CountryResponseDTO> updateCountryById(Integer countryId, CountryUpdateDTO countryUpdateDTO) {

        CountryResponseDTO countryResponseDTO = this.countryService
                .updateCountryById(
                        countryId,
                        countryUpdateDTO
                );

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(countryResponseDTO);
    }

    @Override
    public ResponseEntity<Void> deleteCountryById(Integer countryId) {

        this.countryService
                .deleteCountryById(countryId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Override
    public ResponseEntity<List<CountryResponseDTO>> findAllActiveCountries() {

        List<CountryResponseDTO> countriesResponseDTO = this.countryService
                .findAllActiveCountries();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(countriesResponseDTO);
    }

}
