package co.edu.unbosque.microservice_system_management.dto.request;

import co.edu.unbosque.microservice_system_management.dto.response.CountryResponseDTO;
import co.edu.unbosque.microservice_system_management.dto.response.IndustryResponseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class IssuerUpdateDTO {

    @NotBlank(message = "Issuer name must not be blank")
    @Size(
            message = "Issuer name must be between 3-120 characters long",
            min = 3,
            max = 120
    )
    private String name;

    @NotBlank(message = "Issuer ticker must not be blank")
    @Size(
            message = "Issuer ticker must be between 2-10 characters long",
            min = 2,
            max = 10
    )
    private String ticker;

    @NotNull(message = "Issuer country must not be null")
    private CountryResponseDTO country;

    @NotNull(message = "Issuer industry must not be null")
    private IndustryResponseDTO industry;

    @NotBlank(message = "Issuer website must not be blank")
    @Size(
            message = "Issuer website must be between 1 - 10 characters long",
            min = 1,
            max = 50
    )
    private String website;

    @NotBlank(message = "Issuer notes must not be blank")
    @Size(
            message = "Issuer notes must be between 2 - 500 characters long",
            min = 2,
            max = 500
    )
    private String notes;

}
