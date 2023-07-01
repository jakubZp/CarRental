package com.example.carrental.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "Rental")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rental implements Comparable<Rental>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;

    @ManyToOne
    private Car car;

    @ManyToOne
    private Customer customer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rental rental = (Rental) o;
        return fromDate.equals(rental.fromDate) && toDate.equals(rental.toDate) && car.equals(rental.car) && customer.equals(rental.customer);
        //TODO?? id.equals(rental.id): it should be true but its better without it because id is unique and with it objects won't be equals anytime
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fromDate, toDate, car, customer);
    }

    @Override
    public int compareTo(Rental rental) {
        if(fromDate.isAfter(rental.fromDate))
            return 1;
        else if(fromDate.isBefore(rental.fromDate))
            return -1;
        else
            return 0;
    }
}
