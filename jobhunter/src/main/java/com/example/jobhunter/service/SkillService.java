package com.example.jobhunter.service;

import com.example.jobhunter.domain.DTO.ResultPaginationDTO;
import com.example.jobhunter.domain.Skill;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface SkillService {
    Skill createSkill(Skill skill);

    Skill updateSkill(Skill skill);

    ResultPaginationDTO getAllSkills(Specification<Skill> specification, Pageable pageable);

    Void delete(Long id);
}
