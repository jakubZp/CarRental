package com.example.carrental.repository;

import com.example.carrental.model.Car;
import com.example.carrental.model.Person;
import com.example.carrental.model.Rental;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(properties = "application-test.properties")
class RentalRepositoryTest {

    @Autowired
    private RentalRepository underTest;

    private Car car;
    private Person person;

    @BeforeEach
    void setup() {
        // TODO mockito.any() or just mock it ??
        car = new Car(1L, "toyota", "yaris", 2023, new BigDecimal(100), null, null);
        person = new Person(1L, "Tom", "Smith", "123456789", "Warsaw", "tom@gmail.com", ".ZB~q1%53sjp", null);
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    public void shouldReturn_EmptyList_whenDatesNotOverlapped() {
        // given
        Rental r1 = new Rental(1L,
                LocalDateTime.parse("2023-05-10T10:00"),
                LocalDateTime.parse("2023-05-20T10:00"),
                car,
                person);
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
                car, person);
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
                car, person);
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
                car, person);
        underTest.save(r1);

        // when
        List<Rental> rentals = underTest.findAllRentals();

        // then
        assertThat(rentals).isEqualTo(underTest.findAll());
    }

}