package co.edu.unbosque.microservice_system_management.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CountryUpdateDTO {

    @NotBlank(message = "Country Code Must not be blank")
    @Size(
            min = 2,
            max = 2,
            message = "Country Code Must be only 2 characters long"
    )
    @Pattern(
            regexp = "^[A-Za-z]{2}$",
            message = "Country Code Must contain letter only"
    )
    private String code;

    @NotBlank(message = "Country name Must not be blank")
    @Size(
            min = 3,
            max = 50,
            message = "Country Code Must be only 2 characters long"
    )
    @Pattern(
            regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]{3,50}$",
            message = "Country Name Must contain letter only"
    )
    private String name;

}
