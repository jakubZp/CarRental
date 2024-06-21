package com.example.carrental.rental.report;

import com.example.carrental.rental.Rental;
import com.example.carrental.priceUpdate.PriceUpdateService;
import com.example.carrental.rental.RentalService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportExcelService {

    private final PriceUpdateService priceUpdateService;
    private final RentalService rentalService;
    private BigDecimal earnedMoney = BigDecimal.ZERO;

    public void generateRentalsReport(List<Rental> rentals, HttpServletResponse response) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Report");

        String[] header = {"Rental ID", "From date", "To date", "Car ID", "Customer ID", "Days", "Daily price", "Earning"};
        Row headerRow = sheet.createRow(0);
        for(int i=0; i < header.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(header[i]);
        }

        CellStyle dateCellStyle = workbook.createCellStyle();
        CreationHelper creationHelper = workbook.getCreationHelper();
        dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd"));

        Collections.sort(rentals);
        for(int i=0; i<rentals.size(); i++) {
            Row dataRow = sheet.createRow(i+1);
            Rental current = rentals.get(i);
            dataRow.createCell(0).setCellValue(current.getId());

            Cell fromDateCell = dataRow.createCell(1);
            fromDateCell.setCellValue(current.getFromDate());
            fromDateCell.setCellStyle(dateCellStyle);
            Cell toDateCell = dataRow.createCell(2);
            toDateCell.setCellValue(current.getToDate());
            toDateCell.setCellStyle(dateCellStyle);

            dataRow.createCell(3).setCellValue(current.getCar().getId());
            dataRow.createCell(4).setCellValue(current.getCustomer().getId());
            dataRow.createCell(5).setCellValue(Duration.between(current.getFromDate(), current.getToDate()).toDays());
            BigDecimal dailyPrice = priceUpdateService.findPriceOnDate(
                    current.getCar().getId(), current.getFromDate()
            );
            dataRow.createCell(6, CellType.NUMERIC).setCellValue(dailyPrice.doubleValue());
            BigDecimal currentEarning = rentalService.calculateEarning(current);
            dataRow.createCell(7, CellType.NUMERIC).setCellValue(currentEarning.doubleValue());
            earnedMoney = earnedMoney.add(currentEarning);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String headerKey = "Content-Disposition";
        LocalDateTime fromDate = rentals.get(0).getFromDate();
        LocalDateTime toDate = rentals.get(rentals.size()-1).getToDate();
        String headerValue = "attachment; filename=rentals" + ".xlsx";
        response.setHeader(headerKey, headerValue);

        // TODO try with resources
        try {
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("Cannot save report in .xlsx file.");
        }
    }
}
