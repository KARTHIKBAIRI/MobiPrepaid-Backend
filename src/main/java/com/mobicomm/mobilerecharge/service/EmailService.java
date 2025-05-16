package com.mobicomm.mobilerecharge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class EmailService {
    private static final Logger logger = Logger.getLogger(EmailService.class.getName());

    @Autowired
    private JavaMailSender mailSender;

    public void sendConfirmationEmail(String to, String subject, String body) {
        int maxRetries = 3;
        int attempt = 1;
        while (attempt <= maxRetries) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(to);
                message.setSubject(subject);
                message.setText(body);
                message.setFrom("no-reply@mobicomm.com");
                mailSender.send(message);
                logger.info("Confirmation email sent to: " + to);
                return;
            } catch (MailException e) {
                logger.warning("Failed to send email to " + to + " on attempt " + attempt + ": " + e.getMessage());
                if (attempt == maxRetries) {
                    logger.severe("Max retries reached for email to " + to);
                    throw new RuntimeException("Failed to send email after " + maxRetries + " attempts: " + e.getMessage());
                }
                attempt++;
                try {
                    Thread.sleep(1000 * attempt);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Email retry interrupted: " + ie.getMessage());
                }
            }
        }
    }
}