package com.example.jobhunter.config;

import com.example.jobhunter.domain.DTO.request.email.EmailSend;
import com.example.jobhunter.service.EmailService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id:email-group}")
    private String groupId;

    private final EmailService emailService;

    public KafkaConsumerConfig(EmailService emailService) {
        this.emailService = emailService;
    }

    @Bean
    public ConsumerFactory<String, EmailSend> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.TYPE_MAPPINGS, "emailSend:com.example.jobhunter.domain.DTO.request.email.EmailSend");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.example.jobhunter.domain.DTO.request.email.EmailSend");

        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(new JsonDeserializer<>(EmailSend.class, false)));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EmailSend> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, EmailSend> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @KafkaListener(topics = "email-topic", groupId = "email-group")
    public void consumeEmailNotification(EmailSend notification) {
        emailService.sendEmailFromTemplateSync(
                notification.getEmail(),
                notification.getSubject(),
                notification.getTemplate(),
                notification.getName(),
                notification.getJobs()
        );
    }
}
