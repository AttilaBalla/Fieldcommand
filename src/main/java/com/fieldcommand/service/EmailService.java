package com.fieldcommand.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private JavaMailSender javaMailSender;

    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("$(spring.mail.username)")
    private String MESSAGE_FROM;

    public void sendMessage(String email, String activationCode) {
        SimpleMailMessage message = null;

        try {
            message = new SimpleMailMessage();
            message.setFrom(MESSAGE_FROM);
            message.setTo(email);
            message.setSubject("A new Fieldcommand account has been created for you");
            message.setText("Hi there! An account has been created for You on Fieldcommand. \n\n" +
                            "Follow this link to get started: [link to be added here]");

            javaMailSender.send(message);


        } catch (Exception ex) {
            logger.error("Failed to send e-mail to {}, reason: {}", email, ex);
        }

    }
}
