package com.example.carrental.car;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CarDTOMapper implements Function<Car, CarDTO> {
    @Override
    public CarDTO apply(Car car) {
        return new CarDTO(
                car.getId(),
                car.getBrand(),
                car.getModel(),
                car.getProductionYear(),
                car.getActualDailyPrice()
        );
    }
}
