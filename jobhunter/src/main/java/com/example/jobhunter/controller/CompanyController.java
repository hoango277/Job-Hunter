package com.example.jobhunter.controller;

import com.example.jobhunter.domain.Company;
import com.example.jobhunter.domain.DTO.ResultPaginationDTO;
import com.example.jobhunter.service.CompanyService;
import com.example.jobhunter.utils.annotation.APIMessage;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${api.version}")
public class CompanyController {

    private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    @APIMessage("Create a new Company")
    public ResponseEntity<Company> addCompany(@Valid @RequestBody Company company) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.addCompany(company));
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company company) {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.updateCompany(company));
    }

    @GetMapping("/companies")
    @APIMessage("get all companies with filter and pageable")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(
            @Filter Specification<Company> specification,
            Pageable pageable
            ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                companyService.getAllCompanies(specification, pageable)
        );
    }

    @DeleteMapping("/companies/{id}")
    @APIMessage("Delete company")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("/companies/{id}")
    @APIMessage("Get Company by id")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {

        return ResponseEntity.status(HttpStatus.OK).body(
                companyService.getCompanyById(id)
        );
    }
}
