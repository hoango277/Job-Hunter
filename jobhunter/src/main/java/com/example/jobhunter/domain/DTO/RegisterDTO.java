package com.example.jobhunter.domain.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDTO {
    String email;
    String password;
    String confirmPassword;
    String name;
}
