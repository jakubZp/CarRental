package com.example.carrental.service;

import com.example.carrental.controller.dto.PriceUpdateDTO;
import com.example.carrental.model.Car;
import com.example.carrental.model.PriceUpdate;
import com.example.carrental.repository.CarRepository;
import com.example.carrental.repository.PriceUpdateRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PriceUpdateService {

    private final PriceUpdateRepository priceUpdateRepository;
    private final CarRepository carRepository;

    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
    public List<PriceUpdate> getPriceUpdates() {
        return priceUpdateRepository.findAll();
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
    public PriceUpdate addPriceUpdate(PriceUpdateDTO priceUpdate) {
        Long carId = priceUpdate.getCarId();
        Car c = carRepository.findById(carId).orElseThrow(() -> {
            throw new IllegalStateException("car with id " + carId + " does not exists");
        });

        if(Objects.equals(priceUpdate.getPrice(), c.getActualDailyPrice())) {
            throw new IllegalStateException("new price is same as actual price of car " + carId);
        }

        PriceUpdate newPrice = new PriceUpdate(
                null,
                priceUpdate.getUpdateDate(),
                priceUpdate.getPrice(),
                c);

        c.getPriceUpdates().add(newPrice);

        return priceUpdateRepository.save(newPrice);
    }

    public Optional<BigDecimal> findPriceOnDate(Long carId, LocalDateTime date) {
        return priceUpdateRepository.findFirstByCar_IdAndUpdateDateBeforeOrderByUpdateDateDesc(carId, date)
                .map(PriceUpdate::getPrice);
    }
}
