package co.edu.unbosque.microservice_system_management.controller.interfaces;

import co.edu.unbosque.microservice_system_management.dto.request.BrokerCreateDTO;
import co.edu.unbosque.microservice_system_management.dto.request.BrokerUpdateDTO;
import co.edu.unbosque.microservice_system_management.dto.response.BrokerResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/broker")
public interface IBrokerController {

    @PostMapping
    ResponseEntity<BrokerResponseDTO> createBroker(
            @RequestBody BrokerCreateDTO brokerCreateDTO
    );

    @GetMapping("/{brokerId}")
    ResponseEntity<BrokerResponseDTO> findBrokerById(
            @PathVariable Integer brokerId
    );

    @PutMapping("/{brokerId}")
    ResponseEntity<BrokerResponseDTO> updateBrokerById(
            @PathVariable Integer brokerId,
            @RequestBody BrokerUpdateDTO brokerUpdateDTO
    );

    @DeleteMapping("/{brokerId}")
    ResponseEntity<Void> deleteBrokerById(
            @PathVariable Integer brokerId
    );

    @GetMapping
    ResponseEntity<List<BrokerResponseDTO>> findAllActiveBrokers();

    @GetMapping("/country/{countryId}")
    ResponseEntity<List<BrokerResponseDTO>> findAllActiveBrokersByCountry(
            @PathVariable Integer countryId
    );
}

