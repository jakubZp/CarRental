package com.example.carrental.priceUpdate;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PriceUpdateDTO {
    private Long priceUpdateId;
    private LocalDateTime updateDate;
    private BigDecimal price;
    private Long carId;
}
