package co.edu.unbosque.microservice_system_management.service.implementation;

import co.edu.unbosque.microservice_system_management.dto.response.IndustryResponseDTO;
import co.edu.unbosque.microservice_system_management.model.entity.Industry;
import co.edu.unbosque.microservice_system_management.model.repository.InIndustryRepository;
import co.edu.unbosque.microservice_system_management.service.interfaces.InIndustryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class IndustryServiceImplementation implements InIndustryService {

    private final InIndustryRepository industryRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<IndustryResponseDTO> findAllActiveIndustries() {

        List<Industry> industries = this.industryRepository
                .findAllActiveIndustries();

        List<IndustryResponseDTO> industriesResponseDTO = industries
                .stream()
                .map(
                        (industry) -> this.modelMapper
                                .map(
                                        industry,
                                        IndustryResponseDTO.class
                                )
                )
                .toList();

        return industriesResponseDTO;
    }
}
