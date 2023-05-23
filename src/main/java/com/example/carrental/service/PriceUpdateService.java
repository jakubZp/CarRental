package com.example.carrental.service;

import com.example.carrental.controller.dto.PriceUpdateDTO;
import com.example.carrental.model.Car;
import com.example.carrental.model.PriceUpdate;
import com.example.carrental.repository.CarRepository;
import com.example.carrental.repository.PriceUpdateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class PriceUpdateService {
    private final PriceUpdateRepository priceUpdateRepository;
    private final CarRepository carRepository;

    public List<PriceUpdate> getPriceUpdates() {
        return priceUpdateRepository.findAll();
    }

    public PriceUpdate addPriceUpdate(PriceUpdateDTO priceUpdate) {
        PriceUpdate newPrice = new PriceUpdate();
        Long carId = priceUpdate.getCarId();;
        Car c = carRepository.findById(carId).orElseThrow(() -> {
            throw new IllegalStateException("car with id " + carId + " does not exists");
        });

        if(Objects.equals(priceUpdate.getPrice(), c.getActualDailyPrice())) {
            throw new IllegalStateException("new price is same as actual price of car " + carId);
        }

        newPrice.setCar(c);
        newPrice.setUpdateDate(priceUpdate.getUpdateDate());
        newPrice.setPrice(priceUpdate.getPrice());

        c.getPriceUpdates().add(newPrice);

        return priceUpdateRepository.save(newPrice);
    }
}
