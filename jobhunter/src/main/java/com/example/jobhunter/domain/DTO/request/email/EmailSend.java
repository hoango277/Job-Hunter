package com.example.jobhunter.domain.DTO.request.email;

import com.example.jobhunter.domain.Job;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
public class EmailSend implements Serializable {
    private String email;
    private String subject;
    private String template;
    private String name;
    private List<Job> jobs;
}
