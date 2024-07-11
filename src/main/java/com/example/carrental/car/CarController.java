package com.example.carrental.car;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
                .map(carDTOMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CarDTO getSingleCar(@PathVariable long id) {
        return carDTOMapper.mapToDTO(carService.getSingleCar(id));
    }

    @PostMapping
    public CarDTO addCar(@Valid @RequestBody CarDTO car) {
        return carDTOMapper.mapToDTO(carService.addCar(car));
    }

    @DeleteMapping("/{id}")//TODO cannot delete car with rentals - document it
    public void deleteCar(@PathVariable long id) {
        carService.deleteCar(id);
    }

    @PutMapping("/{id}")
    public CarDTO updateCar(@PathVariable long id,
                          @Valid @RequestBody CarDTO updatedCar) {
        return carDTOMapper.mapToDTO(carService.updateCar(id, updatedCar));
    }

    @GetMapping("/{from}/{to}")
    public List<CarDTO> getAvailableCarsBetweenDates(@PathVariable LocalDateTime from,
                                                  @PathVariable LocalDateTime to,
                                                  @RequestParam(required = false) Integer page,
                                                  @RequestParam(required = false) Integer pageSize) {
        return carService.getAvailableCarsBetweenDates(from, to, page, pageSize)
                .stream()
                .map(carDTOMapper::mapToDTO)
                .collect(Collectors.toList());
    }
}