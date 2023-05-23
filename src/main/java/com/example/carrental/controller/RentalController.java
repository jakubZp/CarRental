package com.example.carrental.controller;

import com.example.carrental.controller.dto.RentalDTO;
import com.example.carrental.model.Car;
import com.example.carrental.model.Rental;
import com.example.carrental.service.RentalService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/rentals")
public class RentalController {
    private RentalService rentalService;

    @GetMapping()
    public List<Rental> getRentals() {
        return rentalService.getAllRentals();
    }

    @GetMapping("/id")
    public Rental getSingleRental(@PathVariable long id) {
        return rentalService.getSingleRental(id);
    }

    @PostMapping
    public Rental addRental(@RequestBody RentalDTO rentalDTO) {
        return rentalService.addRental(rentalDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteRental(@PathVariable long id) {
        rentalService.deleteRental(id);
    }
}
