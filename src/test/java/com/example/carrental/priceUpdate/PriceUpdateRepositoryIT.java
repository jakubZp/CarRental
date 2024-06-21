package com.example.carrental.priceUpdate;

import com.example.carrental.car.Car;
import com.example.carrental.car.CarRepository;
import com.example.carrental.config.EnableTestcontainers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@EnableTestcontainers
class PriceUpdateRepositoryIT {

    @Autowired
    private PriceUpdateRepository underTest;
    @Autowired
    CarRepository carRepository;
    private final Car car = Car.builder()
                            .actualDailyPrice(BigDecimal.valueOf(100))
                            .build();

    @BeforeEach
    public void setup() {
        carRepository.save(car);
    }

    @AfterEach
    public void afterEach() {
        underTest.deleteAll();
        carRepository.deleteAll();
    }


    @Test
    public void shouldReturnCorrectPrice_forCarIdAndDate() {
        // given
        PriceUpdate priceUpdate1 = new PriceUpdate(1L, LocalDateTime.parse("2023-06-20T10:00"), BigDecimal.valueOf(150), car);
        PriceUpdate priceUpdate2 = new PriceUpdate(2L, LocalDateTime.parse("2023-06-25T10:00"), BigDecimal.valueOf(130), car);
        PriceUpdate priceUpdate3 = new PriceUpdate(3L, LocalDateTime.parse("2023-06-28T18:10"), BigDecimal.valueOf(100), car);
        underTest.save(priceUpdate1);
        underTest.save(priceUpdate2);
        underTest.save(priceUpdate3);

        // when
        PriceUpdate result = underTest.findFirstByCar_IdAndUpdateDateBeforeOrderByUpdateDateDesc(
                car.getId(), LocalDateTime.parse("2023-06-27T10:00")).orElse(null);

        // then
        assert result != null;
        assertThat(result.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(130));

    }
}