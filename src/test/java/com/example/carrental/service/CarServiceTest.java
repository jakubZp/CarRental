package com.example.carrental.service;


import com.example.carrental.model.Car;
import com.example.carrental.repository.CarRepository;
import com.example.carrental.repository.PriceUpdateRepository;
import net.bytebuddy.implementation.bytecode.Throw;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CarServiceTest {

    @Mock
    private CarRepository carRepository;
    @Mock
    private PriceUpdateRepository priceUpdateRepository;
    private CarService underTest;

    @BeforeEach
    void setup() {
        underTest = new CarService(carRepository, priceUpdateRepository);
    }

    @Test
    public void should_getAllCars() {
        // when
        underTest.getAllCars(0, 10);

        // then
        Mockito.verify(carRepository).findAllCars(Mockito.any(PageRequest.class));
    }

    @Test
    public void should_getSingleCar_whenCarIdIsCorrect() {
        // given
        Car car = new Car(1L, "toyota", "yaris", 2023,
                  new BigDecimal(100), null, null);

        long carId = car.getId();
        Mockito.when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        // when
        Car result = underTest.getSingleCar(carId);

        // then
        Mockito.verify(carRepository).findById(carId);
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
        Mockito.verify(carRepository).findById(carId);
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
        Mockito.verify(carRepository)
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
        Mockito.verify(carRepository).deleteById(carId);
    }

    @Test
    public void should_updateExistingCar() {
        // given
        Car car = new Car(1L, "toyota", "yaris", 2023,
                new BigDecimal(100), null, null);

        Car updatedCar = new Car(null, "ford", "focus", 2020,
                new BigDecimal(150), null, null);

        long carId = car.getId();
        Mockito.when(carRepository.findById(carId)).thenReturn(Optional.of(car));

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

}