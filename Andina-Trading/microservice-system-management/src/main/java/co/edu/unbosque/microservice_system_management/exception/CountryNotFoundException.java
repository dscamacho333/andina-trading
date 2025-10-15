package co.edu.unbosque.microservice_system_management.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(
        callSuper = true
)
@Data
public class CountryNotFoundException extends RuntimeException{

    private final String message;

}
