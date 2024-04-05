package com.example.carrental.service;

import com.example.carrental.car.Car;
import com.example.carrental.customer.Customer;
import com.example.carrental.priceUpdate.PriceUpdateService;
import com.example.carrental.rental.RentalDTO;
import com.example.carrental.rental.RentalDTOMapper;
import com.example.carrental.rental.Rental;
import com.example.carrental.rental.RentalService;
import com.example.carrental.car.CarRepository;
import com.example.carrental.customer.CustomerRepository;
import com.example.carrental.rental.RentalRepository;
import com.example.carrental.user.Role;
import com.example.carrental.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private CarRepository carRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PriceUpdateService priceUpdateService;
    private RentalDTOMapper rentalDTOMapper;
    private RentalService underTest;
    private Car car;
    private User user;
    private Customer customer;
    private Rental rental;

    @BeforeEach
    void setup() {
        underTest = new RentalService(rentalRepository, carRepository, customerRepository, priceUpdateService);
        car = new Car(1L, "toyota", "yaris", 2023, new BigDecimal(100), new ArrayList<>(), null);
        user = new User(1L, null, null, "Tom", "Smith", "123456789", "Warsaw", "tom@gmail.com", "zaq1", Role.CUSTOMER, null);
        customer = new Customer();
        customer.setUser(user);
        customer.setCustomerRents(new ArrayList<>());
        rentalDTOMapper = new RentalDTOMapper();

        rental = new Rental(1L,
                LocalDateTime.parse("2023-05-10T10:00"),
                LocalDateTime.parse("2023-05-20T10:00"),
                car, customer
        );
    }

    @Test
    public void should_getAllRentals() {
        // when
        underTest.getAllRentals();
        // then
        Mockito.verify(rentalRepository).findAllRentals();
    }

    @Test
    public void should_getSingleRental_whenRentalIdCorrect() {
        // given
        long rentalId = rental.getId();
        Mockito.when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));

        // when
        Rental result = underTest.getSingleRental(rentalId);

        // then
        assertThat(result).isEqualTo(rental);
    }

    @Test
    public void should_throwException_whenGetSingleRentalIdIncorrect() {
        // given
        long rentalId = 5L;
        Mockito.when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));

        // when
        Rental result = underTest.getSingleRental(rentalId);

        // then
        assertThat(result).isEqualTo(rental);
    }

    @Test
    public void should_addNewRental() {
        // given
        Mockito.when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        Mockito.when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        Mockito.when(rentalRepository.findByCarAndDatesOverlap(car, rental.getFromDate(), rental.getToDate())).thenReturn(List.of());

        // when
        RentalDTO rentalDTO = rentalDTOMapper.apply(rental);
        underTest.addRental(rentalDTO);

        // then
        ArgumentCaptor<Rental> rentalArgumentCaptor =
                ArgumentCaptor.forClass(Rental.class);

        Mockito.verify(rentalRepository).save(rentalArgumentCaptor.capture());

        Rental capturedRental = rentalArgumentCaptor.getValue();
        assertThat(capturedRental).isEqualTo(rental);
    }

    @Test
    public void should_throwException_whenAddRentalAndCarIdNotExists() {
        // when
        RentalDTO rentalDTO = rentalDTOMapper.apply(rental);
        Throwable thrown = Assertions.catchThrowable(() -> {
           underTest.addRental(rentalDTO);
        });

        // then
        String message = "car with id " + rental.getCar().getId() + " does not exists.";
        assertThat(thrown).hasMessage(message);
    }

    @Test
    public void should_throwException_whenAddRentalAndCustomerIdNotExists() {
        // given
        Mockito.when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));

        // when
        RentalDTO rentalDTO = rentalDTOMapper.apply(rental);
        Throwable thrown = Assertions.catchThrowable(() -> {
            underTest.addRental(rentalDTO);
        });

        // then
        String message = "customer with id " + rental.getCustomer().getId() + " does not exists.";
        assertThat(thrown).hasMessage(message);
    }

    @Test
    public void should_throwException_whenAddRentalAndOverlappingRentalsIsNotEmpty() {
        // given
        Mockito.when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        Mockito.when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        Mockito.when(rentalRepository.findByCarAndDatesOverlap(car, rental.getFromDate(), rental.getToDate()))
                .thenReturn(List.of(new Rental(), new Rental()));

        // when
        RentalDTO rentalDTO = rentalDTOMapper.apply(rental);
        Throwable thrown = Assertions.catchThrowable(() -> {
            underTest.addRental(rentalDTO);
        });

        // then
        String message = "The car is already booked for the selected dates.";
        assertThat(thrown).hasMessage(message);
    }

    @Test
    public void should_throwException_whenAddRentalAndFromDateIsAfterToDate() {
        // given
        rental.setFromDate(LocalDateTime.parse("2023-05-20T10:00"));
        rental.setToDate(LocalDateTime.parse("2023-05-10T10:00"));

        Mockito.when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        Mockito.when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        Mockito.when(rentalRepository.findByCarAndDatesOverlap(car, rental.getFromDate(), rental.getToDate()))
                .thenReturn(List.of());

        // when
        RentalDTO rentalDTO = rentalDTOMapper.apply(rental);
        Throwable thrown = Assertions.catchThrowable(() -> {
            underTest.addRental(rentalDTO);
        });

        // then
        String message = "from date is after to date.";
        assertThat(thrown).hasMessage(message);
    }

    @Test
    public void should_deleteRental_whenRentalIdExists() {
        // given
        RentalDTO rentalDTO = rentalDTOMapper.apply(rental);
        long rentalId = rental.getId();
        Mockito.when(rentalRepository.existsById(rentalId)).thenReturn(true);

        Mockito.when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        Mockito.when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        Mockito.when(rentalRepository.findByCarAndDatesOverlap(car, rental.getFromDate(), rental.getToDate())).thenReturn(List.of());

        // when
        underTest.addRental(rentalDTO);
        underTest.deleteRental(rentalId);

        // then
        Mockito.verify(rentalRepository).deleteById(rentalId);
    }

    @Test
    public void should_throwException_whenDeleteRentalIdNotExists() {
        // given
        RentalDTO rentalDTO = rentalDTOMapper.apply(rental);
        long rentalId = rental.getId();

        Mockito.when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        Mockito.when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        Mockito.when(rentalRepository.findByCarAndDatesOverlap(car, rental.getFromDate(), rental.getToDate())).thenReturn(List.of());

        // when
        underTest.addRental(rentalDTO);
        Throwable thrown = Assertions.catchThrowable(() -> {
            underTest.deleteRental(rentalId);
        });

        // then
        String message = "rental with id " + rentalId + " does not exists";
        assertThat(thrown).hasMessage(message);
    }

    @Test
    public void should_calculateEarningCorrectly() {
        // given
        Car car = new Car();
        car.setId(1L);
        car.setActualDailyPrice(new BigDecimal(100));
        Rental rental = new Rental(1L,
                LocalDateTime.parse("2023-05-10T10:00"),
                LocalDateTime.parse("2023-05-20T10:00"),
                car, new Customer()
        );
        Mockito.when(priceUpdateService.findPriceOnDate(rental.getCar().getId(), rental.getFromDate()))
                .thenReturn(Optional.of(BigDecimal.valueOf(100)));

        // when
        BigDecimal result = underTest.calculateEarning(rental);

        // then
        BigDecimal expected = new BigDecimal(1000);
        assertThat(result).isEqualByComparingTo(expected);
    }
}