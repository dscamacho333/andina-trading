package co.edu.unbosque.microservice_system_management.service.implementation;

import co.edu.unbosque.microservice_system_management.dto.request.CountryCreateDTO;
import co.edu.unbosque.microservice_system_management.dto.request.CountryUpdateDTO;
import co.edu.unbosque.microservice_system_management.dto.response.CountryResponseDTO;
import co.edu.unbosque.microservice_system_management.exception.CountryNotFoundException;
import co.edu.unbosque.microservice_system_management.model.entity.Country;
import co.edu.unbosque.microservice_system_management.model.repository.ICountryRepository;
import co.edu.unbosque.microservice_system_management.service.interfaces.ICountryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CountryServiceImplementation implements ICountryService {

    private final ICountryRepository countryRepository;
    private final ModelMapper modelMapper;

    @Override
    public CountryResponseDTO createCountry(CountryCreateDTO countryCreateDTO) {

        Country country = this.modelMapper
                .map(
                        countryCreateDTO,
                        Country.class
                );

        country = this.countryRepository
                .save(country);

        CountryResponseDTO countryResponseDTO = this.modelMapper
                .map(
                        country,
                        CountryResponseDTO.class
                );

        return countryResponseDTO;
    }

    @Override
    public CountryResponseDTO findCountryById(Integer countryId) {

        Country country = this.countryRepository
                .findById(countryId)
                .orElseThrow(
                        () -> new CountryNotFoundException("Country with ID: " + countryId + " does not exist.")
                );

        CountryResponseDTO countryResponseDTO = this.modelMapper
                .map(
                        country,
                        CountryResponseDTO.class
                );

        return countryResponseDTO;
    }

    @Override
    public CountryResponseDTO updateCountryById(Integer countryId, CountryUpdateDTO countryUpdateDTO) {

        this.findCountryById(countryId);

        Country country = this.modelMapper
                .map(
                        countryUpdateDTO,
                        Country.class
                );

        country.setId(countryId);
        country.setActive(true);

        country = this.countryRepository
                .save(country);

        CountryResponseDTO countryResponseDTO = this.modelMapper
                .map(
                        country,
                        CountryResponseDTO.class
                );

        return countryResponseDTO;
    }

    @Override
    public void deleteCountryById(Integer countryId) {

        this.findCountryById(countryId);

        this.countryRepository
                .deleteCountryByid(countryId);

    }

    @Override
    public List<CountryResponseDTO> findAllActiveCountries() {

        List<Country> countries = this.countryRepository
                .findAllActiveCountries();

        List<CountryResponseDTO> countriesResponseDTO = countries
                .stream()
                .map(
                        (country) -> this.modelMapper
                                .map(
                                        country,
                                        CountryResponseDTO.class
                                )
                )
                .toList();

        return countriesResponseDTO;
    }
}
