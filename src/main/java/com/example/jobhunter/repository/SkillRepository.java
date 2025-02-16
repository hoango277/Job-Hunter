package com.example.jobhunter.repository;

import java.util.List;
import java.util.Optional;

import com.example.jobhunter.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;



@Repository
public interface SkillRepository extends JpaRepository<Skill, Long>, JpaSpecificationExecutor<Skill> {
    boolean existsByName(String name);

    Optional findById(long id);

    List<Skill> findByIdIn(List<Long> id);

}
