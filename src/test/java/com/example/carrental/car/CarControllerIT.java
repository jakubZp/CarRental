package com.example.carrental.car;

import com.example.carrental.integrationTestsHelpers.AuthHelper;
import com.example.carrental.integrationTestsHelpers.EnableTestcontainers;
import com.example.carrental.priceUpdate.PriceUpdateRepository;
import com.example.carrental.user.token.TokenRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableTestcontainers
public class CarControllerIT {

    @LocalServerPort
    private Integer port;

    @Autowired
    CarRepository carRepository;
    @Autowired
    PriceUpdateRepository priceUpdateRepository;
    @Autowired
    TokenRepository tokenRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @AfterEach
    void tearDown() {
        priceUpdateRepository.deleteAll();
        carRepository.deleteAll();
        tokenRepository.deleteAll();
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
        Car c = carRepository.findAll().get(0);
        int id = c.getId().intValue();
        String expectedBrand = c.getBrand();

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

    @Test
    public void should_addCar_withEmployeeLogin() {
        // given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("brand", "toyota");
        requestBody.put("model", "avensis");
        requestBody.put("productionYear", 2018);
        requestBody.put("actualDailyPrice", BigDecimal.valueOf(80));

        String employeeToken = AuthHelper.loginEmployeeAndGetJWTToken();

        // when, then
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + employeeToken)
                .body(requestBody)
                .when()
                .post("api/v1/cars")
                .then()
                .statusCode(200)
                .header("Content-Type", "application/json")
                .body("carId", notNullValue());
    }

    @Test
    public void should_updateCar_withEmployeeLogin() {
        // given
        Car c = Car.builder()
                .brand("toyota")
                .model("yaris")
                .productionYear(2017)
                .actualDailyPrice(BigDecimal.valueOf(70))
                .build();
        carRepository.save(c);
        int carId = carRepository.findAll().get(0).getId().intValue();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("brand", "toyota");
        requestBody.put("model", "avensis");
        requestBody.put("productionYear", 2018);
        requestBody.put("actualDailyPrice", BigDecimal.valueOf(80));

        String expectedModel = (String) requestBody.get("model");
        Integer expectedProductionYear = (Integer) requestBody.get("productionYear");
        BigDecimal expectedActualDailyPrice = (BigDecimal) requestBody.get("actualDailyPrice");

        String employeeToken = AuthHelper.loginEmployeeAndGetJWTToken();

        // when, then
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + employeeToken)
                .body(requestBody)
                .when()
                .put("api/v1/cars/" + carId)
                .then()
                .statusCode(200)
                .header("Content-Type", "application/json")
                .body("carId", notNullValue())
                .body("model", equalTo(expectedModel))
                .body("productionYear", equalTo(expectedProductionYear))
                .body("actualDailyPrice", hasToString(expectedActualDailyPrice.toString()));
    }

    @Test
    public void should_deleteCar_withEmployeeLogin() {
        // given
        Car c = Car.builder()
                .brand("toyota")
                .model("yaris")
                .productionYear(2017)
                .actualDailyPrice(BigDecimal.valueOf(70))
                .build();
        carRepository.save(c);
        int carId = carRepository.findAll().get(0).getId().intValue();

        String employeeToken = AuthHelper.loginEmployeeAndGetJWTToken();

        // when, then
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + employeeToken)
                .when()
                .delete("api/v1/cars/" + carId)
                .then()
                .statusCode(200);
    }
}