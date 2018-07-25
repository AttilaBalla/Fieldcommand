package com.fieldcommand.utility.EmailSender;

import org.springframework.mail.MailSendException;

public interface EmailSender {

    void sendMessage(String email, String activationCode) throws MailSendException;

}
