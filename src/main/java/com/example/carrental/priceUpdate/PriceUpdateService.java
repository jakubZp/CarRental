package com.example.carrental.priceUpdate;

import com.example.carrental.car.Car;
import com.example.carrental.car.CarRepository;
import com.example.carrental.exceptions.CannotFindPriceException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PriceUpdateService {

    private final PriceUpdateRepository priceUpdateRepository;
    private final CarRepository carRepository;
    private final PriceUpdateDTOMapper priceUpdateDTOMapper;

    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
    public Page<PriceUpdate> getPriceUpdates(Integer page, Integer size) {
        int pageNumber = page != null && page > 0 ? page : 0;
        int pageSize   = size != null && size > 0 ? size : 10;
        return priceUpdateRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
    public PriceUpdate addPriceUpdate(PriceUpdateDTO priceUpdateDTO) {
        Long carId = priceUpdateDTO.getCarId();
        Car c = carRepository.findById(carId).orElseThrow(() -> {
            throw new IllegalStateException("car with id " + carId + " does not exists");
        });

        if(Objects.equals(priceUpdateDTO.getPrice(), c.getActualDailyPrice())) {
            throw new IllegalStateException("new price is same as actual price of car " + carId);
        }

        PriceUpdate newPrice = new PriceUpdate(
                null,
                priceUpdateDTO.getUpdateDate(),
                priceUpdateDTO.getPrice(),
                c);

        if (c.getPriceUpdates() == null) {
            c.setPriceUpdates(new ArrayList<>());
        }
        c.getPriceUpdates().add(newPrice);

        return priceUpdateRepository.save(newPrice);
    }

    public BigDecimal findPriceOnDate(Long carId, LocalDateTime date) {
        PriceUpdate result = priceUpdateRepository.findFirstByCar_IdAndUpdateDateBeforeOrderByUpdateDateDesc(carId, date)
                .orElseThrow(() -> new CannotFindPriceException(
                "cannot find price for car with id " + carId + " on date " + date));
        return result.getPrice();
    }
}
