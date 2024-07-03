package com.example.carrental.car;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;
    private final CarDTOMapper carDTOMapper;

    @GetMapping()
    public List<CarDTO> getCars(@RequestParam(required = false) Integer page,
                             @RequestParam(required = false) Integer pageSize) {
        return carService.getAllCars(page, pageSize)
                .stream()
                .map(carDTOMapper)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CarDTO getSingleCar(@PathVariable long id) {
        return carDTOMapper.apply(carService.getSingleCar(id));
    }

    @PostMapping
    public CarDTO addCar(@RequestBody Car car) {//TODO DTO
        return carDTOMapper.apply(carService.addCar(car));
    }

    @DeleteMapping("/{id}")//TODO cannot delete car with rentals - document it
    public void deleteCar(@PathVariable long id) {
        carService.deleteCar(id);
    }

    @PutMapping("/{id}")
    public CarDTO updateCar(@PathVariable long id,
                          @RequestBody Car updatedCar) {
        return carDTOMapper.apply(carService.updateCar(id, updatedCar));
    }

    @GetMapping("/{from}/{to}")
    public List<CarDTO> getAvailableCarsBetweenDates(@PathVariable LocalDateTime from,
                                                  @PathVariable LocalDateTime to,
                                                  @RequestParam(required = false) Integer page,
                                                  @RequestParam(required = false) Integer pageSize) {
        return carService.getAvailableCarsBetweenDates(from, to, page, pageSize)
                .stream()
                .map(carDTOMapper)
                .collect(Collectors.toList());
    }
}