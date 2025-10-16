package co.edu.unbosque.microservice_system_management.service.implementation;

import co.edu.unbosque.microservice_system_management.dto.response.EconomySituationResponseDTO;
import co.edu.unbosque.microservice_system_management.model.entity.EconomySituation;
import co.edu.unbosque.microservice_system_management.model.repository.IEconomySituationRepository;
import co.edu.unbosque.microservice_system_management.service.interfaces.IEconomySituationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EconomySituationServiceImplementation implements IEconomySituationService {

    private final IEconomySituationRepository economySituationRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<EconomySituationResponseDTO> findAllActiveEconomySituations() {

        List<EconomySituation> economySituations = this.economySituationRepository
                .findAllActiveEconomySituations();

        List<EconomySituationResponseDTO> economySituationsResponseDTO = economySituations
                .stream()
                .map(
                        (economySituation) -> this.modelMapper
                                .map(
                                        economySituation,
                                        EconomySituationResponseDTO.class
                                )
                )
                .toList();

        return economySituationsResponseDTO;
    }
}
