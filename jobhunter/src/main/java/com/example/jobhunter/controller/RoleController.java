package com.example.jobhunter.controller;

import com.example.jobhunter.domain.DTO.ResultPaginationDTO;
import com.example.jobhunter.domain.Role;
import com.example.jobhunter.service.RoleService;
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
public class RoleController {

    private final RoleService roleService;


    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @APIMessage("create a role")
    public ResponseEntity<Role> create(@Valid @RequestBody Role role)
    {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roleService.create(role));
    }

    @PutMapping("/roles")
    @APIMessage("update role")
    public ResponseEntity<Role> update(@Valid @RequestBody Role role){
        return ResponseEntity.status(HttpStatus.OK)
                .body(roleService.update(role));
    }

    @DeleteMapping("/roles/{id}")
    @APIMessage("delete role")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(roleService.delete(id));
    }

    @GetMapping("/roles")
    @APIMessage("fetch role")
    public ResponseEntity<ResultPaginationDTO> fetchAll(
            @Filter Specification<Role> specification,
            Pageable pageable
            ){
        return ResponseEntity.status(HttpStatus.OK).body(roleService.fetchAll(specification, pageable));
    }

    @GetMapping("/roles/{id}")
    @APIMessage("fetch role by id")
    public ResponseEntity<Role> fetchById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(roleService.fetchById(id));
    }

}

