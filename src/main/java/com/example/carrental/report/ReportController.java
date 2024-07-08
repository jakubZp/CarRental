package com.example.carrental.report;

import com.example.carrental.rental.Rental;
import com.example.carrental.rental.RentalService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final RentalService rentalService;
    private final ReportFactoryProvider reportFactoryProvider;
    private ReportFactory reportFactory;

    @GetMapping("/{format}/all")
    public void generatePdfForAllRentals(@PathVariable String format,
                                         HttpServletResponse response) {
        List<Rental> rentals = rentalService.getAllRentals();
        reportFactory = reportFactoryProvider.getReportFactory(format);
        reportFactory.generateReportForRentals(rentals, response);
    }

    @GetMapping("/{format}/{fromDate}/{toDate}")
    public void generatePdfForRentalsPeriod(@PathVariable String format,
                                            @PathVariable String fromDate,
                                            @PathVariable String toDate,
                                            HttpServletResponse response) {
        LocalDateTime startDate = LocalDateTime.parse(fromDate);
        LocalDateTime endDate = LocalDateTime.parse(toDate);
        List <Rental> rentals = rentalService.getRentalsBetweenDates(startDate, endDate);
        reportFactory = reportFactoryProvider.getReportFactory(format);
        reportFactory.generateReportForRentals(rentals, response);
    }

}
