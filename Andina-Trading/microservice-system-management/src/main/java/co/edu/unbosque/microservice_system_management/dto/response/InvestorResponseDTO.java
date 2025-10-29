package co.edu.unbosque.microservice_system_management.dto.response;

import co.edu.unbosque.microservice_system_management.model.enums.AccountStatus;
import co.edu.unbosque.microservice_system_management.model.enums.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InvestorResponseDTO {

    private Integer id;
    private String name;
    private String email;
    private Role role;
    private String phone;
    private AccountStatus accountStatus;
    private LocalDateTime createdAt;

}
