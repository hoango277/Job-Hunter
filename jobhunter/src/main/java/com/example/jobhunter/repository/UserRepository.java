package com.example.jobhunter.repository;

import com.example.jobhunter.domain.Company;
import com.example.jobhunter.domain.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> , JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);

    boolean existsUserByEmail(String email);

    Optional<User> findByRefreshTokenAndEmail(String refreshToken,  String email);

    List<User> findByCompany(Company company);
}
