package com.example.jobhunter.domain;


import com.example.jobhunter.utils.SecurityUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
@Getter
@Setter
@Entity
@Table(name="subscribers")
public class Subscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "name cannot be blank")
    private String name;
    @NotBlank(message = "email cannot be blank")
    private String email;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value={"subscribers"})
    @JoinTable(name="subcriber_skill", joinColumns = @JoinColumn(name = "subscriber_id"), inverseJoinColumns = @JoinColumn(name="skill_id"))
    private List<Skill> skills;

    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;

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
