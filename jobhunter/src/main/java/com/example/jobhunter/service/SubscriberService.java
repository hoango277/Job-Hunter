package com.example.jobhunter.service;

import com.example.jobhunter.domain.Subscriber;
import jakarta.validation.Valid;

public interface SubscriberService {
    Subscriber create(@Valid Subscriber subscriber);

    Subscriber update(@Valid Subscriber subscriber);
    void sendSubscribersEmailJobs();
}
