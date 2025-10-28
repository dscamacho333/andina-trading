package co.edu.unbosque.microservice_system_management.dto.request;

import co.edu.unbosque.microservice_system_management.dto.response.DocumentTypeResponseDTO;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BrokerCreateDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no debe exceder 50 caracteres")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "El nombre contiene caracteres inválidos")
    private String firstName;

    @Size(max = 50, message = "El segundo nombre no debe exceder 50 caracteres")
    @Pattern(regexp = "^[\\p{L} .'-]*$", message = "El segundo nombre contiene caracteres inválidos")
    private String middleName;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50, message = "El apellido no debe exceder 50 caracteres")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "El apellido contiene caracteres inválidos")
    private String lastName;


    @NotNull(message = "El tipo de documento es obligatorio")
    private DocumentTypeResponseDTO documentType;


    @NotBlank(message = "El número de documento es obligatorio")
    @Size(max = 30, message = "El número de documento no debe exceder 30 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9\\-./]+$", message = "El número de documento solo puede contener letras, números y - . /")
    private String documentNumber;

    @NotBlank(message = "El email es obligatorio")
    @Size(max = 120, message = "El email no debe exceder 120 caracteres")
    @Email(message = "El email no tiene un formato válido")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 15, message = "El teléfono no debe exceder 15 caracteres")
    @Pattern(regexp = "^[0-9+()\\-\\s]+$", message = "El teléfono solo puede contener dígitos, espacios y símbolos + ( ) -")
    private String phone;


}
