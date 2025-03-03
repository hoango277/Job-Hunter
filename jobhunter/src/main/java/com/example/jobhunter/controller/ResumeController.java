package com.example.jobhunter.controller;

import com.example.jobhunter.domain.DTO.Response.resume.ResCreateResume;
import com.example.jobhunter.domain.DTO.Response.resume.ResFetchResume;
import com.example.jobhunter.domain.DTO.Response.resume.ResUpdateResume;
import com.example.jobhunter.domain.DTO.ResultPaginationDTO;
import com.example.jobhunter.domain.Resume;
import com.example.jobhunter.service.ResumeService;
import com.example.jobhunter.utils.annotation.APIMessage;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("${api.version}")
public class ResumeController {
    private ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    @APIMessage("Create a new CV")
    public ResponseEntity<ResCreateResume> create(@Valid @RequestBody Resume resume)
    {
        boolean checkIdExist = resumeService.checkResumeExistByUserAndJob(resume);
        if(!checkIdExist)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or Job does not exist");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(
                resumeService.create(resume)
        );
    }

    @PutMapping("/resumes")
    @APIMessage("Update CV")
    public ResponseEntity<ResUpdateResume> update(@RequestBody Resume resume)
    {
        return ResponseEntity.status(HttpStatus.OK).body(
                resumeService.update(resume)
        );
    }

    @DeleteMapping("/resumes/{id}")
    @APIMessage("Delete CV")
    public ResponseEntity<Void> delete(@PathVariable Long id)
    {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(resumeService.delete(id));
    }

    @GetMapping("/resumes/{id}")
    @APIMessage("Fetch resume by ID")
    public ResponseEntity<ResFetchResume> fetchResumeById(@PathVariable Long id)
    {
        return ResponseEntity.status(HttpStatus.OK).body(
                resumeService.fetchResumeById(id)
        );
    }

    @GetMapping("/resumes")
    @APIMessage("Fetch all resume")
    public ResponseEntity<ResultPaginationDTO> fetchAllResume(
            @Filter Specification<Resume> specification
            , Pageable pageable
            )
    {
        return ResponseEntity.status(HttpStatus.OK).body(
                resumeService.fetchAll(specification, pageable)
        );
    }

    @PostMapping("/resumes/by-user")
    @APIMessage("Fetch user resume")
    public ResponseEntity<ResultPaginationDTO> fetchAllResume(Pageable pageable
    )
    {
        return ResponseEntity.status(HttpStatus.OK).body(
                resumeService.fetchUserResume(pageable)
        );
    }



}
