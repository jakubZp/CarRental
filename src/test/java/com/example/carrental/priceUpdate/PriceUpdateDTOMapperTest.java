package com.example.carrental.priceUpdate;

import com.example.carrental.car.Car;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PriceUpdateDTOMapperTest {

    private final PriceUpdateDTOMapper underTest = new PriceUpdateDTOMapper();

    @Test
    public void should_mapPriceUpdate_toDTO() {
        // given
        PriceUpdate priceUpdate = PriceUpdate.builder()
                .id(1L)
                .price(BigDecimal.valueOf(200))
                .updateDate(LocalDateTime.now())
                .car(Car.builder().id(1L).build())
                .build();

        // when
        PriceUpdateDTO result = underTest.apply(priceUpdate);

        // then
        assertThat(result).isInstanceOf(PriceUpdateDTO.class);
    }
}