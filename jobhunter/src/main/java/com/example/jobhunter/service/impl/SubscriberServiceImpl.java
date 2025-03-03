package com.example.jobhunter.service.impl;

import com.example.jobhunter.domain.DTO.request.email.EmailSend;
import com.example.jobhunter.domain.Job;
import com.example.jobhunter.domain.Skill;
import com.example.jobhunter.domain.Subscriber;
import com.example.jobhunter.repository.JobRepository;
import com.example.jobhunter.repository.SkillRepository;
import com.example.jobhunter.repository.SubscriberRepository;
import com.example.jobhunter.service.EmailService;
import com.example.jobhunter.service.SubscriberService;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriberServiceImpl implements SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;
    private final JobRepository jobRepository;
    private final EmailService emailService;
    private final KafkaTemplate<String, EmailSend> kafkaTemplate;

    public SubscriberServiceImpl(SubscriberRepository subscriberRepository, SkillRepository skillRepository, JobRepository jobRepository, EmailService emailService, KafkaTemplate<String, EmailSend> kafkaTemplate) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
        this.emailService = emailService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Subscriber create(Subscriber subscriber) {
         Subscriber subscriberCheck = subscriberRepository.findByEmail(subscriber.getEmail())
                 .orElse(null);
         if (subscriberCheck != null) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
         }
         if(subscriber.getSkills() != null) {
             List<Long> skills = subscriber.getSkills()
                     .stream().map(item -> item.getId()).collect(Collectors.toList());
             List<Skill> skillList = skillRepository.findByIdIn(skills);
             subscriber.setSkills(skillList);
         }
         return subscriberRepository.save(subscriber);

    }

    @Override
    public Subscriber update(Subscriber subscriber) {
        Subscriber subscriberDB = subscriberRepository.findById(subscriber.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subscriber not found"));
        if(subscriber.getSkills() != null) {
            List<Long> skills = subscriber.getSkills()
                    .stream().map(item -> item.getId()).collect(Collectors.toList());
            List<Skill> skillList = skillRepository.findByIdIn(skills);
            subscriberDB.setSkills(skillList);
        }
        return subscriberRepository.save(subscriberDB);
    }


    @Override
    public void sendSubscribersEmailJobs() {
        List<Subscriber> listSubs = this.subscriberRepository.findAll();
        if (listSubs != null && listSubs.size() > 0) {
            for (Subscriber sub : listSubs) {
                List<Skill> listSkills = sub.getSkills();
                if (listSkills != null && listSkills.size() > 0) {
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
                    if (listJobs != null && listJobs.size() > 0) {
                        EmailSend emailSend = new EmailSend();
                        emailSend.setEmail(sub.getEmail());
                        emailSend.setSubject("Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay");
                        emailSend.setTemplate("job");
                        emailSend.setName(sub.getName());
                        emailSend.setJobs(listJobs);
                        kafkaTemplate.send("email-topic", emailSend);
                    }
                }
            }
        }
    }

}
