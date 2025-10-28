package co.edu.unbosque.microservice_system_management.service.implementation;

import co.edu.unbosque.microservice_system_management.dto.request.BrokerCreateDTO;
import co.edu.unbosque.microservice_system_management.dto.request.BrokerUpdateDTO;
import co.edu.unbosque.microservice_system_management.dto.response.BrokerResponseDTO;
import co.edu.unbosque.microservice_system_management.exception.BrokerNotFoundException;
import co.edu.unbosque.microservice_system_management.model.entity.Broker;
import co.edu.unbosque.microservice_system_management.model.repository.IBrokerRepository;
import co.edu.unbosque.microservice_system_management.service.interfaces.IBrokerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BrokerServiceImplementation implements IBrokerService {

    private final IBrokerRepository brokerRepository;
    private final ModelMapper modelMapper;

    @Override
    public BrokerResponseDTO createBroker(BrokerCreateDTO brokerCreateDTO) {

        Broker broker = this.modelMapper
                .map(
                        brokerCreateDTO,
                        Broker.class
                );

        broker = this.brokerRepository
                .save(broker);

        BrokerResponseDTO brokerResponseDTO = this.modelMapper
                .map(
                        broker,
                        BrokerResponseDTO.class
                );

        return brokerResponseDTO;
    }

    @Override
    public BrokerResponseDTO findBrokerById(Integer brokerId) {

        Broker broker = this.brokerRepository
                .findById(brokerId)
                .orElseThrow(
                        () -> new BrokerNotFoundException("Broker with ID: " + brokerId + " does not exist.")
                );

        BrokerResponseDTO brokerResponseDTO = this.modelMapper
                .map(
                        broker,
                        BrokerResponseDTO.class
                );

        return brokerResponseDTO;
    }

    @Override
    public BrokerResponseDTO updateBrokerById(Integer brokerId, BrokerUpdateDTO brokerUpdateDTO) {

        this.findBrokerById(brokerId);

        Broker broker = this.modelMapper
                .map(
                        brokerUpdateDTO,
                        Broker.class
                );

        broker.setId(brokerId);

        broker = this.brokerRepository
                .save(broker);

        BrokerResponseDTO brokerResponseDTO = this.modelMapper
                .map(
                        broker,
                        BrokerResponseDTO.class
                );

        return brokerResponseDTO;
    }

    @Override
    public void deleteBrokerById(Integer brokerId) {

        this.findBrokerById(brokerId);

        this.brokerRepository
                .deleteBrokerById(brokerId);

    }

    @Override
    public List<BrokerResponseDTO> findAllActiveBrokers() {

        List<Broker> brokers = this.brokerRepository
                .findAllActiveBrokers();

        List<BrokerResponseDTO> brokersResponseDTO = brokers
                .stream()
                .map(
                        (broker) -> this.modelMapper
                                .map(
                                        broker,
                                        BrokerResponseDTO.class
                                )
                )
                .toList();

        return brokersResponseDTO;
    }

    @Override
    public List<BrokerResponseDTO> findAllActiveBrokersByCountry(Integer countryId) {

        List<Broker> brokers = this.brokerRepository
                .findAllActiveBrokersByCountryId(countryId);

        List<BrokerResponseDTO> brokersResponseDTO = brokers
                .stream()
                .map(
                        (broker) -> this.modelMapper
                                .map(
                                        broker,
                                        BrokerResponseDTO.class
                                )
                )
                .toList();

        return brokersResponseDTO;
    }

}
