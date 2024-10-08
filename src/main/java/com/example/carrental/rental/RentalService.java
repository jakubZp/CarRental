package com.example.carrental.rental;

import com.example.carrental.car.Car;
import com.example.carrental.customer.Customer;
import com.example.carrental.car.CarRepository;
import com.example.carrental.customer.CustomerRepository;
import com.example.carrental.user.UserStatus;
import com.example.carrental.priceUpdate.PriceUpdateService;
import com.example.carrental.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class RentalService {

    private RentalRepository rentalRepository;
    private CarRepository carRepository;
    private CustomerRepository customerRepository;
    private PriceUpdateService priceUpdateService;
    private UserRepository userRepository;

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
    @Transactional
    public Rental addRental(RentalDTO rentalDTO) {
        validateCustomerIdOwnership(rentalDTO);

        Car c = carRepository.findById(rentalDTO.carId()).orElseThrow(() -> {
            throw new IllegalStateException("car with id " + rentalDTO.carId() + " does not exists.");
        });

        Customer customer = customerRepository.findById(rentalDTO.customerId()).orElseThrow(() -> {
            throw new IllegalStateException("customer with id " + rentalDTO.customerId() + " does not exists.");
        });

        if (customer.getUser().getStatus() != UserStatus.ACTIVE) {
            throw new IllegalStateException("cannot create rental for inactive customer.");
        }

        LocalDateTime fromDate = rentalDTO.fromDate();
        LocalDateTime toDate = rentalDTO.toDate();
        if(fromDate == null || toDate == null) {
            throw new IllegalStateException("dates cannot be null.");
        }
        List<Rental> overlappingRentals = rentalRepository.findByCarAndDatesOverlap(c, fromDate, toDate);
        if(!overlappingRentals.isEmpty()) {
            throw new IllegalStateException("the car is already booked for the selected dates.");
        }

        if(fromDate.isAfter(toDate)) {
            throw new IllegalStateException("from date is after to date.");
        }

        Rental rental = new Rental(null, fromDate, toDate, c, customer);
        c.getCarRents().add(rental);
        customer.getCustomerRents().add(rental);

        return rentalRepository.save(rental);
    }

    protected void validateCustomerIdOwnership(RentalDTO rentalDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            boolean isCustomer = authorities.stream()
                    .anyMatch(authority -> "CUSTOMER".equals(authority.getAuthority()));

            if(isCustomer) {
                String email = userDetails.getUsername();
                Long customerId = userRepository.findUserByEmail(email).orElseThrow(() -> {
                    throw new IllegalStateException("customer with email " + email + " does not exists.");
                }).getId();

                if(!customerId.equals(rentalDTO.customerId()))
                    throw new IllegalStateException("customer can only add a rental for himself");
            }
        }
        else
            throw new IllegalStateException("user is not logged in.");
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
        long days = Duration.between(r.getFromDate(), r.getToDate()).toDays();
        BigDecimal price = priceUpdateService.findPriceOnDate(r.getCar().getId(), r.getFromDate());
        return BigDecimal.valueOf(days).multiply(price);
    }

}
