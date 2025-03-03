package com.example.jobhunter.service.impl;

import com.example.jobhunter.domain.DTO.Meta;
import com.example.jobhunter.domain.DTO.ResultPaginationDTO;
import com.example.jobhunter.domain.Permission;
import com.example.jobhunter.domain.Role;
import com.example.jobhunter.repository.PermissionRepository;
import com.example.jobhunter.repository.RoleRepository;
import com.example.jobhunter.service.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }


    @Override
    public Role create(Role role) {
        if(roleRepository.existsByName(role.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role already exists");
        }
        if(role.getPermissions() != null)
        {
            List<Long> permission = role.getPermissions()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());

            List<Permission> permissionList = permissionRepository.findByIdIn(permission);
            role.setPermissions(permissionList);
        }
        return roleRepository.save(role);
    }

    @Override
    public Role update(Role role) {
        Role roleDB = roleRepository.findById(role.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
        if(role.getPermissions() != null)
        {
            List<Long> permission = role.getPermissions()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());

            List<Permission> permissionList = permissionRepository.findByIdIn(permission);
            roleDB.setPermissions(permissionList);
        }

        roleDB.setName(role.getName());
        roleDB.setDescription(role.getDescription());
        roleDB.setActive(role.isActive());

        return roleRepository.save(roleDB);
    }

    @Override
    public Void delete(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
        roleRepository.delete(role);
        return null;
    }

    @Override
    public ResultPaginationDTO fetchAll(Specification<Role> specification, Pageable pageable) {
        Page<Role> rolePage = roleRepository.findAll(specification, pageable);

        ResultPaginationDTO paginationDTO = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPages(pageable.getPageSize());
        meta.setPages(rolePage.getTotalPages());
        meta.setTotal(rolePage.getTotalElements());

        paginationDTO.setMeta(meta);
        paginationDTO.setResult(rolePage.getContent());

        return paginationDTO;
    }

    @Override
    public Role fetchById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
    }
}
