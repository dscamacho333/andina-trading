package co.edu.unbosque.microservice_system_management.dto.response;

import lombok.Data;

@Data
public class DocumentTypeResponseDTO {

    private Integer id;

    private String code;

    private String name;

    private CountryResponseDTO country;

}
