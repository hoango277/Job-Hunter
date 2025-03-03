package com.example.jobhunter.service.impl;

import com.example.jobhunter.domain.Company;
import com.example.jobhunter.domain.DTO.Meta;
import com.example.jobhunter.domain.DTO.Response.job.ResCreateJob;
import com.example.jobhunter.domain.DTO.Response.job.ResUpdateJob;
import com.example.jobhunter.domain.DTO.ResultPaginationDTO;
import com.example.jobhunter.domain.Job;
import com.example.jobhunter.domain.Skill;
import com.example.jobhunter.repository.CompanyRepository;
import com.example.jobhunter.repository.JobRepository;
import com.example.jobhunter.repository.SkillRepository;
import com.example.jobhunter.service.JobService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class JobServiceImpl implements JobService {
    private JobRepository jobRepository;
    private SkillRepository skillRepository;
    private ModelMapper modelMapper;
    private final CompanyRepository companyRepository;
    public JobServiceImpl(JobRepository jobRepository,
                          SkillRepository skillRepository,
                          ModelMapper modelMapper, CompanyRepository companyRepository
    ) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.modelMapper = modelMapper;

        this.companyRepository = companyRepository;
    }

    @Override
    public ResCreateJob createJob(Job job) {
        List<Skill> skills = job.getSkills();
        List<Skill> skillToAdd = new ArrayList<Skill>();
        for (Skill skill : skills) {
            Skill skillCheck = skillRepository.findById(skill.getId())
                    .orElse(null);
            if (skillCheck != null) {
                skillToAdd.add(skillCheck);
            }
        }
        if(job.getCompany() != null) {
            job.setCompany(
                    companyRepository.findById(job.getCompany().getId())
                            .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"))
            );
        }

        job.setSkills(skillToAdd);
        jobRepository.save(job);
        ResCreateJob resCreateJob = modelMapper.map(job, ResCreateJob.class);
        List<String> skillToResponse = job.getSkills().stream()
                .map(x->x.getName())
                .collect(Collectors.toList());
        resCreateJob.setSkills(skillToResponse);
        return resCreateJob;
    }

    @Override
    public ResUpdateJob updateJob(Job job) {
        Job jobToUpdate = jobRepository.findById(job.getId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));
        if(job.getSkills() != null) {
            List<Long> skillId = job.getSkills()
                    .stream().map(x-> x.getId()).collect(Collectors.toList());
            List<Skill> skills = skillRepository.findByIdIn(skillId);
            job.setSkills(skills);
        }
        jobToUpdate.setQuantity(job.getQuantity());
        jobToUpdate.setLevel(job.getLevel());
        jobToUpdate.setName(job.getName());
        jobToUpdate.setDescription(job.getDescription());
        jobToUpdate.setSalary(job.getSalary());
        jobToUpdate.setLocation(job.getLocation());
        jobToUpdate.setStartDate(job.getStartDate());
        if(job.getCompany() != null) {
            jobToUpdate.setCompany(
                    companyRepository.findById(job.getCompany().getId())
                            .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"))
            );
        }



        jobRepository.save(jobToUpdate);

        ResUpdateJob resUpdateJob = modelMapper.map(job, ResUpdateJob.class);
        List<String> skillToResponse = job.getSkills().stream()
                .map(x->x.getName())
                .collect(Collectors.toList());
        resUpdateJob.setSkills(skillToResponse);
        return resUpdateJob;
    }

    @Override
    public Void deleteJob(Long id) {
        Job jobToDelete = jobRepository.findById(id)
                        .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));
        jobRepository.delete(jobToDelete);
        return null;
    }

    @Override
    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));
    }

    @Override
    public ResultPaginationDTO getAllJobs(Specification<Job> specification, Pageable pageable) {
        Page<Job> jobs = jobRepository.findAll(specification, pageable);

        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        Meta meta = new Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setTotal(jobs.getTotalElements());
        meta.setPages(jobs.getTotalPages());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(jobs.getContent());
        return resultPaginationDTO;
    }
}
