package com.example.jobhunter.config;

import java.util.List;

import com.example.jobhunter.domain.Permission;
import com.example.jobhunter.domain.Role;
import com.example.jobhunter.domain.User;
import com.example.jobhunter.service.UserService;
import com.example.jobhunter.util.SecurityUtil;
import com.example.jobhunter.util.error.IdInvalidException;
import com.example.jobhunter.util.error.PermissionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



public class PermissionInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;

    @Override
    @Transactional
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception, IdInvalidException {

        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);

        // check permission
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        if (email != null && !email.isEmpty()) {
            User user = this.userService.getUserByUsername(email);
            if (user != null) {
                Role role = user.getRole();
                if (role != null) {
                    List<Permission> permissions = role.getPermissions();
                    boolean isAllow = permissions.stream()
                            .anyMatch(item -> item.getApiPath().equals(path) && item.getMethod().equals(httpMethod));
                    if (isAllow == false)
                        throw new PermissionException("Bạn không có quyền truy cập");
                    System.out.println(">>>>>> requestURI= " + requestURI);

                } else {
                    throw new PermissionException("Bạn không có quyền truy cập endpoint này");
                }
            }
        }
        return true;
    }
}
