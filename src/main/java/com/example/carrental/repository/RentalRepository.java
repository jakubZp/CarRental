package com.example.carrental.repository;

import com.example.carrental.model.Car;
import com.example.carrental.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    @Query("SELECT r FROM Rental r" +
            "    WHERE ?2 BETWEEN r.fromDate AND r.toDate" +
            "          OR ?3 BETWEEN r.fromDate AND r.toDate" +
            "    AND r.car = ?1")
    List<Rental> findByCarAndDatesOverlap(Car c, LocalDateTime fromDate, LocalDateTime toDate);

    @Query("select r from Rental r" +
            " left join fetch r.car" +
            " left join fetch r.customer")
    List<Rental> findAllRentals();

    List<Rental> findByFromDateBetween(LocalDateTime fromDate, LocalDateTime toDate);
}
