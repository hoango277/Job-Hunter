package com.example.jobhunter.service;

public interface EmailService {
    void sendEmailSync(String to, String subject, String templateName, boolean isMultipart, boolean isHtml);

    void sendEmailFromTemplateSync(String to, String subject, String templateName ,String username, Object value);
}
