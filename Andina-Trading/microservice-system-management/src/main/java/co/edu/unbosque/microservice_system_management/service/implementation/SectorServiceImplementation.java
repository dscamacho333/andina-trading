package co.edu.unbosque.microservice_system_management.service.implementation;

import co.edu.unbosque.microservice_system_management.dto.response.SectorResponseDTO;
import co.edu.unbosque.microservice_system_management.model.entity.Sector;
import co.edu.unbosque.microservice_system_management.model.repository.ISectorRepository;
import co.edu.unbosque.microservice_system_management.service.interfaces.ISectorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SectorServiceImplementation implements ISectorService {

    private final ISectorRepository sectorRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<SectorResponseDTO> findAllActiveSectors() {

        List<Sector> sectors = this.sectorRepository
                .findAllActiveSectors();

        List<SectorResponseDTO> sectorsResponseDTO = sectors
                .stream()
                .map(
                        (sector) -> this.modelMapper
                                .map(
                                        sector,
                                        SectorResponseDTO.class
                                )
                )
                .toList();

        return sectorsResponseDTO;
    }
}
