package com.daijobmap.demo.controllers;

import com.daijobmap.demo.entities.Job;
import com.daijobmap.demo.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
public class JobController {
    
    @Autowired
    private JobService jobService;
    
    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        List<Job> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Integer id) {
        Optional<Job> job = jobService.getJobById(id);
        return job.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Job>> getJobsByCategory(@PathVariable Integer categoryId) {
        try {
            List<Job> jobs = jobService.getJobsByCategoryId(categoryId);
            return ResponseEntity.ok(jobs);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/location/{location}")
    public ResponseEntity<List<Job>> getJobsByLocation(@PathVariable String location) {
        List<Job> jobs = jobService.getJobsByLocation(location);
        return ResponseEntity.ok(jobs);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Job>> searchJobsByTitle(@RequestParam String title) {
        List<Job> jobs = jobService.searchJobsByTitle(title);
        return ResponseEntity.ok(jobs);
    }
    
    @GetMapping("/category/{categoryId}/location/{location}")
    public ResponseEntity<List<Job>> getJobsByCategoryAndLocation(
            @PathVariable Integer categoryId, 
            @PathVariable String location) {
        try {
            List<Job> jobs = jobService.getJobsByCategoryAndLocation(categoryId, location);
            return ResponseEntity.ok(jobs);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/active-categories")
    public ResponseEntity<List<Job>> getJobsByActiveCategories() {
        List<Job> jobs = jobService.getJobsByActiveCategories();
        return ResponseEntity.ok(jobs);
    }
    
    @GetMapping("/recent")
    public ResponseEntity<List<Job>> getRecentJobs(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        List<Job> jobs = jobService.getRecentJobs(startDate);
        return ResponseEntity.ok(jobs);
    }
    
    @GetMapping("/posted-after")
    public ResponseEntity<List<Job>> getJobsPostedAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Job> jobs = jobService.getJobsPostedAfter(date);
        return ResponseEntity.ok(jobs);
    }
    
    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        try {
            Job createdJob = jobService.createJob(job);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdJob);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Integer id, @RequestBody Job jobDetails) {
        try {
            Job updatedJob = jobService.updateJob(id, jobDetails);
            return ResponseEntity.ok(updatedJob);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Integer id) {
        try {
            jobService.deleteJob(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Integer id) {
        boolean exists = jobService.existsById(id);
        return ResponseEntity.ok(exists);
    }
}
