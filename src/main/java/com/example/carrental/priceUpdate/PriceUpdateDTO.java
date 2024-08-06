package com.example.carrental.priceUpdate;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class PriceUpdateDTO {
    @NotNull(message = "updateDate cannot be null")
    private LocalDateTime updateDate;
    @NotNull(message = "price cannot be null")
    private BigDecimal price;
    @NotNull(message = "carId cannot be null")
    private Long carId;
}
