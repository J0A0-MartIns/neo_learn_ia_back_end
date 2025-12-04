package neo_learn_ia_api.Neo.Learn.Ia.API.service;

public interface EmailService {

    void sendConfirmationEmail(String toEmail, String token);

    void sendPasswordResetEmail(String toEmail, String token);
}