package co.edu.unbosque.microservice_system_management.service.implementation;

import co.edu.unbosque.microservice_system_management.dto.response.InvestorResponseDTO;
import co.edu.unbosque.microservice_system_management.exception.InvestorNotFoundException;
import co.edu.unbosque.microservice_system_management.model.entity.Investor;
import co.edu.unbosque.microservice_system_management.model.repository.InInvestorRepository;
import co.edu.unbosque.microservice_system_management.service.interfaces.InInvestorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class InvestorServiceImplementation implements InInvestorService {

    private final InInvestorRepository investorRepository;
    private final ModelMapper modelMapper;

    @Override
    public InvestorResponseDTO findInvestorById(Integer investorId) {

        Investor investor = this.investorRepository
                .findById(investorId)
                .orElseThrow(
                        () -> new InvestorNotFoundException("Investor with ID: " + investorId + " does not exist.")
                );

        InvestorResponseDTO response = this.modelMapper
                .map(
                        investor,
                        InvestorResponseDTO.class
                );
        response.setRole(investor.getRoleUser());

        log.info("Inversionista con ID: {} fue encontrando", investorId, response);

        return response;
    }

    @Override
    public void deleteInvestorById(Integer investorId) {

        this.findInvestorById(investorId);

        this.investorRepository
                .deleteInvestorById(investorId);

        log.info("Inversionista con ID: {} fue eliminado", investorId);
    }

    @Override
    public List<InvestorResponseDTO> findAllInvestorsByAccountStatus(String accountStatus) {

        List<Investor> investors = this.investorRepository
                .findAllInvestorsByAccountStatus(accountStatus);

        List<InvestorResponseDTO> response = investors
                .stream()
                .map(inv -> {
                    InvestorResponseDTO dto = this.modelMapper.map(inv, InvestorResponseDTO.class);
                    dto.setRole(inv.getRoleUser());
                    return dto;
                })
                .toList();

        log.info("Listando todos los inversionistas", response);

        return response;
    }

}
