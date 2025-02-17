package com.example.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.example.jobhunter.domain.Job;
import com.example.jobhunter.domain.Resume;
import com.example.jobhunter.domain.User;
import com.example.jobhunter.domain.response.ResultPaginationDTO;
import com.example.jobhunter.domain.response.resume.ResCreateResumeDTO;
import com.example.jobhunter.domain.response.resume.ResFetchResumeDTO;
import com.example.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import com.example.jobhunter.repository.JobRepository;
import com.example.jobhunter.repository.ResumeRepository;
import com.example.jobhunter.repository.UserRepository;
import com.example.jobhunter.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;



@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    @Autowired
    FilterBuilder fb;
    @Autowired
    private FilterParser filterParser;
    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;

    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository,
            JobRepository jobRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    public Optional<Resume> fetchById(long id) {
        return this.resumeRepository.findById(id);
    }

    public boolean checkResumeExistByUserAndJob(Resume resume) {
        if (resume.getUser() == null)
            return false;
        Optional<User> userOptional = this.userRepository.findById(resume.getUser().getId());
        if (userOptional.isEmpty())
            return false;

        if (resume.getJob() == null)
            return false;
        Optional<Job> jobOptional = this.jobRepository.findById(resume.getJob().getId());
        if (jobOptional.isEmpty())
            return false;

        return true;
    }

    public ResCreateResumeDTO create(Resume resume) {
        resume = this.resumeRepository.save(resume);
        ResCreateResumeDTO resCreateResumeDTO = new ResCreateResumeDTO();
        resCreateResumeDTO.setId(resume.getId());
        resCreateResumeDTO.setCreatedAt(resume.getCreatedAt());
        resCreateResumeDTO.setCreatedBy(resume.getCreatedBy());

        return resCreateResumeDTO;
    }

    public ResUpdateResumeDTO update(Resume resume) {
        resume = this.resumeRepository.save(resume);
        ResUpdateResumeDTO resUpdateResumeDTO = new ResUpdateResumeDTO();
        resUpdateResumeDTO.setUpdatedAt(resume.getUpdatedAt());
        resUpdateResumeDTO.setUpdatedBy(resume.getUpdatedBy());

        return resUpdateResumeDTO;

    }

    public void deleteById(long id) {
        this.resumeRepository.deleteById(id);
    }

    public ResFetchResumeDTO getResume(Resume resume) {
        ResFetchResumeDTO resFetchResumeDTO = new ResFetchResumeDTO();
        resFetchResumeDTO.setId(resume.getId());
        resFetchResumeDTO.setEmail(resume.getEmail());
        resFetchResumeDTO.setUrl(resume.getUrl());
        resFetchResumeDTO.setStatus(resume.getStatus());
        resFetchResumeDTO.setCreateBy(resume.getCreatedBy());
        resFetchResumeDTO.setCreatedAt(resume.getCreatedAt());
        resFetchResumeDTO.setUpdatedAt(resume.getUpdatedAt());
        resFetchResumeDTO.setUpdatedBy(resume.getUpdatedBy());

        if (resume.getJob() != null) {
            resFetchResumeDTO.setCompanyName(resume.getJob().getCompany().getName());
        }

        resFetchResumeDTO
                .setUser(new ResFetchResumeDTO.UserResume(resume.getUser().getId(), resume.getUser().getName()));
        resFetchResumeDTO.setJob(new ResFetchResumeDTO.JobResume(resume.getJob().getId(), resume.getJob().getName()));

        return resFetchResumeDTO;
    }

    public ResultPaginationDTO fetchAll(Specification specification, Pageable pageable) {
        Page<Resume> pageResume = this.resumeRepository.findAll(specification, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageResume.getTotalPages());
        mt.setTotal(pageResume.getTotalElements());

        rs.setMeta(mt);

        List<ResFetchResumeDTO> listResume = pageResume.getContent()
                .stream().map(item -> this.getResume(item)).collect(Collectors.toList())

        ;

        rs.setResult(listResume);

        return rs;
    }

    public ResultPaginationDTO fetchResumeByUser(Pageable pageable) {
        // query builder
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        FilterNode node = filterParser.parse("email='" + email + "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageResume.getTotalPages());
        mt.setTotal(pageResume.getTotalElements());
        rs.setMeta(mt);
        // remove sensitive data
        List<ResFetchResumeDTO> listResume = pageResume.getContent()
                .stream().map(item -> this.getResume(item))
                .collect(Collectors.toList());
        rs.setResult(listResume);
        return rs;
    }

}
