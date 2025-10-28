package co.edu.unbosque.microservice_system_management.dto.response;

import co.edu.unbosque.microservice_system_management.model.entity.DocumentType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BrokerResponseDTO {

    private Integer id;
    private String firstName;
    private String middleName;
    private String lastName;
    private DocumentType documentType;
    private String documentNumber;
    private String email;
    private String phone;
    private LocalDateTime createAt;
    private LocalDateTime updatedAt;

}
