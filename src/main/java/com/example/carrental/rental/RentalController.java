package com.example.carrental.rental;

import jakarta.validation.Valid;
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
        return rentalService.getAllRentals()
                .stream()
                .map(rentalDTOMapper)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public RentalDTO getSingleRental(@PathVariable long id) {
        return rentalDTOMapper.apply(
                rentalService.getSingleRental(id)
        );
    }

    @PostMapping
    public RentalDTO addRental(@Valid @RequestBody RentalDTO rentalDTO) {
        return rentalDTOMapper.apply(rentalService.addRental(rentalDTO));
    }

    @DeleteMapping("/{id}")
    public void deleteRental(@PathVariable long id) {
        rentalService.deleteRental(id);
    }
}
