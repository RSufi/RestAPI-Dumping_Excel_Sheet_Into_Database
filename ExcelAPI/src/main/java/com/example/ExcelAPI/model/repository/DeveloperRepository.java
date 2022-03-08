package com.example.ExcelAPI.model.repository;
import com.example.ExcelAPI.model.entity.DeveloperEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DeveloperRepository extends JpaRepository<DeveloperEntity, Integer>{
    
}
