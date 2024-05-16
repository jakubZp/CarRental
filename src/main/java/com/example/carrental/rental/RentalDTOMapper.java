package com.example.carrental.rental;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class RentalDTOMapper implements Function<Rental, RentalDTO> {
    @Override
    public RentalDTO apply(Rental rental) {
        return new RentalDTO(
                rental.getId(),
                rental.getFromDate(),
                rental.getToDate(),
                rental.getCar().getId(),
                rental.getCustomer().getId()
        );
    }
}
