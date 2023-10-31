package com.example.carrental.controller;

import com.example.carrental.controller.dto.RentalDTO;
import com.example.carrental.controller.mapper.RentalDTOMapper;
import com.example.carrental.model.Rental;
import com.example.carrental.service.RentalService;
import com.example.carrental.service.report.ReportExcelService;
import com.example.carrental.service.report.ReportPDFService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;
    private final RentalDTOMapper rentalDTOMapper;
//    private final ReportPDFService reportPDF;
//    private final ReportExcelService reportExcel;

    @GetMapping()
    public List<RentalDTO> getRentals() {
        List <RentalDTO> rentals = rentalService.getAllRentals()
                .stream()
                .map(rentalDTOMapper)
                .collect(Collectors.toList());

        return rentals;
    }

    @GetMapping("/{id}")
    public RentalDTO getSingleRental(@PathVariable long id) {
        return rentalDTOMapper.apply(
                rentalService.getSingleRental(id)
        );
    }

    @PostMapping
    public Rental addRental(@RequestBody RentalDTO rentalDTO) {
        return rentalService.addRental(rentalDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteRental(@PathVariable long id) {
        rentalService.deleteRental(id);
    }

//    @GetMapping("/pdf/all")
//    public void generatePdfForAllRentals(HttpServletResponse response) {
//
//        List<Rental> rentals = rentalService.getAllRentals();
//        reportPDF.generateRentalsReport(rentals, response);
//    }
//
//    @GetMapping("/pdf/{fromDate}/{toDate}")
//    public void generatePdfForRentalsPeriod(@PathVariable String fromDate,
//                                            @PathVariable String toDate,
//                                            HttpServletResponse response) {
//        LocalDateTime startDate = LocalDateTime.parse(fromDate);
//        LocalDateTime endDate = LocalDateTime.parse(toDate);
//        List <Rental> rentals = rentalService.getRentalsBetweenDates(startDate, endDate);
//        reportPDF.generateRentalsReport(rentals, response);
//    }
//
//    @GetMapping("/excel/all")
//    public void generateExcelForAllRentals(HttpServletResponse response) {
//        List<Rental> rentals = rentalService.getAllRentals();
//        reportExcel.generateRentalsReport(rentals, response);
//    }
//
//    @GetMapping("/excel/{fromDate}/{toDate}")
//    public void generateExcelForRentalsPeriod(@PathVariable String fromDate,
//                                              @PathVariable String toDate,
//                                              HttpServletResponse response) {
//        LocalDateTime startDate = LocalDateTime.parse(fromDate);
//        LocalDateTime endDate = LocalDateTime.parse(toDate);
//        List<Rental> rentals = rentalService.getRentalsBetweenDates(startDate, endDate);
//        reportExcel.generateRentalsReport(rentals, response);
//    }

}
