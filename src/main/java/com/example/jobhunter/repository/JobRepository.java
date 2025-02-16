package com.example.jobhunter.repository;

import java.util.List;

import com.example.jobhunter.domain.Job;
import com.example.jobhunter.domain.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Page<Job> findAll(Specification specification, Pageable pageable);

    List<Job> findBySkillsIn(List<Skill> skills);
}
