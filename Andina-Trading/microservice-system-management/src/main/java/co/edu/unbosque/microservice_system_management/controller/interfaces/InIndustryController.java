package co.edu.unbosque.microservice_system_management.controller.interfaces;

import co.edu.unbosque.microservice_system_management.dto.response.IndustryResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/v1/industry")
public interface InIndustryController {

    @GetMapping
    ResponseEntity<List<IndustryResponseDTO>> findAllActiveIndustries();


}
