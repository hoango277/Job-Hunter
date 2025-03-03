package com.example.jobhunter.service;

import com.example.jobhunter.domain.Company;
import com.example.jobhunter.domain.DTO.ResultPaginationDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CompanyService {

    Company addCompany(@Valid @RequestBody Company company);

    Company updateCompany(@Valid @RequestBody Company company);

    ResultPaginationDTO getAllCompanies(Specification<Company> specification, Pageable pageable);

    Void deleteCompany(Long id);

    Company getCompanyById(Long id);
}
