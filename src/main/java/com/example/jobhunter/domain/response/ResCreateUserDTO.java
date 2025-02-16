package com.example.jobhunter.domain.response;

import java.time.Instant;

import com.example.jobhunter.util.constant.GenderEnum;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ResCreateUserDTO {
    private long id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant createAt;
    private CompanyUser company;

    @Setter
    @Getter
    public static class CompanyUser {
        private long id;
        private String name;

    }
}
