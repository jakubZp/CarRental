package com.example.carrental.repository;

import com.example.carrental.model.Car;
import com.example.carrental.model.PriceUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest(properties = "application-test.properties")
class PriceUpdateRepositoryTest {

    @Autowired
    private PriceUpdateRepository underTest;

    @Test
    public void shouldReturnCorrectPrice_forCarIdAndDate() {
        // given
        Car car = new Car(1L, "toyota", "yaris", 2023, new BigDecimal(100), null, null);
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
        assertThat(result.getPrice()).isEqualTo(BigDecimal.valueOf(130));

    }
}