package com.example.carrental.rental;

import com.example.carrental.car.Car;
import com.example.carrental.car.CarRepository;
import com.example.carrental.customer.Customer;
import com.example.carrental.customer.CustomerRepository;
import com.example.carrental.integrationTestsHelpers.AuthHelper;
import com.example.carrental.integrationTestsHelpers.EnableTestcontainers;
import com.example.carrental.user.token.TokenRepository;
import com.example.carrental.user.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
    @Autowired
    UserRepository userRepository;

    private List<Car> cars;
    private List<Customer> customers;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost:" + port;
        customers = customerRepository.findAllCustomers();
        cars = List.of(
                Car.builder().build(),
                Car.builder().build()
        );
        carRepository.saveAll(cars);
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
        String employeeToken = AuthHelper.loginEmployeeAndGetJWTToken();

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
    public void shouldNot_getAllRentals_withoutLogin() {
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
    public void should_getOneRentalById_withEmployeeLogin() {
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
        String employeeToken = AuthHelper.loginEmployeeAndGetJWTToken();

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
    public void shouldNot_getOneRentalById_withoutLogin() {
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
                .when()
                .get("api/v1/rentals/" + id)
                .then()
                .statusCode(403);
    }

    @Test
    public void should_addRental_withEmployeeLogin() {
        // given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("fromDate", "2024-09-19T12:00:00");
        requestBody.put("toDate", "2024-09-27T12:00:00");
        requestBody.put("carId", cars.get(0).getId().intValue());
        requestBody.put("customerId", customers.get(0).getId().intValue());

        String employeeToken = AuthHelper.loginEmployeeAndGetJWTToken();

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

    @Test
    public void should_addRental_withCustomerLogin() {
        // given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("fromDate", "2024-09-19T12:00:00");
        requestBody.put("toDate", "2024-09-27T12:00:00");
        requestBody.put("carId", cars.get(0).getId().intValue());

        int testCustomerId = userRepository.findUserByEmail("test@customer.com").orElseThrow(() -> {
            throw new UsernameNotFoundException("Cannot find user with email test@customer.com");
                }).getCustomer().getId().intValue();
        requestBody.put("customerId", testCustomerId);

        String customerToken = AuthHelper.loginCustomerAndGetJWTToken();

        // when, then
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + customerToken)
                .body(requestBody)
                .when()
                .post("api/v1/rentals")
                .then()
                .statusCode(200)
                .header("Content-Type", "application/json")
                .body("rentalId", notNullValue());
    }

    @Test
    public void should_notAddRental_withoutLogin() {
        // given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("fromDate", "2024-09-19T12:00:00");
        requestBody.put("toDate", "2024-09-27T12:00:00");
        requestBody.put("carId", cars.get(0).getId().intValue());
        requestBody.put("customerId", customers.get(0).getId().intValue());

        // when, then
        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("api/v1/rentals")
                .then()
                .statusCode(403);
    }

    @Test
    public void should_deleteOneRentalById_withEmployeeLogin() {
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
        String employeeToken = AuthHelper.loginEmployeeAndGetJWTToken();

        // when, then
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + employeeToken)
                .when()
                .delete("api/v1/rentals/" + id)
                .then()
                .statusCode(200);
    }

    @Test
    public void shouldNot_deleteOneRentalById_withoutLogin() {
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
                .when()
                .delete("api/v1/rentals/" + id)
                .then()
                .statusCode(403);
    }

}