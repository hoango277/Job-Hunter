package com.example.jobhunter.controller;

import com.example.jobhunter.domain.Company;
import com.example.jobhunter.domain.response.ResultPaginationDTO;
import com.example.jobhunter.service.CompanyService;
import com.example.jobhunter.util.annotation.ApiMessage;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;


import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<?> createCompany(@Valid @RequestBody Company company) {

        return ResponseEntity.status(HttpStatus.CREATED).body(this.companyService.handleCreateCompany(company));
    }

    @GetMapping("/companies")
    @ApiMessage("Fetch Companies")
    public ResponseEntity<ResultPaginationDTO> getCompany(
            @Filter Specification<Company> spec, Pageable pageable) {

        return ResponseEntity.ok(this.companyService.handleGetCompany(spec, pageable));
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company company) {
        Company updateCompany = this.companyService.handleUpdateCompany(company);
        return ResponseEntity.ok(updateCompany);

    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") long id) {
        this.companyService.handleDeleteCompany(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/companies/{id}")
    @ApiMessage("fetch company by id")
    public ResponseEntity<Company> fetchCompanyById(@PathVariable("id") long id) {
        Optional<Company> companyOptional = this.companyService.findById(id);
        return ResponseEntity.ok().body(companyOptional.get());
    }

}
