package com.example.jobhunter.service;

import com.example.jobhunter.domain.DTO.Response.resume.ResCreateResume;
import com.example.jobhunter.domain.DTO.Response.resume.ResFetchResume;
import com.example.jobhunter.domain.DTO.Response.resume.ResUpdateResume;
import com.example.jobhunter.domain.DTO.ResultPaginationDTO;
import com.example.jobhunter.domain.Resume;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface ResumeService {
    boolean checkResumeExistByUserAndJob(Resume resume);

    ResCreateResume create(Resume resume);

    ResUpdateResume update(Resume resume);

    Void delete(Long id);

    ResFetchResume fetchResumeById(Long id);


    ResultPaginationDTO fetchAll(Specification<Resume> specification, Pageable pageable);

    ResultPaginationDTO fetchUserResume(Pageable pageable);
}
