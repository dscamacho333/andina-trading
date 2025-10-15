package co.edu.unbosque.microservice_system_management.dto.response;

import lombok.*;

@Data
public class CityResponseDTO {

    private Integer id;
    private String name;
    private CountryResponseDTO country;
    private EconomySituationResponseDTO economySituation;

}
