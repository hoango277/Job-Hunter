package com.example.jobhunter.domain;

import com.example.jobhunter.utils.SecurityUtils;
import com.example.jobhunter.utils.constant.StatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name="resumes")
@Getter
@Setter
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Email can not be empty")
    private String email;
    @NotBlank(message = "URL can not be empty (please upload your cv again)")
    private String url;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="job_id")
    private Job job;

    @PrePersist
    public void handlePrePersist(){
        createdBy = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : null;
        createdAt = Instant.now();
    }

    @PreUpdate
    public void handlePreUpdate(){
        updatedAt = Instant.now();
        updatedBy = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : null ;
    }
}
