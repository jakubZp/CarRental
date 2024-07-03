package com.example.carrental.rental;

import com.example.carrental.car.Car;
import com.example.carrental.car.CarRepository;
import com.example.carrental.customer.Customer;
import com.example.carrental.customer.CustomerRepository;
import com.example.carrental.integrationTestsHelpers.AuthHelper;
import com.example.carrental.integrationTestsHelpers.EnableTestcontainers;
import com.example.carrental.user.TokenRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableTestcontainers
class RentalControllerIT {

    @LocalServerPort
    private Integer port;

    @Autowired
    RentalRepository rentalRepository;
    @Autowired
    CarRepository carRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    TokenRepository tokenRepository;

    private List<Car> cars;
    private List<Customer> customers;
    private String employeeToken;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost:" + port;

        cars = List.of(
                Car.builder().build(),
                Car.builder().build()
        );
        carRepository.saveAll(cars);

        customers = customerRepository.findAllCustomers();
        employeeToken = AuthHelper.loginEmployeeAndGetJWTToken();
    }

    @AfterEach
    void tearDown() {
        rentalRepository.deleteAll();
        carRepository.deleteAll();
        tokenRepository.deleteAll();
    }

    @Test
    public void should_getAllRentals_withEmployeeLogin() {
        // given
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

        // when, then
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + employeeToken)
                .when()
                .get("api/v1/rentals")
                .then()
                .statusCode(200)
                .body(".", hasSize(2));
    }

    @Test
    public void shouldGet_oneRentalById() {
        // given
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

        int id = rentalRepository.findAllRentals().get(0).getId().intValue();

        // when, then
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + employeeToken)
                .when()
                .get("api/v1/rentals/" + id)
                .then()
                .statusCode(200)
                .body("rentalId", equalTo(id));
    }

    @Test
    public void shouldNot_GetAllRentals_withoutEmployeeLogin() {
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

    @Test
    public void shouldAddRental_withEmployeeLogin() {
        // given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("fromDate", "2024-09-19T12:00:00");
        requestBody.put("toDate", "2024-09-27T12:00:00");
        requestBody.put("carId", cars.get(0).getId().intValue());
        requestBody.put("customerId", customers.get(0).getId().intValue());

        // when, then
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + employeeToken)
                .body(requestBody)
                .when()
                .post("api/v1/rentals")
                .then()
                .statusCode(200)
                .header("Content-Type", "application/json")
                .body("rentalId", notNullValue());
    }

}