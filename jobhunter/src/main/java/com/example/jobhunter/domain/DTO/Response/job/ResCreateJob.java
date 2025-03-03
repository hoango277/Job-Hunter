package com.example.jobhunter.domain.DTO.Response.job;


import com.example.jobhunter.utils.constant.LevelEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class ResCreateJob {
    private String name;
    private String location;
    private double salary;
    private int quantity;
    private LevelEnum level;
    private String description;
    private Instant startDate;
    private Instant endDate;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private List<String> skills;
}
