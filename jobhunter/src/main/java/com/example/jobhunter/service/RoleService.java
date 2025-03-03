package com.example.jobhunter.service;

import com.example.jobhunter.domain.DTO.ResultPaginationDTO;
import com.example.jobhunter.domain.Role;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface RoleService {
    Role create(@Valid Role role);

    Role update(@Valid Role role);

    Void delete(Long id);

    ResultPaginationDTO fetchAll(Specification<Role> specification, Pageable pageable);

    Role fetchById(Long id);
}
