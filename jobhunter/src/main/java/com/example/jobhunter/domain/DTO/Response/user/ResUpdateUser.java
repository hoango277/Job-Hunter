package com.example.jobhunter.domain.DTO.Response.user;

import com.example.jobhunter.domain.DTO.Response.company.CompanyResponse;
import com.example.jobhunter.utils.constant.GenderEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;


@Getter
@Setter
public class ResUpdateUser {
    private String name;
    private String email;
    private int age;
    private GenderEnum gender;
    private Instant updatedAt;
    private String updatedBy;
    private CompanyResponse company;
}
