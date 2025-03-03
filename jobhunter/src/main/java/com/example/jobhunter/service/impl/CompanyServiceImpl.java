package com.example.jobhunter.service.impl;

import com.example.jobhunter.domain.Company;
import com.example.jobhunter.domain.DTO.Meta;
import com.example.jobhunter.domain.DTO.Response.company.CompanyResponse;
import com.example.jobhunter.domain.DTO.ResultPaginationDTO;
import com.example.jobhunter.domain.User;
import com.example.jobhunter.repository.CompanyRepository;
import com.example.jobhunter.repository.UserRepository;
import com.example.jobhunter.service.CompanyService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CompanyServiceImpl implements CompanyService {
    private UserRepository userRepository;
    private CompanyRepository companyRepository;
    private ModelMapper modelMapper;

    public CompanyServiceImpl(CompanyRepository companyRepository,
                              UserRepository userRepository,
                              ModelMapper modelMapper
    ) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public Company addCompany(Company company) {
        companyRepository.save(company);
        return company;
    }

    @Override
    public Company updateCompany(Company company) {
        return companyRepository.findById(company.getId())
                .map(existingCompany -> {
                    existingCompany.setName(company.getName());
                    existingCompany.setLogo(company.getLogo());
                    existingCompany.setDescription(company.getDescription());
                    return companyRepository.save(existingCompany);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
    }

    @Override
    public ResultPaginationDTO getAllCompanies(Specification<Company> spec, Pageable pageable) {
        Page<Company> pages = companyRepository.findAll(spec, pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        Meta meta = new Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pages.getTotalPages());
        meta.setTotal(pages.getTotalElements());

        List<CompanyResponse> companyResponses = new ArrayList<>();
        for (Company company : pages.getContent()) {
            CompanyResponse companyResponse = modelMapper.map(company, CompanyResponse.class);
            companyResponses.add(companyResponse);
        }
        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(companyResponses);
        return resultPaginationDTO;
    }

    @Override
    public Void deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
        List<User> user = userRepository.findByCompany(company);
        userRepository.deleteAll(user);
        companyRepository.delete(company);
        return null;
    }

    @Override
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
    }
}

