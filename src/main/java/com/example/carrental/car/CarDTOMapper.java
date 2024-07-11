package com.example.carrental.car;

import org.springframework.stereotype.Service;

@Service
public class CarDTOMapper{

    public CarDTO mapToDTO(Car car) {
        return new CarDTO(
                car.getId(),
                car.getBrand(),
                car.getModel(),
                car.getProductionYear(),
                car.getActualDailyPrice()
        );
    }

    public Car mapToCar(CarDTO carDTO) {
        return new Car(
                carDTO.carId(),
                carDTO.brand(),
                carDTO.model(),
                carDTO.productionYear(),
                carDTO.actualDailyPrice(),
                null,
                null
        );
    }
}
