package co.edu.unbosque.microservice_order.client;

import co.edu.unbosque.microservice_order.model.dto.MailRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "msvc-contract")
public interface MailClient {

    @PostMapping("/api/email")
    void send(@RequestBody MailRequestDTO mail);

}