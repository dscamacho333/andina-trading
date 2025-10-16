package co.edu.unbosque.microservice_system_management.dto.request;

import co.edu.unbosque.microservice_system_management.dto.response.CountryResponseDTO;
import co.edu.unbosque.microservice_system_management.dto.response.EconomySituationResponseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CityCreateDTO {

    @NotBlank(message = "City Name Must not be blank")
    @Size(
            min = 3,
            max = 25,
            message = "City Name Must be between 3-25 character long"
    )
    @Pattern(
            regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]{3,25}$",
            message = "City Name must contain only letters and spaces (including accents and ñ)"
    )
    private String name;

    @NotNull(message = "Country Must not be null")
    private CountryResponseDTO country;

    @NotNull(message = "Economy Situation Must not be null")
    private EconomySituationResponseDTO economySituation;

}
