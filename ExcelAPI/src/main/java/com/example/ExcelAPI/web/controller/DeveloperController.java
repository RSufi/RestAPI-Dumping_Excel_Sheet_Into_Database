package com.example.ExcelAPI.web.controller;

import java.util.List;
import java.util.Map;

import com.example.ExcelAPI.model.entity.DeveloperEntity;

import com.example.ExcelAPI.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin("*")
public class DeveloperController {
    @Autowired
    private ServiceImpl serviceImpl;

    @PostMapping("/developer/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        if (serviceImpl.checkExcelFormat(file)) {
            // true

            this.serviceImpl.save(file);

            return ResponseEntity.ok(Map.of("message", "File is uploaded and data is saved to db"));

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload excel file ");
    }

    @GetMapping("/developer")
    public List<DeveloperEntity> getAllDevelopers() {
        return this.serviceImpl.getAllDevelopers();
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> getFile() {
        String filename = "developers.xlsx";
        InputStreamResource file = new InputStreamResource(serviceImpl.load());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = " + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
}
