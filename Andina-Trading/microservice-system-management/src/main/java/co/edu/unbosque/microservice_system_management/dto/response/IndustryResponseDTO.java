package co.edu.unbosque.microservice_system_management.dto.response;

import lombok.Data;

@Data
public class IndustryResponseDTO {

    private Integer id;
    private SectorResponseDTO sector;
    private String name;
    private boolean isActive;

}
