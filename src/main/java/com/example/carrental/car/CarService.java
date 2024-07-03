package com.example.carrental.car;

import com.example.carrental.exceptions.DateNotValidException;
import com.example.carrental.priceUpdate.PriceUpdate;
import com.example.carrental.priceUpdate.PriceUpdateRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
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

    public Page<Car> getAllCars(Integer page, Integer size) {
        int pageNumber = page != null && page > 0 ? page : 0;
        int pageSize   = size != null && size > 0 ? size : 10;
        return carRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    public Car getSingleCar(long id) {
        return carRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("car with id " + id + " does not exists!")
        );
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @Transactional
    public Car addCar(Car car) {//TODO DTO, validation
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
    public Car updateCar(long id, Car updatedCar) {//TODO DTO
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

    public List<Car> getAvailableCarsBetweenDates(LocalDateTime from, LocalDateTime to, Integer page, Integer pageSize) {
        if(from.isAfter(to))
            throw new DateNotValidException("start date should be after end date");
        return carRepository.findAvailableCarsBetweenDates(from, to, PageRequest.of(page, pageSize));
    }
}
