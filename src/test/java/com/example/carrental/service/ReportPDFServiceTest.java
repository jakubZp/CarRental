package com.example.carrental.service;

import com.example.carrental.model.Car;
import com.example.carrental.model.Customer;
import com.example.carrental.model.Rental;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ReportPDFServiceTest {

    private ReportPDFService underTest;
    @Mock
    private PriceUpdateService priceUpdateService;

    @BeforeEach
    public void setup() {
        underTest = new ReportPDFService(priceUpdateService);
    }

    @Test
    public void should_calculateEarningCorrectly() {
        // given
        Car car = new Car();
        car.setId(1L);
        car.setActualDailyPrice(new BigDecimal(100));
        Rental rental = new Rental(1L,
                LocalDateTime.parse("2023-05-10T10:00"),
                LocalDateTime.parse("2023-05-20T10:00"),
                car, new Customer()
        );
        Mockito.when(priceUpdateService.findPriceOnDate(rental.getCar().getId(), rental.getFromDate()))
                .thenReturn(Optional.of(BigDecimal.valueOf(100)));

        // when
        BigDecimal result = underTest.calculateEarning(rental);

        // then
        BigDecimal expected = new BigDecimal(1000);
        assertThat(result).isEqualByComparingTo(expected);
    }

}