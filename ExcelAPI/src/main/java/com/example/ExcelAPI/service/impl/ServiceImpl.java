package com.example.ExcelAPI.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
    public List<DeveloperEntity> convertExcelToListOfDeveloper(InputStream is) {
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
            List<DeveloperEntity> developers = convertExcelToListOfDeveloper(file.getInputStream());
            this.developerRepository.saveAll(developers);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<DeveloperEntity> getAllDevelopers() {
        return this.developerRepository.findAll();
    }

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = { "id", "FirstName", "LastName", "designation", "PhoneNumber" };
    static String SHEET = "DeveloperEntity";

    public static ByteArrayInputStream developersToExcel(List<DeveloperEntity> developers){
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);
            // Header
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < HEADERs.length; col++) {
              Cell cell = headerRow.createCell(col);
              cell.setCellValue(HEADERs[col]);
            }
            int rowIdx = 1;
            for (DeveloperEntity developer : developers) {
              Row row = sheet.createRow(rowIdx++);
              row.createCell(0).setCellValue(developer.getId());
              row.createCell(1).setCellValue(developer.getFirstName());
              row.createCell(2).setCellValue(developer.getLastName());
              row.createCell(3).setCellValue(developer.getDesignation());
              row.createCell(4).setCellValue(developer.getPhoneNumber());
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
          } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
          }

    }

    public ByteArrayInputStream load() {
        List<DeveloperEntity> developers = developerRepository.findAll();
        ByteArrayInputStream in = ServiceImpl.developersToExcel(developers);
        return in;
      }

}
