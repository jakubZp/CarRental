package com.example.carrental.car;

import com.example.carrental.integrationTestsHelpers.EnableTestcontainers;
import com.example.carrental.rental.Rental;
import com.example.carrental.rental.RentalRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@EnableTestcontainers
@Transactional
class CarRepositoryIT {

    @Autowired
    private CarRepository underTest;
    @Autowired
    private RentalRepository rentalRepository;

    private final PageRequest page = PageRequest.of(0,10);
    private final Car c = Car.builder()
                            .id(1L)
                            .build();

    @BeforeEach
    public void setUp() {
        underTest.save(c);
    }

    @AfterEach
    public void afterEach() {
        rentalRepository.deleteAll();
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

        // when
        LocalDateTime fromDate = LocalDateTime.parse("2023-05-05T10:00");
        LocalDateTime toDate = LocalDateTime.parse("2023-05-25T10:00");
        List<Car> result = underTest.findAvailableCarsBetweenDates(fromDate, toDate, page);

        // then
        assertTrue(result.isEmpty());
    }

}