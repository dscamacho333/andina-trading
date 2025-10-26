package co.edu.unbosque.microservice_order.client;

import co.edu.unbosque.microservice_order.model.dto.MailRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "msvc-notification")
public interface MailClient {

    @PostMapping("/email")
    void send(@RequestBody MailRequestDTO mail);

}