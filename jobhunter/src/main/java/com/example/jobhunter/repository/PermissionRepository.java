package com.example.jobhunter.repository;

import com.example.jobhunter.domain.Permission;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface PermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
    List<Permission> findByIdIn(List<Long> ids);
    Optional<Permission> findByModuleAndApiPathAndMethod(String module, String apiPath, String method);
}
