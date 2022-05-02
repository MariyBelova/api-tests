package ru.MariyBelova.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class CreateTokenTests {
    @BeforeAll
    static void beforeAll() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void createTokenPositiveTest() {
        given()//предусловия, подготовка
                .log()
                .method()
                .log()
                .uri()
                .log()
                .body()
                .header("Content-Type", "application/json")
                .body("{\n"
                        + "    \"username\" : \"admin\",\n"
                        + "    \"password\" : \"password123\"\n"
                        + "}")
                .expect()
                .statusCode(200)
                .body("token", is(CoreMatchers.not(nullValue())))
                .when()
                .post("https://restful-booker.herokuapp.com/auth")//шаг(и)
                .prettyPeek(); // логируем ответ
    }

    @Test
    void createTokenWithAWrongPasswordNegativeTest() {
        given() //предусловия, подготовка
                .log()
                .method()
                .log()
                .uri()
                .log()
                .body()
                .header("Content-Type", "application/json")
                .body("{\n"
                        + "    \"username\" : \"admin\",\n"
                        + "    \"password\" : \"password\"\n"
                        + "}")
                .when()
                .post("https://restful-booker.herokuapp.com/auth")//шаг(и)
                .prettyPeek()
                .then() //проверки
                .statusCode(200)
                .body("reason", equalTo("Bad credentials"));
    }

    @Test
    void createTokenWithAWrongUsernameAndPasswordNegativeTest() {
        Response response = given() //предусловия, подготовка
                .log()
                .method()
                .log()
                .uri()
                .log()
                .body()
                .header("Content-Type", "application/json")
                .body("{\n"
                        + "    \"username\" : \"admin123\",\n"
                        + "    \"password\" : \"password\"\n"
                        + "}")
                .when()
                .post("https://restful-booker.herokuapp.com/auth")//шаг(и)
                .prettyPeek();
        assertThat(response.statusCode(), equalTo(200));
        assertThat(response.body().jsonPath().get("reason"), containsStringIgnoringCase("Bad credentials"));
    }
}

