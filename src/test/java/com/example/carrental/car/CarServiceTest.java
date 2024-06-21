package com.example.carrental.car;


import com.example.carrental.car.Car;
import com.example.carrental.car.CarRepository;
import com.example.carrental.car.CarService;
import com.example.carrental.priceUpdate.PriceUpdateRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
class CarServiceTest {

    @Mock private CarRepository carRepository;
    @Mock private PriceUpdateRepository priceUpdateRepository;
    @InjectMocks private CarService underTest;
    private final int page = 0;
    private final int pageSize = 10;
    private final Pageable pageable = Mockito.mock(Pageable.class);

    @Test
    public void should_getAllCars() {
        // given
        final Page<Car> findAllResult = new PageImpl<>(List.of(Car.builder().build()), pageable, 1);
        when(carRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(findAllResult);

        // when
        final Page<Car> findCarsResult = underTest.getAllCars(page, pageSize);

        // then
        verify(carRepository).findAll(Mockito.any(PageRequest.class));
        assertThat(findCarsResult).isEqualTo(findAllResult);
    }

    @Test
    public void should_getSingleCar_whenCarIdIsCorrect() {
        // given
        Car car = new Car(1L, "toyota", "yaris", 2023,
                  new BigDecimal(100), null, null);

        long carId = car.getId();
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        // when
        Car result = underTest.getSingleCar(carId);

        // then
        verify(carRepository).findById(carId);
        assertThat(result).isEqualTo(car);
    }

    @Test
    public void should_throwException_whenCarIdIsIncorrect() {
        // given
        long carId = 2L;

        // when
        Throwable thrown = Assertions.catchThrowable(() -> {
            underTest.getSingleCar(carId);
        });

        // then
        String message = "car with id " + carId + " does not exists!";
        Assertions.assertThat(thrown).hasMessage(message);
        verify(carRepository).findById(carId);
    }

    @Test
    public void should_addNewCar() {
        // given
        Car car = new Car(1L, "toyota", "yaris", 2023,
                  new BigDecimal(100), null, null);

        // when
        underTest.addCar(car);

        // then
        ArgumentCaptor<Car> carArgumentCaptor = ArgumentCaptor.forClass(Car.class);
        verify(carRepository)
                .save(carArgumentCaptor.capture());

        Car capturedCar = carArgumentCaptor.getValue();
        assertThat(capturedCar).isEqualTo(car);
    }

    @Test
    public void should_deleteCarById() {
        // given
        Car car = new Car(1L, "toyota", "yaris", 2023,
                new BigDecimal(100), null, null);
        long carId = car.getId();

        // when
        underTest.addCar(car);
        underTest.deleteCar(carId);

        // then
        verify(carRepository).deleteById(carId);
    }

    @Test
    public void should_updateExistingCar() {
        // given
        Car car = new Car(1L, "toyota", "yaris", 2023,
                new BigDecimal(100), null, null);

        Car updatedCar = new Car(null, "ford", "focus", 2020,
                new BigDecimal(150), null, null);

        long carId = car.getId();
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        // when
        Car result = underTest.updateCar(carId, updatedCar);

        // then
        assertThat(result).isEqualTo(updatedCar);
    }

    @Test
    public void should_throwException_whenCarIdForUpdateDoesNotExists() {
        // given
        Car updatedCar = new Car(null, "ford", "focus", 2020,
                new BigDecimal(150), null, null);
        long carId = 4L;

        // when
        Throwable thrown = Assertions.catchThrowable(() -> {
            underTest.updateCar(carId, updatedCar);
        });

        // then
        String message = "car with id " + carId + " does not exists! Cannot update.";
        assertThat(thrown).hasMessage(message);
    }

    @Test
    public void should_throwException_whenFromDateIsAfterToDate() {
        // given
        LocalDateTime fromDate = LocalDateTime.parse("2023-05-20T10:00");
        LocalDateTime toDate = LocalDateTime.parse("2023-05-10T10:00");

        // when
        Throwable thrown = Assertions.catchThrowable(() -> {
            underTest.getAvailableCarsBetweenDates(fromDate, toDate, page, pageSize);
        });

        // then
        String message = "start date should be after end date";
        assertThat(thrown).hasMessage(message);
    }

    @Test
    public void should_returnAvailableCars_betweenDates() {
        // given
        LocalDateTime fromDate = LocalDateTime.parse("2023-05-10T10:00");
        LocalDateTime toDate = LocalDateTime.parse("2023-05-20T10:00");
        final PageRequest pageRequest = PageRequest.of(page, pageSize);
        final List<Car> findAvailableResult = List.of(new Car());
        when(carRepository.findAvailableCarsBetweenDates(fromDate, toDate, pageRequest))
                .thenReturn(findAvailableResult);

        // when
        List<Car> result = underTest.getAvailableCarsBetweenDates(fromDate, toDate, page, pageSize);

        // then
        assertThat(result).isEqualTo(findAvailableResult);
    }

}