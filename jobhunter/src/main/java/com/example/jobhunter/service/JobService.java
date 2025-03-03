package com.example.jobhunter.service;

import com.example.jobhunter.domain.Company;
import com.example.jobhunter.domain.DTO.Response.job.ResCreateJob;
import com.example.jobhunter.domain.DTO.Response.job.ResUpdateJob;
import com.example.jobhunter.domain.DTO.ResultPaginationDTO;
import com.example.jobhunter.domain.Job;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface JobService {

    ResCreateJob createJob(Job job);

    ResUpdateJob updateJob(Job job);

    Void deleteJob(Long id);

    Job getJobById(Long id);

    ResultPaginationDTO getAllJobs(Specification<Job> specification, Pageable pageable);
}
