package com.example.carrental.controller.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class PriceUpdateDTO {
    private LocalDateTime updateDate;
    private BigDecimal price;
    private Long carId;
}
