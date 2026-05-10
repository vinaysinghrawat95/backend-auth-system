package com.vinay.auth_system.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String toEmail, String token){
        String link = "https://disabled-minnow-gharbazaar1-4c55152c.koyeb.app/api/auth/verify?token=" + token;

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(toEmail);
        mail.setSubject("Verify your email");
        mail.setText("Click the link to verify your account: \n\n"+link);

        mailSender.send(mail);
    }
}
