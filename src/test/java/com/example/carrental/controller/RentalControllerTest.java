package com.example.carrental.controller;

import com.example.carrental.auth.AuthenticationController;
import com.example.carrental.auth.AuthenticationRequest;
import com.example.carrental.auth.RegisterRequest;
import com.example.carrental.car.Car;
import com.example.carrental.car.CarRepository;
import com.example.carrental.config.JwtService;
import com.example.carrental.customer.Customer;
import com.example.carrental.customer.CustomerRepository;
import com.example.carrental.employee.Employee;
import com.example.carrental.employee.EmployeeRepository;
import com.example.carrental.rental.Rental;
import com.example.carrental.rental.RentalRepository;
import com.example.carrental.user.Role;
import com.example.carrental.user.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ExtendWith(MockitoExtension.class)
class RentalControllerTest {

    @LocalServerPort
    private Integer port;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    RentalRepository rentalRepository;
    @Autowired
    CarRepository carRepository;
    @Autowired
    CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        rentalRepository.deleteAll();
    }

    private String registerEmployeeAndGetJWTToken() {
        String response = given()
                .contentType(ContentType.JSON)
                .body(RegisterRequest.builder()
                        .email("employee@gmail.com")
                        .password("test")
                        .role(Role.EMPLOYEE)
                        .build())
                .when()
                .post("/api/v1/auth/register")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .asString();

        return JsonPath.from(response).getString("token");
    }

    @Test
    public void should_getAllRentals_withLogin() {
        // given
        List<Car> cars = new ArrayList<>(
                List.of(
                        Car.builder().id(1L).build(),
                        Car.builder().id(2L).build()
                )
        );
        carRepository.saveAll(cars);

        List<Customer> customers = new ArrayList<>(
                List.of(
                        Customer.builder().id(1L).build(),
                        Customer.builder().id(2L).build()
                )
        );
        customerRepository.saveAll(customers);

        List<Rental> rentals = List.of(
                Rental.builder()
                        .car(cars.get(0))
                        .customer(customers.get(0))
                        .build(),
                Rental.builder()
                        .car(cars.get(1))
                        .customer(customers.get(1))
                        .build()
        );
        rentalRepository.saveAll(rentals);

        String token = registerEmployeeAndGetJWTToken();

        // when, then
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("api/v1/rentals")
                .then()
                .statusCode(200)
                .body(".", hasSize(2));
    }

    @Test
    public void shouldNot_GetAllRentals_withoutLogin() {
        // given
        List<Rental> rentals = List.of(
                Rental.builder().build(),
                Rental.builder().build()
        );
        rentalRepository.saveAll(rentals);

        // when, then
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("api/v1/rentals")
                .then()
                .statusCode(403);
    }

}