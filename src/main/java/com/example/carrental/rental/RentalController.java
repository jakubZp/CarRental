package com.example.carrental.rental;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;
    private final RentalDTOMapper rentalDTOMapper;

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
    public RentalDTO addRental(@RequestBody RentalDTO rentalDTO) {
        return rentalDTOMapper.apply(rentalService.addRental(rentalDTO));
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
