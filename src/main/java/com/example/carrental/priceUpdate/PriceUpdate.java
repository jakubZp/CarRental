package com.example.carrental.priceUpdate;

import com.example.carrental.car.Car;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "price_update")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime updateDate;
    private BigDecimal price;

    @ManyToOne
    private Car car;
}
