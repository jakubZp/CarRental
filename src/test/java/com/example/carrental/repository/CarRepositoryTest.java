package com.example.carrental.repository;

import com.example.carrental.car.Car;
import com.example.carrental.car.CarRepository;
import com.example.carrental.customer.Customer;
import com.example.carrental.customer.CustomerRepository;
import com.example.carrental.rental.Rental;
import com.example.carrental.rental.RentalRepository;
import com.example.carrental.user.Role;
import com.example.carrental.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

//TODO delete application-test.properties
@DataJpaTest(properties = "application-test.properties")
@ActiveProfiles("test")
class CarRepositoryTest {

    @Autowired
    private CarRepository underTest;
    @Autowired
    private RentalRepository rentalRepository;

    private PageRequest page;

    @BeforeEach
    void setup() {
        page = PageRequest.of(0,10);
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    public void should_returnAvailableCar_when_newRentalDates_notOverlapped() {
        // given
        Rental r1 = Rental.builder()
                        .fromDate(LocalDateTime.parse("2023-05-10T10:00"))
                        .toDate(LocalDateTime.parse("2023-05-20T10:00"))
                        .build();
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
        Rental r1 = Rental.builder()
                        .fromDate(LocalDateTime.parse("2023-05-10T10:00"))
                        .toDate(LocalDateTime.parse("2023-05-20T10:00"))
                        .build();
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
        Rental r1 = Rental.builder()
                        .fromDate(LocalDateTime.parse("2023-05-10T10:00"))
                        .toDate(LocalDateTime.parse("2023-05-20T10:00"))
                        .build();
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
        Rental r1 = Rental.builder()
                        .fromDate(LocalDateTime.parse("2023-05-10T10:00"))
                        .toDate(LocalDateTime.parse("2023-05-20T10:00"))
                        .build();
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
        Rental r1 = Rental.builder()
                .fromDate(LocalDateTime.parse("2023-05-10T10:00"))
                .toDate(LocalDateTime.parse("2023-05-20T10:00"))
                .build();
        rentalRepository.save(r1);
        rentalRepository.save(r1);

        // when
        LocalDateTime fromDate = LocalDateTime.parse("2023-05-05T10:00");
        LocalDateTime toDate = LocalDateTime.parse("2023-05-25T10:00");
        List<Car> result = underTest.findAvailableCarsBetweenDates(fromDate, toDate, page);

        // then
        assertTrue(result.isEmpty());
    }

}