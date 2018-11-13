package org.thingsboard.rule.engine.api;

import org.thingsboard.server.common.data.exception.ThingsboardException;

import com.fasterxml.jackson.databind.JsonNode;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public interface MailService {

    void updateMailConfiguration();

    void sendEmail(String email, String subject, String message) throws ThingsboardException;
    
    void sendTestMail(JsonNode config, String email) throws ThingsboardException;
    
    void sendActivationEmail(String activationLink, String email) throws ThingsboardException;
    
    void sendAccountActivatedEmail(String loginLink, String email) throws ThingsboardException;
    
    void sendResetPasswordEmail(String passwordResetLink, String email) throws ThingsboardException;
    
    void sendPasswordWasResetEmail(String loginLink, String email) throws ThingsboardException;

    void send(String from, String to, String cc, String bcc, String subject, String body) throws MessagingException;
}
