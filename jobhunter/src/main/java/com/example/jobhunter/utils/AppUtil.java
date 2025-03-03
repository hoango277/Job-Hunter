package com.example.jobhunter.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;




@Component
@Order(0)
public class AppUtil {
    @Value("${api.version}")
    private String apiVersion;

    public static String[] whitelist;
    public static String[] whiteListInterceptor;

    @PostConstruct
    public void init() {
        whitelist = new String[]{
                apiVersion + "/auth/login",
                apiVersion + "/auth/register",
                apiVersion + "/auth/refresh",
                apiVersion + "/companies/**",
                apiVersion + "/jobs/**",
                apiVersion + "/emails/**",
                "/storage/**",
                "/"
        };
        whiteListInterceptor = new String[]{
                apiVersion + "/auth/**",
                apiVersion + "/companies/**",
                apiVersion + "/jobs/**",
                apiVersion + "/emails/**",
                apiVersion + "/skills",
                apiVersion + "/resumes/by-user",
                apiVersion + "/subscribers/**",
                "/storage/**",
                "/"
        };
    }
}
