package com.example.carrental.integrationTestsHelpers;

import com.example.carrental.auth.AuthenticationRequest;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.given;

public class AuthHelper {
    public static String loginEmployeeAndGetJWTToken() {
        String response = given()
                .contentType(ContentType.JSON)
                .body(AuthenticationRequest.builder()
                        .email("test@employee.com")
                        .password("test")
                        .build())
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .asString();

        return JsonPath.from(response).getString("token");
    }
}
