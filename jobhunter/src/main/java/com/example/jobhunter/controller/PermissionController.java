package com.example.jobhunter.controller;

import com.example.jobhunter.domain.DTO.ResultPaginationDTO;
import com.example.jobhunter.domain.Permission;
import com.example.jobhunter.service.PermissionService;
import com.example.jobhunter.utils.annotation.APIMessage;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.version}")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }


    @PostMapping("/permissions")
    @APIMessage("create a permission")
    public ResponseEntity<Permission> create(@Valid @RequestBody Permission permission) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(permissionService.create(permission));
    }

    @PutMapping("/permissions")
    @APIMessage("Update a permission")
    public ResponseEntity<Permission> update(@Valid @RequestBody Permission permission) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(permissionService.update(permission));
    }

    @DeleteMapping("/permissions/{id}")
    @APIMessage("delete a permission")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(permissionService.delete(id));
    }

    @GetMapping("/permissions")
    @APIMessage("fetch all permission")
    public ResponseEntity<ResultPaginationDTO> fetchAllPermissions(
            @Filter Specification<Permission> specification,
            Pageable pageable
            ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(permissionService.fetchAll(specification, pageable));
    }



}
