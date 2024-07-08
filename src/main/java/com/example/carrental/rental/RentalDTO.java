package com.example.carrental.rental;

import java.time.LocalDateTime;


public record RentalDTO(Long rentalId,
                 LocalDateTime fromDate,
                 LocalDateTime toDate,
                 Long carId,
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
