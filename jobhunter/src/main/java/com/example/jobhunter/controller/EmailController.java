package com.example.jobhunter.controller;


import com.example.jobhunter.service.EmailService;
import com.example.jobhunter.service.SubscriberService;
import com.example.jobhunter.utils.annotation.APIMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.version}")
public class EmailController {
    private final EmailService emailService;
    private final SubscriberService subscriberService;

    public EmailController(EmailService emailService, SubscriberService subscriberService) {
        this.emailService = emailService;
        this.subscriberService = subscriberService;
    }

    @GetMapping("/emails")
    @APIMessage("send simple email")
    public ResponseEntity<Void> sendSimpleEmail() {
        subscriberService.sendSubscribersEmailJobs();

        return ResponseEntity.ok().body(null);
    }
}
