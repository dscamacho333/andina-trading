package co.edu.unbosque.microservice_system_management.service.interfaces;

import co.edu.unbosque.microservice_system_management.dto.response.DocumentTypeResponseDTO;

import java.util.List;

public interface IDocumentTypeService {

    List<DocumentTypeResponseDTO> findAllActiveDocumentTypes();

}
