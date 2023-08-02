package com.example.carrental.repository;

import com.example.carrental.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(properties = "application-test.properties")
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CarRepositoryTest {

    @Autowired
    private CarRepository underTest;
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private CustomerRepository customerRepository;

    private Car car;
    private User user;
    private Customer customer;
    private PageRequest page;

    @BeforeEach
    void setup() {
        car = new Car(1L, "toyota", "yaris", 2023, new BigDecimal(100), null, null);
        //TODO add builder not constructor ?
        user = new User(1L, null, null, "Tom", "Smith", "123456789", "Warsaw", "tom@gmail.com", "zaq1", Role.CUSTOMER, null);
        customer = new Customer();
        customer.setUser(user);
        customerRepository.save(customer);
        underTest.save(car);
        page = PageRequest.of(0,10);
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    public void should_returnAvailableCar_when_newRentalDates_notOverlapped() {
        // given
        Rental r1 = new Rental(1L,
                LocalDateTime.parse("2023-05-10T10:00"),
                LocalDateTime.parse("2023-05-20T10:00"),
                car,
                customer);
        rentalRepository.save(r1);

        // when
        LocalDateTime fromDate = LocalDateTime.parse("2023-05-24T10:00");
        LocalDateTime toDate = LocalDateTime.parse("2023-05-30T10:00");
        List<Car> result = underTest.findAvailableCarsBetweenDates(fromDate, toDate, page);

        // then
        assertFalse(result.isEmpty());
    }

    @Test
    public void should_returnEmptyList_when_newRentalDates_ExactlyOverlapped() {
        // given
        Rental r1 = new Rental(1L,
                LocalDateTime.parse("2023-05-10T10:00"),
                LocalDateTime.parse("2023-05-20T10:00"),
                car,
                customer);
        rentalRepository.save(r1);

        // when
        LocalDateTime fromDate = LocalDateTime.parse("2023-05-10T10:00");
        LocalDateTime toDate = LocalDateTime.parse("2023-05-20T10:00");
        List<Car> result = underTest.findAvailableCarsBetweenDates(fromDate, toDate, page);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    public void should_returnEmptyList_when_newRentalFromDate_overlap() {
        // given
        Rental r1 = new Rental(1L,
                LocalDateTime.parse("2023-05-10T10:00"),
                LocalDateTime.parse("2023-05-20T10:00"),
                car,
                customer);
        rentalRepository.save(r1);

        // when
        LocalDateTime fromDate = LocalDateTime.parse("2023-05-15T10:00");
        LocalDateTime toDate = LocalDateTime.parse("2023-05-28T10:00");
        List<Car> result = underTest.findAvailableCarsBetweenDates(fromDate, toDate, page);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    public void should_returnEmptyList_when_newRentalToDate_overlap() {
        // given
        Rental r1 = new Rental(1L,
                LocalDateTime.parse("2023-05-10T10:00"),
                LocalDateTime.parse("2023-05-20T10:00"),
                car,
                customer);
        rentalRepository.save(r1);

        // when
        LocalDateTime fromDate = LocalDateTime.parse("2023-05-05T10:00");
        LocalDateTime toDate = LocalDateTime.parse("2023-05-18T10:00");
        List<Car> result = underTest.findAvailableCarsBetweenDates(fromDate, toDate, page);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    public void should_returnEmptyList_when_newRentalDates_overlapInTheMiddle() {
        // given
        Rental r1 = new Rental(1L,
                LocalDateTime.parse("2023-05-10T10:00"),
                LocalDateTime.parse("2023-05-20T10:00"),
                car,
                customer);
        rentalRepository.save(r1);

        // when
        LocalDateTime fromDate = LocalDateTime.parse("2023-05-05T10:00");
        LocalDateTime toDate = LocalDateTime.parse("2023-05-25T10:00");
        List<Car> result = underTest.findAvailableCarsBetweenDates(fromDate, toDate, page);

        // then
        assertTrue(result.isEmpty());
    }

}