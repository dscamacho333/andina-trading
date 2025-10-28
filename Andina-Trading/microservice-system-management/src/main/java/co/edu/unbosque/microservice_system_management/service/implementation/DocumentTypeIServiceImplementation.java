package co.edu.unbosque.microservice_system_management.service.implementation;

import co.edu.unbosque.microservice_system_management.dto.response.DocumentTypeResponseDTO;
import co.edu.unbosque.microservice_system_management.model.entity.DocumentType;
import co.edu.unbosque.microservice_system_management.model.repository.IDocumentTypeRepository;
import co.edu.unbosque.microservice_system_management.service.interfaces.IDocumentTypeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DocumentTypeIServiceImplementation implements IDocumentTypeService {

    private final IDocumentTypeRepository documentTypeRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<DocumentTypeResponseDTO> findAllActiveDocumentTypes() {

        List<DocumentType> documentTypes = this.documentTypeRepository
                .findAllActiveDocumentTypes();

        List<DocumentTypeResponseDTO> response = documentTypes
                .stream()
                .map(dt -> this.modelMapper.map(dt, DocumentTypeResponseDTO.class))
                .toList();

        return response;
    }

}
