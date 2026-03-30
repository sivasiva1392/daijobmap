package com.daijobmap.demo.repositories;

import com.daijobmap.demo.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    
    Optional<Category> findByName(String name);
    
    List<Category> findByStatus(Boolean status);
    
    @Query("SELECT c FROM Category c WHERE c.status = true ORDER BY c.name")
    List<Category> findActiveCategoriesOrderByName();
    
    boolean existsByName(String name);
}
