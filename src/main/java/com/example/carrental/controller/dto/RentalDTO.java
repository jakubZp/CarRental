package com.example.carrental.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

// TODO change on record ?
@Getter
@AllArgsConstructor
public class RentalDTO {
    private Long rentalId;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private Long carId;
    private Long customerId;
}
