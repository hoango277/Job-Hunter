package com.example.jobhunter.service.impl;

import com.example.jobhunter.domain.DTO.Meta;
import com.example.jobhunter.domain.DTO.ResultPaginationDTO;
import com.example.jobhunter.domain.Permission;
import com.example.jobhunter.domain.Role;
import com.example.jobhunter.repository.PermissionRepository;
import com.example.jobhunter.repository.RoleRepository;
import com.example.jobhunter.service.PermissionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository, RoleRepository roleRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
    }

    public boolean exists(@Valid Permission permission) {
        Optional<Permission> permissionCheck = permissionRepository.findByModuleAndApiPathAndMethod(
                permission.getModule(),
                permission.getApiPath(),
                permission.getMethod()
        );
        if(permissionCheck.isPresent()) {
            return true;
        }
        return false;
    }

    @Override
    public Permission create(Permission permission) {
        Optional<Permission> permissionCheck = permissionRepository.findByModuleAndApiPathAndMethod(
                permission.getModule(),
                permission.getApiPath(),
                permission.getMethod()
        );
        if(permissionCheck.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Permission already exists");
        }

        return permissionRepository.save(permission);
    }

    @Override
    public Permission update(Permission permission) {
        if(exists(permission)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Permission already exists");
        }
        Permission permissionDB = permissionRepository.findById(permission.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "permission not found"));
        if(permissionDB.getName().equals(permission.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Permission already exists");
        }
        permissionDB.setName(permission.getName());
        permissionDB.setApiPath(permission.getApiPath());
        permissionDB.setMethod(permission.getMethod());
        permissionDB.setModule(permission.getModule());

        return permissionRepository.save(permissionDB);
    }

    @Override
    public Void delete(Long id) {
        Permission permissionDB = permissionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "permission not found"));
        List<Role> roles = permissionDB.getRoles();
        roleRepository.deleteAll(roles);
        permissionRepository.delete(permissionDB);
        return null;
    }

    @Override
    public ResultPaginationDTO fetchAll(Specification<Permission> specification, Pageable pageable) {
        Page<Permission> permissionPage = permissionRepository.findAll(specification, pageable);

        ResultPaginationDTO paginationDTO = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPages(pageable.getPageSize());
        meta.setPages(permissionPage.getTotalPages());
        meta.setTotal(permissionPage.getTotalElements());

        paginationDTO.setMeta(meta);
        paginationDTO.setResult(permissionPage.getContent());

        return paginationDTO;
    }
}
