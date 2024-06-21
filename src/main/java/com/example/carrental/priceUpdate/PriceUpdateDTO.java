package com.example.carrental.priceUpdate;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class PriceUpdateDTO {
    private LocalDateTime updateDate;
    private BigDecimal price;
    private Long carId;
}
