package com.example.jobhunter.controller;


import com.example.jobhunter.domain.Subscriber;
import com.example.jobhunter.service.SubscriberService;
import com.example.jobhunter.utils.annotation.APIMessage;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.version}")
public class SubscriberController {
    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribers")
    @APIMessage("create a subscriber")
    public ResponseEntity<Subscriber> createSubscriber(@Valid @RequestBody Subscriber subscriber) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subscriberService.create(subscriber));
    }

    @PutMapping("/subscribers")
    @APIMessage("update a subscriber")
    public ResponseEntity<Subscriber> update(@RequestBody Subscriber subscriber) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subscriberService.update(subscriber));
    }


}
