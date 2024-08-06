package com.example.carrental.rental;

import com.example.carrental.car.Car;
import com.example.carrental.car.CarRepository;
import com.example.carrental.customer.Customer;
import com.example.carrental.customer.CustomerRepository;
import com.example.carrental.priceUpdate.PriceUpdateService;
import com.example.carrental.user.Role;
import com.example.carrental.user.User;
import com.example.carrental.user.UserRepository;
import com.example.carrental.user.UserStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
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
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class RentalServiceTest {

    @Mock private RentalRepository rentalRepository;
    @Mock private CarRepository carRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private PriceUpdateService priceUpdateService;
    @Mock private UserRepository userRepository;
    private final RentalDTOMapper rentalDTOMapper = new RentalDTOMapper();
    @InjectMocks
    private RentalService underTest = new RentalService(rentalRepository, carRepository, customerRepository, priceUpdateService, userRepository) {
        @Override
        protected void validateCustomerIdOwnership(RentalDTO rentalDTO) {
        }
    };
    private Car car;
    private User user;
    private Customer customer;
    private Rental rental;

    @BeforeEach
    void setup() {
        car = new Car(1L, "toyota", "yaris", 2023, new BigDecimal(100), new ArrayList<>(), null);
        user = new User(1L, null, null, "Tom", "Smith", "123456789", "Warsaw", "tom@gmail.com", "zaq1", Role.CUSTOMER, null, UserStatus.ACTIVE);
        customer = Customer.builder()
                        .user(user)
                        .customerRents(new ArrayList<>())
                        .build();

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
        verify(rentalRepository).findAllRentals();
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
        underTest.addRental(rentalDTOMapper.apply(rental));

        // then
        ArgumentCaptor<Rental> rentalArgumentCaptor =
                ArgumentCaptor.forClass(Rental.class);

        verify(rentalRepository).save(rentalArgumentCaptor.capture());

        Rental capturedRental = rentalArgumentCaptor.getValue();
        assertThat(capturedRental).isEqualTo(rental);
    }

    @Test
    public void should_throwException_whenAddRentalAndCarIdNotExists() {
        // when
        Throwable thrown = Assertions.catchThrowable(() -> {
           underTest.addRental(rentalDTOMapper.apply(rental));
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
        Throwable thrown = Assertions.catchThrowable(() -> {
            underTest.addRental(rentalDTOMapper.apply(rental));
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
        Throwable thrown = Assertions.catchThrowable(() -> {
            underTest.addRental(rentalDTOMapper.apply(rental));
        });

        // then
        String message = "the car is already booked for the selected dates.";
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
        Throwable thrown = Assertions.catchThrowable(() -> {
            underTest.addRental(rentalDTOMapper.apply(rental));
        });

        // then
        String message = "from date is after to date.";
        assertThat(thrown).hasMessage(message);
    }

    @Test
    public void should_deleteRental_whenRentalIdExists() {
        // given
        long rentalId = rental.getId();
        Mockito.when(rentalRepository.existsById(rentalId)).thenReturn(true);

        Mockito.when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        Mockito.when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        Mockito.when(rentalRepository.findByCarAndDatesOverlap(car, rental.getFromDate(), rental.getToDate())).thenReturn(List.of());

        // when
        underTest.addRental(rentalDTOMapper.apply(rental));
        underTest.deleteRental(rentalId);

        // then
        verify(rentalRepository).deleteById(rentalId);
    }

    @Test
    public void should_throwException_whenDeleteRentalIdNotExists() {
        // given
        long rentalId = rental.getId();

        Mockito.when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        Mockito.when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        Mockito.when(rentalRepository.findByCarAndDatesOverlap(car, rental.getFromDate(), rental.getToDate())).thenReturn(List.of());

        // when
        underTest.addRental(rentalDTOMapper.apply(rental));
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
                .thenReturn(BigDecimal.valueOf(100));

        // when
        BigDecimal result = underTest.calculateEarning(rental);

        // then
        BigDecimal expected = new BigDecimal(1000);
        assertThat(result).isEqualByComparingTo(expected);
    }
}