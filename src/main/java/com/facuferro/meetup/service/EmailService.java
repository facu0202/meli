package com.facuferro.meetup.service;

public interface EmailService {
    boolean sendEmailTool(String textMessage, String email, String subject);
}
