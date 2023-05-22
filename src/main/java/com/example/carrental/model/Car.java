package com.example.carrental.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity(name = "Car")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    private String brand;
    private String model;
    @Column(name = "production_year")
    private Integer productionYear;
    @Column(name = "actual_daily_price")
    private BigDecimal actualDailyPrice; // TODO table priceUpdate for price changes history, if actual price changes save old price as history

    @OneToMany()
    @JoinColumn(name = "car_id")
    private List<Rental> carRents;
}
