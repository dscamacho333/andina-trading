package co.edu.unbosque.microservice_contract.controller;

import co.edu.unbosque.microservice_contract.model.dto.MailRequestDTO;
import co.edu.unbosque.microservice_contract.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final MailService mailService;

    @Autowired
    public EmailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/api/email")
    void send(@RequestBody MailRequestDTO mail){
        mailService.sendEmaill(mail.getTo(), mail.getSubject(), mail.getBody());
    };

}
