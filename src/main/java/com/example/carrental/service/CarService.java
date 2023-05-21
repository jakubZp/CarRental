package com.example.carrental.service;

import com.example.carrental.model.Car;
import com.example.carrental.repository.CarRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CarService {
    private final CarRepository carRepository;

    public List<Car> getAllCars() {
        return carRepository.findAllCars();
    }

    public Car getSingleCar(long id) {
        return carRepository.findById(id).orElseThrow();
    }

    public Car addCar(Car car) {
        return carRepository.save(car);
    }

    public void deleteCar(long id) {
        carRepository.deleteById(id);
    }
}
