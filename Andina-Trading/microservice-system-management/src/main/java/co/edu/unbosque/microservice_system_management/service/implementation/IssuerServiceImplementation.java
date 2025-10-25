package co.edu.unbosque.microservice_system_management.service.implementation;

import co.edu.unbosque.microservice_system_management.dto.request.IssuerCreateDTO;
import co.edu.unbosque.microservice_system_management.dto.request.IssuerUpdateDTO;
import co.edu.unbosque.microservice_system_management.dto.response.IssuerResponseDTO;
import co.edu.unbosque.microservice_system_management.exception.IssuerNotFoundException;
import co.edu.unbosque.microservice_system_management.model.entity.Issuer;
import co.edu.unbosque.microservice_system_management.model.repository.InIssuerRepository;
import co.edu.unbosque.microservice_system_management.service.interfaces.InIssuerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class IssuerServiceImplementation implements InIssuerService {

    private final InIssuerRepository issuerRepository;
    private final ModelMapper modelMapper;

    @Override
    public IssuerResponseDTO createIssuer(IssuerCreateDTO issuerCreateDTO) {

        Issuer issuer = this.modelMapper
                .map(
                        issuerCreateDTO,
                        Issuer.class
                );

        issuer = this.issuerRepository
                .save(issuer);

        IssuerResponseDTO issuerResponseDTO = this.modelMapper
                .map(
                        issuer,
                        IssuerResponseDTO.class
                );

        return issuerResponseDTO;
    }

    @Override
    public IssuerResponseDTO findIssuerById(Integer issuerId) {

        Issuer issuer = this.issuerRepository
                .findById(issuerId)
                .orElseThrow(
                        () -> new IssuerNotFoundException("Issuer with ID: " + issuerId + " does not exist.")
                );

        IssuerResponseDTO issuerResponseDTO = this.modelMapper
                .map(
                        issuer,
                        IssuerResponseDTO.class
                );

        return issuerResponseDTO;
    }

    @Override
    public IssuerResponseDTO updateIssuerById(Integer issuerId, IssuerUpdateDTO issuerUpdateDTO) {

        this.findIssuerById(issuerId);

        Issuer issuer = this.modelMapper
                .map(
                        issuerUpdateDTO,
                        Issuer.class
                );

        issuer.setId(issuerId);

        issuer = this.issuerRepository
                .save(issuer);

        IssuerResponseDTO issuerResponseDTO = this.modelMapper
                .map(
                        issuer,
                        IssuerResponseDTO.class
                );

        return issuerResponseDTO;
    }

    @Override
    public void deleteIssuerById(Integer issuerId) {

        this.findIssuerById(issuerId);

        this.issuerRepository
                .deleteIssuerById(issuerId);

    }

    @Override
    public List<IssuerResponseDTO> findAllActiveIssuers() {

        List<Issuer> issuers = this.issuerRepository
                .findAllActiveIssuers();

        List<IssuerResponseDTO> issuersResponseDTO = issuers
                .stream()
                .map(
                        (issuer) -> this.modelMapper
                                .map(
                                        issuer,
                                        IssuerResponseDTO.class
                                )
                )
                .toList();

        return issuersResponseDTO;
    }

    @Override
    public List<IssuerResponseDTO> findAllIssuersByCountry(Integer countryId) {

        List<Issuer> issuers = this.issuerRepository
                .findAllIssuersByCountry(countryId);

        List<IssuerResponseDTO> issuersResponseDTO = issuers
                .stream()
                .map(
                        (issuer) -> this.modelMapper
                                .map(
                                        issuer,
                                        IssuerResponseDTO.class
                                )
                )
                .toList();

        return issuersResponseDTO;

    }

    @Override
    public List<IssuerResponseDTO> findAllIssuersByIndustry(Integer industryId) {

        List<Issuer> issuers = this.issuerRepository
                .findAllIssuersByIndustry(industryId);

        List<IssuerResponseDTO> issuersResponseDTO = issuers
                .stream()
                .map(
                        (issuer) -> this.modelMapper
                                .map(
                                        issuer,
                                        IssuerResponseDTO.class
                                )
                )
                .toList();

        return issuersResponseDTO;

    }

    @Override
    public List<IssuerResponseDTO> findAllIssuersBySector(Integer sectorId) {

        List<Issuer> issuers = this.issuerRepository
                .findAllIssuersBySector(sectorId);

        List<IssuerResponseDTO> issuersResponseDTO = issuers
                .stream()
                .map(
                        (issuer) -> this.modelMapper
                                .map(
                                        issuer,
                                        IssuerResponseDTO.class
                                )
                )
                .toList();

        return issuersResponseDTO;

    }
}
