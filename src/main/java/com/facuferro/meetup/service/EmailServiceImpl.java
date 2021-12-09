package com.facuferro.meetup.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Validated
public class EmailServiceImpl implements  EmailService {

    private JavaMailSender sender;
    @Value("${meetup.mail.send.enable}")
    private boolean enableMail;

    public boolean sendEmailTool(String textMessage, String email, String subject) {
        if (!enableMail){
            log.info("EMail not sent. Email notification is disabled");
            return true;
        }else {
            return send(textMessage, email, subject);
        }
    }

    private boolean send(String textMessage, String email, String subject) {
        log.info("Sending email");
        boolean send = false;
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(email);
            helper.setText(textMessage, true);
            helper.setSubject(subject);
            sender.send(message);
            send = true;
            log.info("Email was sending");
        } catch (MessagingException e) {
            log.error("Error : {}", e);
        }
        return send;
    }
}
