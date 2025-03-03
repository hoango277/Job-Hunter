package com.example.jobhunter.domain.DTO.Response;


import com.example.jobhunter.domain.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ResLogin {
    @JsonProperty("access_token")
    private String accessToken;
    private UserLogin user;


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class UserLogin{
        private Long id;
        private String email;
        private String name;
        private Role role;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public  static class UserGetAccount{
        private UserLogin user;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public  static class UserInsideToken{
        private Long id;
        private String email;
        private String name;
    }

}
