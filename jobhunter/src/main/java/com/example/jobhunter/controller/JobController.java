package com.example.jobhunter.controller;


import com.example.jobhunter.domain.DTO.Response.job.ResCreateJob;
import com.example.jobhunter.domain.DTO.Response.job.ResUpdateJob;
import com.example.jobhunter.domain.DTO.ResultPaginationDTO;
import com.example.jobhunter.domain.Job;
import com.example.jobhunter.service.JobService;
import com.example.jobhunter.utils.annotation.APIMessage;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.version}")
public class JobController {
    private JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @APIMessage("Create a new job")
    public ResponseEntity<ResCreateJob> createJob(@RequestBody Job job) {
        return ResponseEntity.ok(jobService.createJob(job));
    }

    @PutMapping("/jobs")
    @APIMessage("Update job")
    public ResponseEntity<ResUpdateJob> updateJob(@RequestBody Job job) {
        return ResponseEntity.ok(jobService.updateJob(job));
    }

    @DeleteMapping("/jobs/{id}")
    @APIMessage("Delete job")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(null);
    }

    @GetMapping("/jobs/{id}")
    @APIMessage("Get job by id")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    @GetMapping("/jobs")
    @APIMessage("Get all job with filter and pagination")
    public ResponseEntity<ResultPaginationDTO> getAllJobs(
            @Filter Specification<Job> specification,
            Pageable pageable
            ){
        return ResponseEntity.status(HttpStatus.OK).body(jobService.getAllJobs(specification, pageable));
    }
}
