package com.example.jobhunter.service.impl;

import com.example.jobhunter.domain.DTO.Meta;
import com.example.jobhunter.domain.DTO.ResultPaginationDTO;
import com.example.jobhunter.domain.Skill;
import com.example.jobhunter.repository.JobRepository;
import com.example.jobhunter.repository.SkillRepository;
import com.example.jobhunter.service.SkillService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@Service
public class SkillServiceImpl implements SkillService {
    private SkillRepository skillRepository;
    private final JobRepository jobRepository;

    public SkillServiceImpl(SkillRepository skillRepository, JobRepository jobRepository) {
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
    }

    @Override
    public Skill createSkill(Skill skill) {
       Optional<Skill> optionalSkill = skillRepository.findByName(skill.getName());
       if (optionalSkill.isPresent()) {
           throw new ResponseStatusException(HttpStatus.CONFLICT, "Skill already exists");
       }

       return skillRepository.save(skill);
    }

    @Override
    public Skill updateSkill(Skill skill) {
        Skill skillToUpdate = skillRepository.findById(skill.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Skill not found"));

        skillToUpdate.setName(skill.getName());
        skillRepository.save(skillToUpdate);
        return skillToUpdate;
    }

    @Override
    public ResultPaginationDTO getAllSkills(Specification<Skill> specification, Pageable pageable) {
        Page<Skill> skills = skillRepository.findAll(specification, pageable);

        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        Meta meta = new Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(skills.getTotalPages());
        meta.setTotal(skills.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(skills.getContent());
        return resultPaginationDTO;
    }

    @Override
    public Void delete(Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Skill not found"));
        skill.getJobs().forEach(j -> j.getSkills().remove(skill));
        skill.getSubscribers().forEach(sub -> sub.getSkills().remove(skill));
        skillRepository.delete(skill);
        return null;
    }
}
