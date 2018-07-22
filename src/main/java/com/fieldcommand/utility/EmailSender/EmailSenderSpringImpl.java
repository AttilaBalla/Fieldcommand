package com.fieldcommand.utility.EmailSender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Qualifier("springMail")
public class EmailSenderSpringImpl implements EmailSender {

    @Value("${frontend.address}")
    private String frontendAddress;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private JavaMailSender javaMailSender;

    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMessage(String email, String activationCode) {
        SimpleMailMessage message;

        message = new SimpleMailMessage();
        message.setFrom("Fieldcommand");
        message.setTo(email);
        message.setSubject("A new Fieldcommand account has been created for you");
        message.setText("Hi there! An account has been created for You on Fieldcommand. \n\n" +
                "Follow this link to get started: " + frontendAddress + "/activate/" + activationCode + "\n\n" +
                "Afterwards, you can login here:" + frontendAddress + "/login/");

        javaMailSender.send(message);
    }
}


