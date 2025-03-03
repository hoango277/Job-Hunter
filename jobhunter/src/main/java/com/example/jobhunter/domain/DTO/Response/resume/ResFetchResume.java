package com.example.jobhunter.domain.DTO.Response.resume;

import com.example.jobhunter.utils.constant.StatusEnum;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class ResFetchResume {

    private Long id;

    private String email;

    private String url;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private String companyName;

    private UserResume user;

    private JobResume job;

    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    public static class JobResume {
        private Long id;
        private String name;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    public static class UserResume {
        private Long id;
        private String name;
    }
}
