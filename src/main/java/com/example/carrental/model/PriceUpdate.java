package com.example.carrental.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "price_update")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PriceUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime updateDate;
    private BigDecimal price;
    @ManyToOne
    //@JsonIgnore
    private Car car;
}
