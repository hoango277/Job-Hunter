package com.example.jobhunter.domain;

import com.example.jobhunter.utils.SecurityUtils;
import com.example.jobhunter.utils.constant.LevelEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="jobs")
@Getter
@Setter
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private double salary;
    private int quantity;
    @Enumerated(EnumType.STRING)
    private LevelEnum level;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    private Instant startDate;
    private Instant endDate;
    private boolean active;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"jobs"})
    @JoinTable(name = "job_skill", joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skills;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Resume> resumes;


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
