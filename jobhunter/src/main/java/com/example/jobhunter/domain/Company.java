package com.example.jobhunter.domain;

import com.example.jobhunter.utils.SecurityUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name="companies")
@Getter
@Setter
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="ten cong ty khong duoc de trong")
    private String name;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    private String address;

    private String logo;


//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> users;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Job> jobs;


    @PrePersist
    public void prePersist() {
        createdBy = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : null ;
        createdAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
        updatedBy = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : null;
    }

}
