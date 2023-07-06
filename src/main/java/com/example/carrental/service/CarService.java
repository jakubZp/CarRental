package com.example.carrental.service;

import com.example.carrental.model.Car;
import com.example.carrental.model.PriceUpdate;
import com.example.carrental.repository.CarRepository;
import com.example.carrental.repository.PriceUpdateRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final PriceUpdateRepository priceUpdateRepository;

    public List<Car> getAllCars(int pageNumber, int size) {
        return carRepository.findAllCars(PageRequest.of(pageNumber, size));
    }

    public Car getSingleCar(long id) {
        return carRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("car with id " + id + " does not exists!")
        );
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @Transactional
    public Car addCar(Car car) {
        PriceUpdate priceUpdate = new PriceUpdate(
                null, LocalDateTime.now(), car.getActualDailyPrice(), car);
        priceUpdateRepository.save(priceUpdate);

        return carRepository.save(car);
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public void deleteCar(long id) {
        carRepository.deleteById(id);
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @Transactional
    public Car updateCar(long id, Car updatedCar) {
        Car c = carRepository.findById(id).orElseThrow(() -> {
            throw new IllegalStateException("car with id " + id + " does not exists! Cannot update.");
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

            PriceUpdate priceUpdate = new PriceUpdate(null, LocalDateTime.now(), newActualDailyPrice, c);
            priceUpdateRepository.save(priceUpdate);
        }

        return c;
    }
}
