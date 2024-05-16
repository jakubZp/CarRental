package com.example.carrental.rental;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


record RentalDTO(Long rentalId,
                 LocalDateTime fromDate,
                 LocalDateTime toDate,
                 Long carId,
                 Long customerId){
}
