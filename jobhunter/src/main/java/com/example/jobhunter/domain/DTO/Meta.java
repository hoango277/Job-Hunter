package com.example.jobhunter.domain.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Meta {
    private int page;
    private int pageSize;
    private int pages;
    private Long total;
}
