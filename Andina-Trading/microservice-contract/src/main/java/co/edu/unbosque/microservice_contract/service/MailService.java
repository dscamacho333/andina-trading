package co.edu.unbosque.microservice_contract.service;


import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmaill(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }


    public void sendOrderNotification(String to, String subject, String text) {
        try {
            System.out.println("📩 Enviando correo a: "+ to);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setText(text, false);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("tu_correo@gmail.com"); // debe coincidir con spring.mail.username
            mailSender.send(message);
            System.out.println(" Correo enviado exitosamente");
        } catch (Exception e) {
            System.out.println(" Error al enviar correo: {}"+ e.getMessage());
        }
    }

}
