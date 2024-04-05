package com.example.carrental.car;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @GetMapping()
    public List<Car> getCars(@RequestParam(required = false) Integer page,
                             @RequestParam(required = false) Integer pageSize) {
        return carService.getAllCars(page, pageSize);
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

    @GetMapping("/{from}/{to}")
    public List<Car> getAvailableCarsBetweenDates(@PathVariable LocalDateTime from,
                                                  @PathVariable LocalDateTime to,
                                                  @RequestParam(required = false) Integer page,
                                                  @RequestParam(required = false) Integer pageSize) {
        return carService.getAvailableCarsBetweenDates(from, to, page, pageSize);
    }
}