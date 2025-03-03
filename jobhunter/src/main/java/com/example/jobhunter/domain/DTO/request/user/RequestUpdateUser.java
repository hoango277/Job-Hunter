package com.example.jobhunter.domain.DTO.request.user;

import com.example.jobhunter.domain.Company;
import com.example.jobhunter.domain.Role;
import com.example.jobhunter.utils.constant.GenderEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RequestUpdateUser {
    private Long id;
    private String name;
    private int age;
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
    private String address;
    private Company company;
    private Role role;
}
