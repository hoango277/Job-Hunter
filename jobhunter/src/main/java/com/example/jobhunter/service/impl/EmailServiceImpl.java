package com.example.jobhunter.service.impl;

import com.example.jobhunter.domain.Job;
import com.example.jobhunter.domain.Skill;
import com.example.jobhunter.domain.Subscriber;
import com.example.jobhunter.repository.JobRepository;
import com.example.jobhunter.repository.SubscriberRepository;
import com.example.jobhunter.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.List;


@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final JobRepository jobRepository;
    private final SubscriberRepository subscriberRepository;

    public EmailServiceImpl (SpringTemplateEngine templateEngine, JavaMailSender javaMailSender, JobRepository jobRepository, SubscriberRepository subscriberRepository) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
        this.jobRepository = jobRepository;
        this.subscriberRepository = subscriberRepository;
    }



    @Override
    public void sendEmailSync(String to, String subject, String content, boolean isMultipart,
                              boolean isHtml) {
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
                    isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content, isHtml);
            this.javaMailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            System.out.println("ERROR SEND EMAIL: " + e);
        }
    }



    @Override
    public void sendEmailFromTemplateSync(String to, String subject, String templateName, String username, Object value) {
        Context context = new Context();
        context.setVariable("name", username);
        context.setVariable("jobs", value);
        String content = templateEngine.process(templateName, context);
        this.sendEmailSync(to, subject, content, false, true);
    }




}
