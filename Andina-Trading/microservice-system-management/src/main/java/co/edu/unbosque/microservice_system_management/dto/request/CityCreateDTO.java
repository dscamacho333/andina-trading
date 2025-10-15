package co.edu.unbosque.microservice_system_management.dto.request;

import co.edu.unbosque.microservice_system_management.dto.response.CountryResponseDTO;
import co.edu.unbosque.microservice_system_management.dto.response.EconomySituationResponseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CityCreateDTO {

    @NotBlank(message = "City Name Must not be blank")
    private String name;

    @NotNull(message = "Country Must not be null")
    private CountryResponseDTO country;

    @NotNull(message = "Economy Situation Must not be null")
    private EconomySituationResponseDTO economySituation;

}
