package com.example.jobhunter.repository;

import com.example.jobhunter.domain.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;



public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {
    boolean existsByEmail(String email);

    Subscriber findByEmail(String email);
}
