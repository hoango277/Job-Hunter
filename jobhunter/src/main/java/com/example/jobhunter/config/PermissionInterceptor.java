package com.example.jobhunter.config;

import com.example.jobhunter.domain.Permission;
import com.example.jobhunter.domain.Role;
import com.example.jobhunter.domain.User;
import com.example.jobhunter.repository.UserRepository;
import com.example.jobhunter.utils.SecurityUtils;
import com.example.jobhunter.utils.errors.PermissionException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;

public class PermissionInterceptor implements HandlerInterceptor {
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {
        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String httpMethod = request.getMethod();

        String email = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : null ;
        if(email != null && !email.isEmpty()) {
            User user = userRepository.findByEmail(email)
                    .orElse(null);
            if(user != null) {
                Role role = user.getRole();
                if(role != null) {
                    List<Permission> permissionList = role.getPermissions();
                    if(permissionList != null && !permissionList.isEmpty()) {
                        boolean check = permissionList.stream()
                                .anyMatch(item -> item.getApiPath().equals(path) && item.getMethod().equals(httpMethod));
                        if(check) {
                            return true;
                        }
                    }
                }
            }
        }
        throw new PermissionException("Permission Error");
    }
}
