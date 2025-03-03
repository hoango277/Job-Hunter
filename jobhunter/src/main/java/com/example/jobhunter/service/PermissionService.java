package com.example.jobhunter.service;

import com.example.jobhunter.domain.DTO.ResultPaginationDTO;
import com.example.jobhunter.domain.Permission;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface PermissionService {
    Permission create(@Valid Permission permission);

    Permission update(@Valid Permission permission);

    Void delete(Long id);

    ResultPaginationDTO fetchAll(Specification<Permission> specification, Pageable pageable);
}
