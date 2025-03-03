package com.example.jobhunter.controller;

import com.example.jobhunter.domain.DTO.ResultPaginationDTO;
import com.example.jobhunter.domain.Skill;
import com.example.jobhunter.service.SkillService;
import com.example.jobhunter.utils.annotation.APIMessage;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("${api.version}")
public class SkillController {
    private SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @APIMessage("Create a new skill")
    public ResponseEntity<Skill> create(@RequestBody Skill skill) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(skillService.createSkill(skill));
    }

    @PutMapping("/skills")
    @APIMessage("Update skill")
    public ResponseEntity<Skill> update(@RequestBody Skill skill) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(skillService.updateSkill(skill));
    }

    @GetMapping("/skills")
    @APIMessage("Get all skill with pagination and filter")
    public ResponseEntity<ResultPaginationDTO> getAllSkills(
            @Filter Specification<Skill> specification,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(skillService.getAllSkills(specification, pageable));
    }

    @DeleteMapping("/skills/{id}")
    @APIMessage("delete skill")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(skillService.delete(id));
    }
}
