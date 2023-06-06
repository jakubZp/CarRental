package com.example.carrental.service;

import com.example.carrental.controller.dto.RentalDTO;
import com.example.carrental.model.Car;
import com.example.carrental.model.Person;
import com.example.carrental.model.Rental;
import com.example.carrental.repository.CarRepository;
import com.example.carrental.repository.PersonRepository;
import com.example.carrental.repository.RentalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock private RentalRepository rentalRepository;
    @Mock private CarRepository carRepository;
    @Mock private PersonRepository personRepository;
    private RentalService underTest;
    private Car car;
    private Person person;

    @BeforeEach
    void setup() {
        underTest = new RentalService(rentalRepository, carRepository, personRepository);
        car = new Car(1L, "toyota", "yaris", 2023, new BigDecimal(100), null, null);
        person = new Person(1L, "Tom", "Smith", "123456789", "Warsaw", "tom@gmail.com", "zaq1", null);
    }

    @Test
    public void should_GetAllRentals() {
        // when
        underTest.getAllRentals();
        // then
        Mockito.verify(rentalRepository).findAllRentals();
    }

    @Test
    @Disabled
    public void should_AddNewRental() {
        // given
        Rental r1 = new Rental(1L,
                LocalDateTime.parse("2023-05-10T10:00"),
                LocalDateTime.parse("2023-05-20T10:00"),
                car, person
                );
        // when
        //underTest.addRental(r1); // TODO convert/map to DTO

        // then
        ArgumentCaptor<Rental> rentalArgumentCaptor =
                ArgumentCaptor.forClass(Rental.class);

        //Mockito.verify(rentalRepository).save()

        Rental capturedRental = rentalArgumentCaptor.getValue();
    }
}