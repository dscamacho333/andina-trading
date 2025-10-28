package co.edu.unbosque.microservice_system_management.controller.implementation;

import co.edu.unbosque.microservice_system_management.controller.interfaces.IBrokerController;
import co.edu.unbosque.microservice_system_management.dto.request.BrokerCreateDTO;
import co.edu.unbosque.microservice_system_management.dto.request.BrokerUpdateDTO;
import co.edu.unbosque.microservice_system_management.dto.response.BrokerResponseDTO;
import co.edu.unbosque.microservice_system_management.service.interfaces.IBrokerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BrokerControllerImplementation implements IBrokerController {

    private final IBrokerService brokerService;

    @Override
    public ResponseEntity<BrokerResponseDTO> createBroker(BrokerCreateDTO brokerCreateDTO) {
        BrokerResponseDTO response = this.brokerService.createBroker(brokerCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<BrokerResponseDTO> findBrokerById(Integer brokerId) {
        BrokerResponseDTO response = this.brokerService.findBrokerById(brokerId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<BrokerResponseDTO> updateBrokerById(Integer brokerId, BrokerUpdateDTO brokerUpdateDTO) {
        BrokerResponseDTO response = this.brokerService.updateBrokerById(brokerId, brokerUpdateDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @Override
    public ResponseEntity<Void> deleteBrokerById(Integer brokerId) {
        this.brokerService.deleteBrokerById(brokerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<List<BrokerResponseDTO>> findAllActiveBrokers() {
        List<BrokerResponseDTO> response = this.brokerService.findAllActiveBrokers();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<List<BrokerResponseDTO>> findAllActiveBrokersByCountry(Integer countryId) {
        List<BrokerResponseDTO> response = this.brokerService.findAllActiveBrokersByCountry(countryId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

