package com.example.carrental.repository;

import com.example.carrental.model.Car;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("select c from Car c")
//            " left join fetch c.carRents")
    List<Car> findAllCars(Pageable page);

    @Query("SELECT c " +
            "FROM Car c " +
            "WHERE c.id NOT IN (" +
            "        SELECT r.car.id " +
            "        FROM Rental r WHERE" +
            "            (r.fromDate BETWEEN ?1 AND ?2" +
            "            OR r.toDate BETWEEN ?1 AND ?2)" +
            "    )")
    List<Car> findAvailableCarsBetweenDates(LocalDateTime fromDate, LocalDateTime toDate, Pageable page);

    // in SQL
    //-- available between 15.03.2023 and 29.03.2023
    //
    //SELECT *
    //FROM car c
    //WHERE NOT EXISTS(
    //        SELECT *
    //        FROM rental r
    //        WHERE r.car_id = c.id
    //            AND (r.from_date BETWEEN '2023-03-15T10:00:00' AND '2023-03-29T10:00:00'
    //                OR r.to_date BETWEEN '2023-03-15T10:00:00' AND '2023-03-29T10:00:00'
    //                  )
    //    );
}
