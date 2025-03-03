package com.example.jobhunter.service.impl;

import com.example.jobhunter.domain.DTO.Meta;
import com.example.jobhunter.domain.DTO.Response.resume.ResCreateResume;
import com.example.jobhunter.domain.DTO.Response.resume.ResFetchResume;
import com.example.jobhunter.domain.DTO.Response.resume.ResUpdateResume;
import com.example.jobhunter.domain.DTO.ResultPaginationDTO;
import com.example.jobhunter.domain.Job;
import com.example.jobhunter.domain.Resume;
import com.example.jobhunter.domain.User;
import com.example.jobhunter.repository.JobRepository;
import com.example.jobhunter.repository.ResumeRepository;
import com.example.jobhunter.repository.UserRepository;
import com.example.jobhunter.service.ResumeService;
import com.example.jobhunter.utils.SecurityUtils;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResumeServiceImpl implements ResumeService {
    private final FilterParser filterParser;
    private final FilterSpecificationConverter filterSpecificationConverter;
    private JobRepository jobRepository;
    private UserRepository userRepository;
    private ResumeRepository resumeRepository;
    private final ModelMapper modelMapper;

    public ResumeServiceImpl(JobRepository jobRepository, UserRepository userRepository,
                             ResumeRepository resumeRepository, ModelMapper modelMapper, FilterParser filterParser, FilterSpecificationConverter filterSpecificationConverter) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.resumeRepository = resumeRepository;
        this.modelMapper = modelMapper;
        this.filterParser = filterParser;
        this.filterSpecificationConverter = filterSpecificationConverter;
    }
    @Override
    public boolean checkResumeExistByUserAndJob(Resume resume) {
        if(resume.getUser() == null){
            return false;
        }
        Optional<User> user = userRepository.findById(resume.getUser().getId());
        if(user.isEmpty()){
            return false;
        }

        if(resume.getJob() == null){
            return false;
        }
        Optional<Job> job = jobRepository.findById(resume.getJob().getId());
        if(job.isEmpty()){
            return false;
        }
        return true;

    }

    @Override
    public ResCreateResume create(Resume resume) {
        resumeRepository.save(resume);

        ResCreateResume resCreateResume = new ResCreateResume();
        resCreateResume.setCreatedAt(resume.getCreatedAt());
        resCreateResume.setCreatedBy(resume.getCreatedBy());
        resCreateResume.setId(resume.getId());
        return resCreateResume;
    }

    @Override
    public ResUpdateResume update(Resume resume) {
        Resume resumeFetch = resumeRepository.findById(resume.getId())
                .orElseThrow(() -> new RuntimeException("resume not found"));
        resumeFetch.setStatus(resume.getStatus());
        resumeRepository.save(resumeFetch);
        ResUpdateResume resUpdateResume = new ResUpdateResume();
        resUpdateResume.setUpdatedAt(resumeFetch.getUpdatedAt());
        resUpdateResume.setUpdatedBy(resumeFetch.getUpdatedBy());
        return resUpdateResume;
    }

    @Override
    public Void delete(Long id) {
        Resume resume = resumeRepository.findById(id).orElseThrow(
                () -> new RuntimeException("resume not found")
        );
        resumeRepository.delete(resume);
        return null;
    }

    @Override
    public ResFetchResume fetchResumeById(Long id) {
        Resume resume = resumeRepository.findById(id).orElseThrow(
                () -> new RuntimeException("resume not found")
        );
        ResFetchResume resFetchResume = modelMapper.map(resume, ResFetchResume.class);
        ResFetchResume.JobResume jobResume = modelMapper.map(resume.getJob(), ResFetchResume.JobResume.class);
        ResFetchResume.UserResume userResume = modelMapper.map(resume.getUser(), ResFetchResume.UserResume.class);

        if(resume.getJob() != null){
            resFetchResume.setCompanyName(resume.getJob().getCompany().getName());
        }

        resFetchResume.setJob(jobResume);
        resFetchResume.setUser(userResume);
        return resFetchResume;
    }

    @Override
    public ResultPaginationDTO fetchAll(Specification<Resume> specification, Pageable pageable) {
        Page<Resume> resumes = resumeRepository.findAll(specification, pageable);

        ResultPaginationDTO paginationDTO = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPages(pageable.getPageSize());
        meta.setPages(resumes.getTotalPages());
        meta.setTotal(resumes.getTotalElements());

        paginationDTO.setMeta(meta);
        List<ResFetchResume> listResume = resumes.getContent().stream()
                        .map(item -> this.fetchResumeById(item.getId()))
                                .collect(Collectors.toList());
        paginationDTO.setResult(listResume);
        return paginationDTO;

    }

    @Override
    public ResultPaginationDTO fetchUserResume(Pageable pageable) {
        String email = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : null;
        FilterNode node = filterParser.parse("email='" + email + "'");
        FilterSpecification<Resume> specification = filterSpecificationConverter.convert(node);

        Page<Resume> resumes = resumeRepository.findAll(specification, pageable);

        ResultPaginationDTO paginationDTO = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPages(pageable.getPageSize());
        meta.setPages(resumes.getTotalPages());
        meta.setTotal(resumes.getTotalElements());
        paginationDTO.setMeta(meta);
        List<ResFetchResume> listResume = resumes.getContent().stream()
                .map(item -> this.fetchResumeById(item.getId()))
                .collect(Collectors.toList());
        paginationDTO.setResult(listResume);
        return paginationDTO;
    }


}
