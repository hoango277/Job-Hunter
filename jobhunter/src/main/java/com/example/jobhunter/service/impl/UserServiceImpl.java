package com.example.jobhunter.service.impl;

import com.example.jobhunter.domain.Company;
import com.example.jobhunter.domain.DTO.Meta;
import com.example.jobhunter.domain.DTO.RegisterDTO;
import com.example.jobhunter.domain.DTO.Response.company.CompanyResponse;
import com.example.jobhunter.domain.DTO.Response.user.UserResponse;
import com.example.jobhunter.domain.DTO.ResultPaginationDTO;
import com.example.jobhunter.domain.DTO.Response.user.ResCreateUser;
import com.example.jobhunter.domain.DTO.Response.user.ResUpdateUser;
import com.example.jobhunter.domain.Role;
import com.example.jobhunter.domain.User;
import com.example.jobhunter.domain.DTO.request.user.RequestCreateUser;
import com.example.jobhunter.domain.DTO.request.user.RequestUpdateUser;
import com.example.jobhunter.repository.CompanyRepository;
import com.example.jobhunter.repository.RoleRepository;
import com.example.jobhunter.repository.UserRepository;
import com.example.jobhunter.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private CompanyRepository companyRepository;
    private PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           ModelMapper modelMapper,
                           CompanyRepository companyRepository, RoleRepository roleRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.companyRepository = companyRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }

    @Override
    public User register(RegisterDTO registerDTO) {
        boolean checkUserIsExistByEmail = userRepository.existsUserByEmail(registerDTO.getEmail());
        if(checkUserIsExistByEmail) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Đã có email này trong database!");
        }
        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setName(registerDTO.getName());
        userRepository.save(user);
        return user;
    }

    @Override
    public ResCreateUser createUser(RequestCreateUser requestCreateUser) throws ResponseStatusException {
        User user = new User();
        if(requestCreateUser.getRole() != null)
        {
            Role r = roleRepository.findById(requestCreateUser.getRole().getId()).orElse(null);
            user.setRole(r);
        }

        if(requestCreateUser.getCompany() != null) {
            Company company = companyRepository.findById(requestCreateUser.getCompany().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
            user.setCompany(company);
        }
        boolean checkUserIsExistByEmail = userRepository.existsUserByEmail(requestCreateUser.getEmail());
        if(checkUserIsExistByEmail) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Đã có email này trong database!");
        }
        user.setEmail(requestCreateUser.getEmail());
        user.setAge(requestCreateUser.getAge());
        user.setGender(requestCreateUser.getGender());
        user.setAddress(requestCreateUser.getAddress());
        user.setPassword(passwordEncoder.encode(requestCreateUser.getPassword()));
        user.setName(requestCreateUser.getName());
        userRepository.save(user);

        ResCreateUser resCreateUser = modelMapper.map(user, ResCreateUser.class);
        resCreateUser.setCompany(modelMapper.map(user.getCompany(), CompanyResponse.class));

        return resCreateUser;
    }

    @Override
    public ResUpdateUser updateUser(RequestUpdateUser requestUpdateUser) {


        User userToUpdate = userRepository.findById(requestUpdateUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if(requestUpdateUser.getRole() != null)
        {
            Role r = roleRepository.findById(requestUpdateUser.getRole().getId()).orElse(null);
            userToUpdate.setRole(r);
        }
        if(requestUpdateUser.getCompany() != null) {
            Company company = companyRepository.findById(requestUpdateUser.getCompany().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
            userToUpdate.setCompany(company);
        }

        userToUpdate.setAge(requestUpdateUser.getAge());
        userToUpdate.setGender(requestUpdateUser.getGender());
        userToUpdate.setAddress(requestUpdateUser.getAddress());
        userToUpdate.setName(requestUpdateUser.getName());
        userRepository.save(userToUpdate);
        ResUpdateUser resUpdateUser = modelMapper.map(userToUpdate, ResUpdateUser.class);
        resUpdateUser.setCompany(modelMapper.map(userToUpdate.getCompany(), CompanyResponse.class));

        return resUpdateUser;
    }

    @Override
    public ResultPaginationDTO getUsers(Specification<User> spec, Pageable pageable) {
        Page<User> users = userRepository.findAll(spec, pageable);


        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(users.getTotalPages());
        meta.setTotal(users.getTotalElements());

        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        resultPaginationDTO.setMeta(meta);

        List<UserResponse> userResponses = new ArrayList<>();
        List<User> listUser= users.getContent();
        for(User user : listUser) {
            UserResponse userResponse = modelMapper.map(user, UserResponse.class);
            if(user.getRole() != null)
            {
                UserResponse.RoleUser roleUser = modelMapper.map(user.getRole(), UserResponse.RoleUser.class);
                userResponse.setRole(roleUser);
            }
            userResponses.add(userResponse);
        }

        resultPaginationDTO.setResult(userResponses);

        return resultPaginationDTO;
    }

    @Override
    public Void deleteUser(Long id) {
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        userRepository.delete(userToDelete);
        return null;
    }

    @Override
    public void setUserRefreshToken(String refreshToken, User user) {
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByRefreshTokenAndEmail(String refreshToken, String email) {

        return userRepository.findByRefreshTokenAndEmail(refreshToken, email);
    }

    @Override
    public void logout(User user) {
        user.setRefreshToken(null);
        userRepository.save(user);
    }


}
