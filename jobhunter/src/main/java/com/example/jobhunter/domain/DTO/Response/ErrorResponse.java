package com.example.jobhunter.domain.DTO.Response;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse<T>{
    private int statusCode;
    private String errorMessage;
    private T data;
}
