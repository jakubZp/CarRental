package com.example.carrental.controller;

import com.example.carrental.model.Rental;
import com.example.carrental.service.RentalService;
import com.example.carrental.service.report.ReportExcelService;
import com.example.carrental.service.report.ReportPDFService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/rentals")
@RequiredArgsConstructor
public class ReportController {

    private final RentalService rentalService;
    private final ReportPDFService reportPDF;
    private final ReportExcelService reportExcel;

    @GetMapping("/pdf/all")
    public void generatePdfForAllRentals(HttpServletResponse response) {

        List<Rental> rentals = rentalService.getAllRentals();
        reportPDF.generateRentalsReport(rentals, response);
    }

    @GetMapping("/pdf/{fromDate}/{toDate}")
    public void generatePdfForRentalsPeriod(@PathVariable String fromDate,
                                            @PathVariable String toDate,
                                            HttpServletResponse response) {
        LocalDateTime startDate = LocalDateTime.parse(fromDate);
        LocalDateTime endDate = LocalDateTime.parse(toDate);
        List <Rental> rentals = rentalService.getRentalsBetweenDates(startDate, endDate);
        reportPDF.generateRentalsReport(rentals, response);
    }

    @GetMapping("/excel/all")
    public void generateExcelForAllRentals(HttpServletResponse response) {
        List<Rental> rentals = rentalService.getAllRentals();
        reportExcel.generateRentalsReport(rentals, response);
    }

    @GetMapping("/excel/{fromDate}/{toDate}")
    public void generateExcelForRentalsPeriod(@PathVariable String fromDate,
                                              @PathVariable String toDate,
                                              HttpServletResponse response) {
        LocalDateTime startDate = LocalDateTime.parse(fromDate);
        LocalDateTime endDate = LocalDateTime.parse(toDate);
        List<Rental> rentals = rentalService.getRentalsBetweenDates(startDate, endDate);
        reportExcel.generateRentalsReport(rentals, response);
    }
}
