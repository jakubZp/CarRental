package com.example.carrental.car;

import com.example.carrental.priceUpdate.PriceUpdate;
import com.example.carrental.rental.Rental;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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
    private BigDecimal actualDailyPrice;

    @OneToMany(mappedBy = "car")
    @JsonIgnore
    private List<Rental> carRents;

    @OneToMany(mappedBy = "car")
    @JsonIgnore//TODO
    private List<PriceUpdate> priceUpdates;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car c = (Car)o;
        return brand.equals(c.getBrand()) && model.equals(c.getModel()) && productionYear.equals(c.getProductionYear())
                && actualDailyPrice.equals(c.getActualDailyPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand, model, productionYear, actualDailyPrice);
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", productionYear=" + productionYear +
                ", actualDailyPrice=" + actualDailyPrice +
                ", carRents=" + carRents +
                ", priceUpdates=" + priceUpdates +
                '}';
    }
}
