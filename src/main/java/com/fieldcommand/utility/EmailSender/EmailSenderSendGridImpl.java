package com.fieldcommand.utility.EmailSender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import com.sendgrid.*;
import org.springframework.stereotype.Service;


import java.io.IOException;

@Service
@Qualifier("sendGrid")
public class EmailSenderSendGridImpl implements EmailSender {

    @Value("${frontend.address}")
    private String frontendAddress;

    @Value("${sendgrid.apikey}")
    private String apiKey;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Email from = new Email("Fieldcommand");

    public void sendMessage(String email, String activationCode) throws MailException {

        String subject = "A new Fieldcommand account has been created for You!";
        Email to = new Email(email);

        Content content = new Content("text/plain", "Hi there! An account has been created for You on Fieldcommand. \n\n" +
                        "Follow this link to get started: "+ frontendAddress + "/activate/" + activationCode);

        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();

        SendGrid sendGrid = new SendGrid(apiKey);

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch(IOException ex) {

            throw new MailSendException(ex.getMessage());
        }
    }
}
