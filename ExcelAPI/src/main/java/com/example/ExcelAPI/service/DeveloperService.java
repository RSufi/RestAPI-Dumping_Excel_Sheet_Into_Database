package com.example.ExcelAPI.service;

import java.io.InputStream;
import java.util.List;

import com.example.ExcelAPI.model.entity.DeveloperEntity;


import org.springframework.web.multipart.MultipartFile;

public interface DeveloperService {
    
    // public List<Developer> importExcelFile(MultipartFile files);

    public  boolean checkExcelFormat(MultipartFile file);

    public  List<DeveloperEntity> convertExcelToListOfDeveloper(InputStream is);

    public void save(MultipartFile file);

    public List<DeveloperEntity> getAllDevelopers();
}
