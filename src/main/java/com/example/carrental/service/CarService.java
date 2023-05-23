package com.example.carrental.service;

import com.example.carrental.controller.dto.PriceUpdateDTO;
import com.example.carrental.model.Car;
import com.example.carrental.model.Person;
import com.example.carrental.model.PriceUpdate;
import com.example.carrental.repository.CarRepository;
import com.example.carrental.repository.PriceUpdateRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    private final PriceUpdateRepository priceUpdateRepository;

    public List<Car> getAllCars() {
        return carRepository.findAllCars();
    }

    public Car getSingleCar(long id) {
        return carRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Car addCar(Car car) {
        PriceUpdate priceUpdate = new PriceUpdate();
        priceUpdate.setPrice(car.getActualDailyPrice());
        priceUpdate.setUpdateDate(LocalDateTime.now());
        priceUpdate.setCar(car);
        priceUpdateRepository.save(priceUpdate);

        return carRepository.save(car);
    }

    public void deleteCar(long id) {
        carRepository.deleteById(id);
    }

    @Transactional
    public Car updateCar(long id, Car updatedCar) {
        Car c = carRepository.findById(id).orElseThrow(() -> {
            throw new IllegalStateException("car with id \" + id + \" does not exists! Cannot update.");
        });

        String newBrand = updatedCar.getBrand();
        if(newBrand != null && newBrand.length() > 0 && !Objects.equals(c.getBrand(), newBrand)) {
            c.setBrand(newBrand);
        }

        String newModel = updatedCar.getModel();
        if(newModel != null && newModel.length() > 0 && !Objects.equals(c.getModel(), newModel)) {
            c.setModel(newModel);
        }

        Integer newProductionYear = updatedCar.getProductionYear();
        if(newProductionYear != null && newProductionYear > 0 && !Objects.equals(c.getProductionYear(), newProductionYear)) {
            c.setProductionYear(newProductionYear);
        }

        BigDecimal newActualDailyPrice = updatedCar.getActualDailyPrice();
        if(newActualDailyPrice != null && !Objects.equals(c.getActualDailyPrice(), newActualDailyPrice)) {
            c.setActualDailyPrice(newActualDailyPrice);

            PriceUpdate priceUpdate = new PriceUpdate();
            priceUpdate.setPrice(newActualDailyPrice);
            priceUpdate.setUpdateDate(LocalDateTime.now());
            priceUpdate.setCar(c);
            priceUpdateRepository.save(priceUpdate);
        }

        return c;
    }
}
