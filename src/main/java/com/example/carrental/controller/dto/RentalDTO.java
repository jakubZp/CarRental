package com.example.carrental.controller.dto;

import lombok.Getter;

import java.time.LocalDateTime;

// TODO change on record ?
@Getter
public class RentalDTO {
    LocalDateTime fromDate;
    LocalDateTime toDate;
    Long carId;
    Long personId;
}
