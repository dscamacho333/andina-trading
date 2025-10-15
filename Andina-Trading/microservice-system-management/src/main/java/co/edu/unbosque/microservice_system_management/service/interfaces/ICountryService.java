package co.edu.unbosque.microservice_system_management.service.interfaces;

import co.edu.unbosque.microservice_system_management.dto.request.CountryCreateDTO;
import co.edu.unbosque.microservice_system_management.dto.request.CountryUpdateDTO;
import co.edu.unbosque.microservice_system_management.dto.response.CountryResponseDTO;

import java.util.List;

public interface ICountryService {

    CountryResponseDTO createCountry(
            CountryCreateDTO countryCreateDTO
    );

    CountryResponseDTO findCountryById(
            Integer countryId
    );

    CountryResponseDTO updateCountryById(
            Integer countryId,
            CountryUpdateDTO countryUpdateDTO
    );

    void deleteCountryById(
            Integer countryId
    );

    List<CountryResponseDTO> findAllActiveCountries();

}
