package com.example.jobhunter.domain.DTO.Response.user;

import com.example.jobhunter.domain.DTO.Response.company.CompanyResponse;
import com.example.jobhunter.utils.constant.GenderEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private int age;
    private GenderEnum gender;
    private CompanyResponse company;
    private RoleUser role;


    @Getter
    @Setter
    @NoArgsConstructor
    public static class RoleUser{
        private Long id;
        private String name;
    }

}
