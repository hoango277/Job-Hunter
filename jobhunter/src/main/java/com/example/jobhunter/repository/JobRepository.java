package com.example.jobhunter.repository;

import com.example.jobhunter.domain.Job;
import com.example.jobhunter.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

    List<Job> findBySkillsIn(List<Skill> listSkills);
}
