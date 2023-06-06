package com.example.carrental.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

// TODO change on record ?
@Getter
@AllArgsConstructor
public class RentalDTO {
    Long rentalId;
    LocalDateTime fromDate;
    LocalDateTime toDate;
    Long carId;
    Long personId;
}
