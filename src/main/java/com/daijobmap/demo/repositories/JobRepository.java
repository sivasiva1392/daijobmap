package com.daijobmap.demo.repositories;

import com.daijobmap.demo.entities.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {
    
    List<Job> findByCategoryId(Integer categoryId);
    
    List<Job> findByLocation(String location);
    
    List<Job> findByPostedDateAfter(LocalDate date);
    
    @Query("SELECT j FROM Job j WHERE j.category.id = :categoryId AND j.location = :location")
    List<Job> findByCategoryAndLocation(@Param("categoryId") Integer categoryId, @Param("location") String location);
    
    @Query("SELECT j FROM Job j WHERE j.title LIKE %:title%")
    List<Job> findByTitleContaining(@Param("title") String title);
    
    @Query("SELECT j FROM Job j JOIN j.category c WHERE c.status = true ORDER BY j.postedDate DESC")
    List<Job> findJobsByActiveCategoriesOrderByPostedDateDesc();
    
    @Query("SELECT j FROM Job j WHERE j.postedDate >= :startDate ORDER BY j.postedDate DESC")
    List<Job> findRecentJobs(@Param("startDate") LocalDate startDate);
}
