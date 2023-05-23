package com.example.carrental.service;

import com.example.carrental.controller.dto.RentalDTO;
import com.example.carrental.model.Car;
import com.example.carrental.model.Person;
import com.example.carrental.model.Rental;
import com.example.carrental.repository.CarRepository;
import com.example.carrental.repository.PersonRepository;
import com.example.carrental.repository.RentalRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class RentalService {
    private RentalRepository rentalRepository;
    private CarRepository carRepository;
    private PersonRepository personRepository;

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public Rental addRental(RentalDTO rentalDTO) {
        Rental rental = new Rental();

        Car c = carRepository.findById(rentalDTO.getCarId()).orElseThrow(() -> {
            throw new IllegalStateException("car with id " + rentalDTO.getCarId() + " does not exists.");
        });

        Person p = personRepository.findById(rentalDTO.getPersonId()).orElseThrow(() -> {
            throw new IllegalStateException("person with id " + rentalDTO.getPersonId() + " does not exists.");
        });

        LocalDateTime fromDate = rentalDTO.getFromDate();
        LocalDateTime toDate = rentalDTO.getToDate();
        List<Rental> overlappingRentals = rentalRepository.findByCarAndDatesOverlap(c, fromDate, toDate);
        if(!overlappingRentals.isEmpty()) {
            throw new IllegalStateException("The car is already booked for the selected dates.");
        }

        if(fromDate.isAfter(toDate)) {
            throw new IllegalStateException("from date is after to date");
        }

        rental.setFromDate(fromDate);
        rental.setToDate(toDate);
        rental.setCar(c);
        rental.setPerson(p);

        c.getCarRents().add(rental);
        p.getPersonRents().add(rental);

        return rentalRepository.save(rental);
        //return rental;
    }

    public void deleteRental(long id) {
        if(!rentalRepository.existsById(id)){
            throw new IllegalStateException("rental with id " + id + " does not exists");
        }

        rentalRepository.deleteById(id);
    }

    public Rental getSingleRental(long id) {
        return rentalRepository.findById(id).orElseThrow(() -> {
            throw new IllegalStateException("rental with id "+ id + " does not exists");
        });
    }
}
