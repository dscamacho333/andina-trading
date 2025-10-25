package co.edu.unbosque.microservice_system_management.dto.response;

import co.edu.unbosque.microservice_system_management.model.entity.Country;
import co.edu.unbosque.microservice_system_management.model.entity.Industry;
import lombok.Data;

@Data
public class IssuerResponseDTO {

    private Integer id;
    private String name;
    private String ticker;
    private Country country;
    private Industry industry;
    private String website;
    private String notes;

}
