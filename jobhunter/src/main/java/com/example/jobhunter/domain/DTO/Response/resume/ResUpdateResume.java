package com.example.jobhunter.domain.DTO.Response.resume;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResUpdateResume {
    private Instant updatedAt;
    private String updatedBy;
}
