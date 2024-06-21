package com.example.carrental.service;

import com.example.carrental.car.Car;
import com.example.carrental.car.CarRepository;
import com.example.carrental.priceUpdate.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PriceUpdateServiceTest {

    @Mock private PriceUpdateRepository priceUpdateRepository;
    private final PriceUpdateDTOMapper priceUpdateDTOMapper = new PriceUpdateDTOMapper();
    @Mock private CarRepository carRepository;
    @InjectMocks private PriceUpdateService underTest;
    private final Integer page = 0;
    private final Integer pageSize = 10;
    private final Pageable pageable = Mockito.mock(Pageable.class);

    @Test
    public void should_getPriceUpdates() {
        // given
        final Page<PriceUpdate> expectedResult = new PageImpl<>(List.of(new PriceUpdate()), pageable, 1);
        when(priceUpdateRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(expectedResult);

        // when
        final Page<PriceUpdate> result = underTest.getPriceUpdates(page, pageSize);

        // then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void should_addPriceUpdate() {
        // given
        Car c = Car.builder()
                .id(1L)
                .actualDailyPrice(BigDecimal.valueOf(100))
                .build();
        PriceUpdateDTO newPriceUpdate = PriceUpdateDTO.builder()
                .updateDate(LocalDateTime.now())
                .price(BigDecimal.valueOf(200))
                .carId(c.getId())
                .build();
        when(carRepository.findById(1L)).thenReturn(Optional.of(c));

        // when
        underTest.addPriceUpdate(newPriceUpdate);

        // then
        ArgumentCaptor<PriceUpdate> priceUpdateArgumentCaptor = ArgumentCaptor.forClass(PriceUpdate.class);
        verify(priceUpdateRepository)
                .save(priceUpdateArgumentCaptor.capture());
        PriceUpdate captured = priceUpdateArgumentCaptor.getValue();
        PriceUpdateDTO result = priceUpdateDTOMapper.apply(captured);

        assertThat(newPriceUpdate).isEqualTo(result);
    }

    @Test
    public void should_addPriceUpdateThrowException_whenCarWithIdDoesNotExists() {
        // given
        Long carId = 1L;
        PriceUpdateDTO newPriceUpdate = PriceUpdateDTO.builder()
                .updateDate(LocalDateTime.now())
                .price(BigDecimal.valueOf(200))
                .carId(carId)
                .build();

        // when
        Throwable thrown = Assertions.catchThrowable(() -> {
            underTest.addPriceUpdate(newPriceUpdate);
        });

        // then
        String message = "car with id " + carId + " does not exists";
        assertThat(thrown).hasMessage(message);
    }

    @Test
    public void should_addPriceUpdateThrowException_whenPricesAreSame() {
        // given
        Long carId = 1L;
        Car c = Car.builder()
                .id(carId)
                .actualDailyPrice(BigDecimal.valueOf(200))
                .build();
        PriceUpdateDTO newPriceUpdate = PriceUpdateDTO.builder()
                .updateDate(LocalDateTime.now())
                .price(BigDecimal.valueOf(200))
                .carId(carId)
                .build();
        when(carRepository.findById(carId)).thenReturn(Optional.of(c));

        // when
        Throwable thrown = Assertions.catchThrowable(() -> {
            underTest.addPriceUpdate(newPriceUpdate);
        });

        // then
        String message = "new price is same as actual price of car " + carId;
        assertThat(thrown).hasMessage(message);
    }

    @Test
    public void should_findPriceOnDate() {
        // given
        long carId = 1L;
        LocalDateTime date = LocalDateTime.parse("2024-05-23T10:00");
        BigDecimal expectedPrice = BigDecimal.valueOf(200);
        when(priceUpdateRepository.findFirstByCar_IdAndUpdateDateBeforeOrderByUpdateDateDesc(carId, date))
                .thenReturn(Optional.ofNullable(PriceUpdate.builder()
                        .price(expectedPrice)
                        .build()));

        // when
        BigDecimal result = underTest.findPriceOnDate(carId, date);

        // then
        assertThat(result).isEqualTo(expectedPrice);
    }

    @Test
    public void should_findPriceOnDate_throwException_whenCannotFindPrice() {
        // given
        long carId = 1L;
        LocalDateTime date = LocalDateTime.parse("2024-05-23T10:00");

        // when
        Throwable thrown = Assertions.catchThrowable(() -> {
            underTest.findPriceOnDate(carId, date);
        });

        // then
        String message = "cannot find price for car with id " + carId + " on date " + date;
        assertThat(thrown).hasMessage(message);
    }
}