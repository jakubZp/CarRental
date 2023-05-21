package com.example.carrental.controller;

import com.example.carrental.model.Rental;
import com.example.carrental.service.RentalService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
