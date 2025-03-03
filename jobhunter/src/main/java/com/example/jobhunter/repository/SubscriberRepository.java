package com.example.jobhunter.repository;

import com.example.jobhunter.domain.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SubscriberRepository extends JpaRepository<Subscriber, Long> , JpaSpecificationExecutor<Subscriber> {
    Optional<Subscriber> findByEmail(String email);
}
