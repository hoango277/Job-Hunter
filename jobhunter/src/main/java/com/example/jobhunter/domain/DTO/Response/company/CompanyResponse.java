package com.example.jobhunter.domain.DTO.Response.company;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class CompanyResponse {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String logo;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
}
