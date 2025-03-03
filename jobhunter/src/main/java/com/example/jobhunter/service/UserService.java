package com.example.jobhunter.service;

import com.example.jobhunter.domain.DTO.RegisterDTO;
import com.example.jobhunter.domain.DTO.ResultPaginationDTO;
import com.example.jobhunter.domain.DTO.Response.user.ResCreateUser;
import com.example.jobhunter.domain.DTO.Response.user.ResUpdateUser;
import com.example.jobhunter.domain.User;
import com.example.jobhunter.domain.DTO.request.user.RequestCreateUser;
import com.example.jobhunter.domain.DTO.request.user.RequestUpdateUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface UserService {
    User findByEmail(String email);

    User register(RegisterDTO registerDTO);

    ResCreateUser createUser(RequestCreateUser requestCreateUser);

    ResUpdateUser updateUser(RequestUpdateUser requestUpdateUser);


    ResultPaginationDTO getUsers(Specification<User> spec, Pageable pageable);

    Void deleteUser(Long id);

    void setUserRefreshToken(String refreshToken, User user);

    Optional<User> findByRefreshTokenAndEmail(String refreshToken, String email);

    void logout(User user);



}
