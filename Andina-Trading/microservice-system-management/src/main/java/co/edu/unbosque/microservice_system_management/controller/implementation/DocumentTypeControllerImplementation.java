package co.edu.unbosque.microservice_system_management.controller.implementation;

import co.edu.unbosque.microservice_system_management.controller.interfaces.IDocumentTypeController;
import co.edu.unbosque.microservice_system_management.dto.response.DocumentTypeResponseDTO;
import co.edu.unbosque.microservice_system_management.service.interfaces.IDocumentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class DocumentTypeControllerImplementation implements IDocumentTypeController {

    private final IDocumentTypeService documentTypeService;

    @Override
    public ResponseEntity<List<DocumentTypeResponseDTO>> findAllActiveDocumentTypes() {
        List<DocumentTypeResponseDTO> response = this.documentTypeService.findAllActiveDocumentTypes();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
