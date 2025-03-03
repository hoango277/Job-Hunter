package com.example.jobhunter.domain;

import com.example.jobhunter.utils.SecurityUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name="skills")
@Getter
@Setter
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "Skill name cannot be blank")
    private String name;

    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;


    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "skills")
    @JsonIgnore
    private List<Job> jobs;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "skills")
    @JsonIgnore
    private List<Subscriber> subscribers;

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
