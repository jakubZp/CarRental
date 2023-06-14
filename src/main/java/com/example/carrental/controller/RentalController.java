package com.example.carrental.controller;

import com.example.carrental.controller.dto.RentalDTO;
import com.example.carrental.controller.mapper.RentalDTOMapper;
import com.example.carrental.model.Car;
import com.example.carrental.model.Rental;
import com.example.carrental.service.RentalService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/rentals")
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
    public Rental addRental(@RequestBody RentalDTO rentalDTO) {
        return rentalService.addRental(rentalDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteRental(@PathVariable long id) {
        rentalService.deleteRental(id);
    }
}
