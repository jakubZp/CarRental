package com.example.carrental.service;

import com.example.carrental.controller.dto.RentalDTO;
import com.example.carrental.controller.mapper.RentalDTOMapper;
import com.example.carrental.model.Car;
import com.example.carrental.model.Customer;
import com.example.carrental.model.Rental;
import com.example.carrental.model.User;
import com.example.carrental.repository.CarRepository;
import com.example.carrental.repository.CustomerRepository;
import com.example.carrental.repository.RentalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock private RentalRepository rentalRepository;
    @Mock private CarRepository carRepository;
    @Mock private CustomerRepository customerRepository;
    private RentalDTOMapper rentalDTOMapper;
    private RentalService underTest;
    private Car car;
    private User user;
    private Customer customer;

    @BeforeEach
    void setup() {
        underTest = new RentalService(rentalRepository, carRepository, customerRepository);
        car = new Car(1L, "toyota", "yaris", 2023, new BigDecimal(100), null, null);
        user = new User(1L, null, null, "Tom", "Smith", "123456789", "Warsaw", "tom@gmail.com", "zaq1");
        customer = new Customer();
        customer.setUser(user);
        rentalDTOMapper = new RentalDTOMapper();
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
        Rental r1 = new Rental(null,
                LocalDateTime.parse("2023-05-10T10:00"),
                LocalDateTime.parse("2023-05-20T10:00"),
                car, customer
                );

        // when
        RentalDTO r1DTO = rentalDTOMapper.apply(r1);
        underTest.addRental(r1DTO);

        // then
        ArgumentCaptor<Rental> rentalArgumentCaptor =
                ArgumentCaptor.forClass(Rental.class);

        Mockito.verify(rentalRepository).save(r1);

        Rental capturedRental = rentalArgumentCaptor.getValue();
    }
}