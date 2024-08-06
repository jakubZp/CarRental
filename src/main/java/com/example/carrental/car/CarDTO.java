package com.example.carrental.car;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

public record CarDTO(
        Long carId,
        @NotNull(message = "brand cannot be null")
        @Length(min = 1, message = "brand length should be greater than 0")
        String brand,
        @NotNull(message = "model cannot be null")
        @Length(min = 1, message = "model length should be greater than 0")
        String model,
        @NotNull(message = "productionYear cannot be null")
        Integer productionYear,
        @NotNull(message = "actualDailyPrice cannot be null")
        @Positive(message = "actual daily price must be positive")
        BigDecimal actualDailyPrice) {
}
