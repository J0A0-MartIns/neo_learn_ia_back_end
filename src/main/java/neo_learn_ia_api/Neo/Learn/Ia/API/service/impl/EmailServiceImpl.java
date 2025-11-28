package neo_learn_ia_api.Neo.Learn.Ia.API.service.impl;

import neo_learn_ia_api.Neo.Learn.Ia.API.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${application.mail.sender}")
    private String senderEmail;

    private final String FRONTEND_URL = "http://localhost:4200";

    @Override
    @Async
    public void sendConfirmationEmail(String toEmail, String token) {
        String subject = "Confirmação de Conta - NeoLearnIA";
        String link = FRONTEND_URL + "/confirmar-email?token=" + token;
        String body = "Olá! Clique no link a seguir para confirmar seu email e ativar sua conta:\n" + link;

        sendEmail(toEmail, subject, body);
    }
    @Override
    @Async
    public void sendPasswordResetEmail(String toEmail, String token) {
        String subject = "Recuperação de Senha - NeoLearnIA";
        String link = FRONTEND_URL + "/recuperar-senha?token=" + token;
        String body = "Você solicitou a recuperação de senha. Clique no link para criar uma nova senha:\n" + link;

        sendEmail(toEmail, subject, body);
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}