package com.example.carrental.rental;

import com.example.carrental.car.Car;
import com.example.carrental.car.CarRepository;
import com.example.carrental.config.EnableTestcontainers;
import com.example.carrental.customer.Customer;
import com.example.carrental.customer.CustomerRepository;
import com.example.carrental.rental.Rental;
import com.example.carrental.rental.RentalRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@EnableTestcontainers
class RentalRepositoryIT {

    @Autowired
    private RentalRepository underTest;
    @Autowired
    CarRepository carRepository;
    @Autowired
    private CustomerRepository customerRepository;

    private Car car = Car.builder().build();
    private Customer customer = Customer.builder().build();

    @BeforeEach
    void setup() {
        carRepository.save(car);
        customerRepository.save(customer);
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
        carRepository.deleteAll();
        underTest.deleteAll();
    }

    @Test
    public void shouldReturn_EmptyList_whenDatesNotOverlapped() {
        // given
        Rental r1 = Rental.builder()
                .fromDate(LocalDateTime.parse("2023-05-10T10:00"))
                .toDate(LocalDateTime.parse("2023-05-20T10:00"))
                .build();
        underTest.save(r1);

        // when
        List<Rental> result = underTest.findByCarAndDatesOverlap(
                car,
                LocalDateTime.parse("2023-05-21T10:00"),
                LocalDateTime.parse("2023-05-27T10:00")
        );

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturn_NotEmptyList_whenDatesOverlapped() {
        // given
        Rental r1 = new Rental(1L,
                LocalDateTime.parse("2023-05-10T10:00"),
                LocalDateTime.parse("2023-05-20T10:00"),
                car, customer);
        underTest.save(r1);

        // when
        List<Rental> result = underTest.findByCarAndDatesOverlap(
                car,
                LocalDateTime.parse("2023-05-12T10:00"),
                LocalDateTime.parse("2023-05-18T10:00")
        );

        // then
        assertFalse(result.isEmpty());
    }

    @Test
    public void shouldReturn_NotEmptyList_whenDatesExactlyOverlapped() {
        // given
        Rental r1 = new Rental(1L,
                LocalDateTime.parse("2023-05-10T10:00"),
                LocalDateTime.parse("2023-05-20T10:00"),
                car, customer);
        underTest.save(r1);

        // when
        List<Rental> result = underTest.findByCarAndDatesOverlap(
                car,
                LocalDateTime.parse("2023-05-10T10:00"),
                LocalDateTime.parse("2023-05-20T10:00")
        );

        // then
        assertFalse(result.isEmpty());
    }

    @Test
    public void should_findAllRentals(){
        // given
        Rental r1 = new Rental(1L,
                LocalDateTime.parse("2023-05-10T10:00"),
                LocalDateTime.parse("2023-05-20T10:00"),
                car, customer);
        underTest.save(r1);

        // when
        List<Rental> rentals = underTest.findAllRentals();

        // then
        assertThat(rentals).hasSize(underTest.findAll().size());
    }

}