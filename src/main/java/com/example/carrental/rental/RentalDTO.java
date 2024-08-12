package com.example.carrental.rental;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;


public record RentalDTO(
        Long rentalId,
        @NotNull(message = "fromDate cannot be null")
        LocalDateTime fromDate,
        @NotNull(message = "toDate cannot be null")
        LocalDateTime toDate,
        @NotNull(message = "carId cannot be null")
        Long carId,
        @NotNull(message = "customerId cannot be null")
        Long customerId) implements Comparable<RentalDTO>{

    @Override
    public int compareTo(RentalDTO r) {
        if(fromDate.isAfter(r.fromDate()))
            return 1;
        else if(fromDate.isBefore(r.fromDate()))
            return -1;
        else
            return 0;
    }
}
