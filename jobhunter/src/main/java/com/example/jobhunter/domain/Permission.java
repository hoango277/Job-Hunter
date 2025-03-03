package com.example.jobhunter.domain;

import com.example.jobhunter.utils.SecurityUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name="permissions")
@Getter
@Setter
@NoArgsConstructor
public class Permission {
    public Permission(String name, String apiPath, String method, String module)
    {
        this.name = name;
        this.apiPath = apiPath;
        this.method = method;
        this.module = module;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message ="name can not be empty")
    private String name;
    @NotBlank(message ="api path can not be empty")
    private String apiPath;
    @NotBlank(message ="method can not be empty")
    private String method;
    @NotBlank(message ="module can not be empty")
    private String module;

    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Role> roles;

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
