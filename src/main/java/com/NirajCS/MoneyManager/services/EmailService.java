
package com.NirajCS.MoneyManager.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    
    private String fromEmail = "nirajyadav9136@gmail.com";

    private final JavaMailSender mailSender;

    public void sendEmail(String toEmail, String subject, String body) {
       try {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        
       } catch (Exception e) {
        throw new RuntimeException( e.getMessage());
       }
    } 
}
