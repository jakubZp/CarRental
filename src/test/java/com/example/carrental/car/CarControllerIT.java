package com.example.carrental.car;

import com.example.carrental.integrationTestsHelpers.EnableTestcontainers;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableTestcontainers
public class CarControllerIT {

    @LocalServerPort
    private Integer port;

    @Autowired
    CarRepository carRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        carRepository.deleteAll();
    }

    @Test
    public void shouldGetAllCars() {
        // given
        List<Car> cars = List.of(
                Car.builder().build(),
                Car.builder().build()
        );
        carRepository.saveAll(cars);

        // when, then
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("api/v1/cars")
                .then()
                .statusCode(200)
                .body(".", hasSize(2));
    }

    @Test
    public void shouldGet_oneCarById() {
        // given
        List<Car> cars = List.of(
                Car.builder()
                        .brand("toyota")
                        .build(),
                Car.builder().build()
        );
        carRepository.saveAll(cars);
        int id = carRepository.findAll().get(0).getId().intValue();
        String expectedBrand = carRepository.findAll().get(0).getBrand();

        // when, then
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("api/v1/cars/" + id)
                .then()
                .statusCode(200)
                .body("brand", equalTo(expectedBrand))
                .body("carId", equalTo(id));
    }

}