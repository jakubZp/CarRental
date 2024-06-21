package com.example.carrental.rental;

import com.example.carrental.car.Car;
import com.example.carrental.customer.Customer;
import com.example.carrental.car.CarRepository;
import com.example.carrental.customer.CustomerRepository;
import com.example.carrental.priceUpdate.PriceUpdateService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RentalService {

    private RentalRepository rentalRepository;
    private CarRepository carRepository;
    private CustomerRepository customerRepository;
    private PriceUpdateService priceUpdateService;

    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
    public List<Rental> getAllRentals() {
        return rentalRepository.findAllRentals();
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
    public Rental getSingleRental(long id) {
        return rentalRepository.findById(id).orElseThrow(() -> {
            throw new IllegalStateException("rental with id "+ id + " does not exists");
        });
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN', 'CUSTOMER')")
    public Rental addRental(RentalDTO rentalDTO) {
        Car c = carRepository.findById(rentalDTO.carId()).orElseThrow(() -> {
            throw new IllegalStateException("car with id " + rentalDTO.carId() + " does not exists.");
        });

        Customer p = customerRepository.findById(rentalDTO.customerId()).orElseThrow(() -> {
            throw new IllegalStateException("customer with id " + rentalDTO.customerId() + " does not exists.");
        });

        LocalDateTime fromDate = rentalDTO.fromDate();
        LocalDateTime toDate = rentalDTO.toDate();
        List<Rental> overlappingRentals = rentalRepository.findByCarAndDatesOverlap(c, fromDate, toDate);
        if(!overlappingRentals.isEmpty()) {
            throw new IllegalStateException("The car is already booked for the selected dates.");
        }

        if(fromDate.isAfter(toDate)) {
            throw new IllegalStateException("from date is after to date.");
        }

        Rental rental = new Rental(null, fromDate, toDate, c, p);
        c.getCarRents().add(rental);
        p.getCustomerRents().add(rental);

        return rentalRepository.save(rental);
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')") //|| (#id == authentication.principal.customer.id && @rentalRepository.findById(#id).orElse(null).getFromDate().isAfter(LocalDateTime.now())) ")// TODO if user is owner of this rental and fromDate isAfter now we can delete rental
    public void deleteRental(long id) {
        if(!rentalRepository.existsById(id)){
            throw new IllegalStateException("rental with id " + id + " does not exists");
        }

        rentalRepository.deleteById(id);
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
    public List<Rental> getRentalsBetweenDates(LocalDateTime fromDate, LocalDateTime toDate) {
        return rentalRepository.findAllByFromDateBetween(fromDate, toDate);
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
    public BigDecimal calculateEarning(Rental r) {
        BigDecimal result;
        long days = Duration.between(r.getFromDate(), r.getToDate()).toDays();
        BigDecimal price = priceUpdateService.findPriceOnDate(r.getCar().getId(), r.getFromDate());

        result = BigDecimal.valueOf(days).multiply(price);

        return result;
    }

}
