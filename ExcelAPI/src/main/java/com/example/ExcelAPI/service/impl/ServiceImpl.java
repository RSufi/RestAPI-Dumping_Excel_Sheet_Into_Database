package com.example.ExcelAPI.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.example.ExcelAPI.model.entity.DeveloperEntity;

import com.example.ExcelAPI.model.repository.DeveloperRepository;
import com.example.ExcelAPI.service.DeveloperService;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ServiceImpl implements DeveloperService {
    @Autowired
    DeveloperRepository developerRepository;

    @Override
    public boolean checkExcelFormat(MultipartFile file) {

        String contentType = file.getContentType();

        if (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<DeveloperEntity> convertExcelToListOfProduct(InputStream is) {
        List<DeveloperEntity> list = new ArrayList<>();
        try {

            XSSFWorkbook workbook = new XSSFWorkbook(is);

            XSSFSheet sheet = workbook.getSheet("Sheet1");

            int rowNumber = 0;
            Iterator<Row> iterator = sheet.iterator();
            while (iterator.hasNext()) {
                Row row = iterator.next();

                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cells = row.iterator();

                int cid = 0;

                DeveloperEntity d = new DeveloperEntity();

                while (cells.hasNext()) {
                    Cell cell = cells.next();

                    switch (cid) {
                        case 0:
                            d.setId((int) cell.getNumericCellValue());
                            break;
                        case 1:
                            d.setFirstName(cell.getStringCellValue());
                            break;
                        case 2:
                            d.setLastName(cell.getStringCellValue());
                            break;
                        case 3:
                            d.setDesignation(cell.getStringCellValue());
                            break;
                        case 4:
                            d.setPhoneNumber((long) cell.getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    cid++;
                }
                list.add(d);
            }
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void save(MultipartFile file) {

        try {
            List<DeveloperEntity> developers = convertExcelToListOfProduct(file.getInputStream());
            this.developerRepository.saveAll(developers);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<DeveloperEntity> getAllProducts() {
        return this.developerRepository.findAll();
    }

    // @Override
    // public List<Developer> importExcelFile(MultipartFile files) {

    // return null;
    // }

}
