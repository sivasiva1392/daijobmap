package com.daijobmap.demo.services;

import com.daijobmap.demo.entities.Job;
import com.daijobmap.demo.entities.Category;
import com.daijobmap.demo.repositories.JobRepository;
import com.daijobmap.demo.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }
    
    public Optional<Job> getJobById(Integer id) {
        return jobRepository.findById(id);
    }
    
    public List<Job> getJobsByCategoryId(Integer categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new RuntimeException("Category not found with id: " + categoryId);
        }
        return jobRepository.findByCategoryId(categoryId);
    }
    
    public List<Job> getJobsByLocation(String location) {
        return jobRepository.findByLocation(location);
    }
    
    public List<Job> getJobsByCategoryAndLocation(Integer categoryId, String location) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new RuntimeException("Category not found with id: " + categoryId);
        }
        return jobRepository.findByCategoryAndLocation(categoryId, location);
    }
    
    public List<Job> searchJobsByTitle(String title) {
        return jobRepository.findByTitleContaining(title);
    }
    
    public List<Job> getJobsByActiveCategories() {
        return jobRepository.findJobsByActiveCategoriesOrderByPostedDateDesc();
    }
    
    public List<Job> getRecentJobs(LocalDate startDate) {
        return jobRepository.findRecentJobs(startDate);
    }
    
    public List<Job> getJobsPostedAfter(LocalDate date) {
        return jobRepository.findByPostedDateAfter(date);
    }
    
    public Job createJob(Job job) {
        if (job.getCategory() == null || job.getCategory().getId() == null) {
            throw new RuntimeException("Category is required for creating a job");
        }
        
        Optional<Category> category = categoryRepository.findById(job.getCategory().getId());
        if (category.isEmpty()) {
            throw new RuntimeException("Category not found with id: " + job.getCategory().getId());
        }
        
        if (!category.get().getStatus()) {
            throw new RuntimeException("Cannot create job for inactive category");
        }
        
        if (job.getPostedDate() == null) {
            job.setPostedDate(LocalDate.now());
        }
        
        job.setCategory(category.get());
        return jobRepository.save(job);
    }
    
    public Job updateJob(Integer id, Job jobDetails) {
        Optional<Job> optionalJob = jobRepository.findById(id);
        if (optionalJob.isEmpty()) {
            throw new RuntimeException("Job not found with id: " + id);
        }
        
        Job job = optionalJob.get();
        
        if (jobDetails.getCategory() != null && jobDetails.getCategory().getId() != null) {
            Optional<Category> category = categoryRepository.findById(jobDetails.getCategory().getId());
            if (category.isEmpty()) {
                throw new RuntimeException("Category not found with id: " + jobDetails.getCategory().getId());
            }
            if (!category.get().getStatus()) {
                throw new RuntimeException("Cannot assign job to inactive category");
            }
            job.setCategory(category.get());
        }
        
        job.setTitle(jobDetails.getTitle());
        job.setLocation(jobDetails.getLocation());
        job.setPostedDate(jobDetails.getPostedDate());
        
        return jobRepository.save(job);
    }
    
    public void deleteJob(Integer id) {
        if (!jobRepository.existsById(id)) {
            throw new RuntimeException("Job not found with id: " + id);
        }
        jobRepository.deleteById(id);
    }
    
    public boolean existsById(Integer id) {
        return jobRepository.existsById(id);
    }
}
