package com.example.jobhunter.repository;

import com.example.jobhunter.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;



public interface CompanyRepository extends JpaRepository<Company, Long> , JpaSpecificationExecutor<Company> {
}
