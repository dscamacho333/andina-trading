package co.edu.unbosque.microservice_system_management.service.interfaces;

import co.edu.unbosque.microservice_system_management.dto.request.BrokerCreateDTO;
import co.edu.unbosque.microservice_system_management.dto.request.BrokerUpdateDTO;
import co.edu.unbosque.microservice_system_management.dto.response.BrokerResponseDTO;

import java.util.List;

public interface IBrokerService {

    BrokerResponseDTO createBroker(
            BrokerCreateDTO brokerCreateDTO
    );

    BrokerResponseDTO findBrokerById(
            Integer brokerId
    );

    BrokerResponseDTO updateBrokerById(
            Integer brokerId,
            BrokerUpdateDTO brokerUpdateDTO
    );

    void deleteBrokerById(
            Integer brokerId
    );

    List<BrokerResponseDTO> findAllActiveBrokers();

    List<BrokerResponseDTO> findAllActiveBrokersByCountry(
            Integer countryId
    );

}
