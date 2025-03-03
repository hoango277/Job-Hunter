package com.example.jobhunter.controller;


import com.example.jobhunter.domain.DTO.ResultPaginationDTO;
import com.example.jobhunter.domain.DTO.Response.user.ResCreateUser;
import com.example.jobhunter.domain.DTO.Response.user.ResUpdateUser;
import com.example.jobhunter.domain.User;
import com.example.jobhunter.domain.DTO.request.user.RequestCreateUser;
import com.example.jobhunter.domain.DTO.request.user.RequestUpdateUser;
import com.example.jobhunter.service.UserService;
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
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @APIMessage("Tạo user mới")
    public ResponseEntity<ResCreateUser> createUser(@Valid @RequestBody RequestCreateUser requestCreateUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                userService.createUser(requestCreateUser)
        );
    }

    @PutMapping("/users")
    @APIMessage("Update user")
    public ResponseEntity<ResUpdateUser> updateUser(@RequestBody RequestUpdateUser requestUpdateUser) {
        return ResponseEntity.status(HttpStatus.OK).body(
                userService.updateUser(requestUpdateUser)
        );
    }

    @GetMapping("/users")
    @APIMessage("Get all user with filter and pagination")
    public ResponseEntity<ResultPaginationDTO> getUsers(
            @Filter Specification<User> spec,
            Pageable pageable
            )
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUsers(spec, pageable));
    }

    @DeleteMapping("/users/{id}")
    @APIMessage("Delete user")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userService.deleteUser(id));
    }
}
