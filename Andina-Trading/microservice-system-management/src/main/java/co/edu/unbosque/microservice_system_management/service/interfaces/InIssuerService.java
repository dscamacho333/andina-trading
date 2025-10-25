package co.edu.unbosque.microservice_system_management.service.interfaces;

import co.edu.unbosque.microservice_system_management.dto.request.IssuerCreateDTO;
import co.edu.unbosque.microservice_system_management.dto.request.IssuerUpdateDTO;
import co.edu.unbosque.microservice_system_management.dto.response.IssuerResponseDTO;

import java.util.List;

public interface InIssuerService {


    IssuerResponseDTO createIssuer(
            IssuerCreateDTO issuerCreateDTO
    );

    IssuerResponseDTO findIssuerById(
            Integer issuerId
    );

    IssuerResponseDTO updateIssuerById(
            Integer issuerId,
            IssuerUpdateDTO issuerUpdateDTO
    );

    void deleteIssuerById(
            Integer issuerId
    );

    List<IssuerResponseDTO> findAllActiveIssuers();

    List<IssuerResponseDTO> findAllIssuersByCountry(
            Integer countryId
    );

    List<IssuerResponseDTO> findAllIssuersByIndustry(
            Integer industryId
    );

    List<IssuerResponseDTO> findAllIssuersBySector(
            Integer sectorId
    );

}
