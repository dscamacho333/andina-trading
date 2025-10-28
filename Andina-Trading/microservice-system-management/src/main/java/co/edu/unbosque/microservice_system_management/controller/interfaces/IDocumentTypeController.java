package co.edu.unbosque.microservice_system_management.controller.interfaces;

import co.edu.unbosque.microservice_system_management.dto.response.DocumentTypeResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/document-type")
public interface IDocumentTypeController {

    @GetMapping
    ResponseEntity<List<DocumentTypeResponseDTO>> findAllActiveDocumentTypes();

}
