package com.example.carrental.controller;

import com.example.carrental.model.Car;
import com.example.carrental.service.CarService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/cars")
@AllArgsConstructor
public class CarController {
    private final CarService carService;

    @GetMapping()
    public List<Car> getCars() {
        return carService.getAllCars();
    }

    @GetMapping("/{id}")
    public Car getSingleCar(@PathVariable long id) {
        return carService.getSingleCar(id);
    }

    @PostMapping
    public Car addCar(@RequestBody Car car) {
        return carService.addCar(car);
    }

    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable long id) {
        carService.deleteCar(id);
    }

    @PutMapping("/{id}")
    public Car updateCar(@PathVariable long id,
                          @RequestBody Car updatedCar) {
        return carService.updateCar(id, updatedCar);
    }
}
