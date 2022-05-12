package ru.MariyBelova.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.MariyBelova.dao.BookingRequest;
import ru.MariyBelova.dao.CreateTokenRequest;

import java.io.FileInputStream;
import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.MariyBelova.tests.CreateTokenTests.PROPERTIES_FILE_PATH;
import static ru.MariyBelova.tests.CreateTokenTests.properties;

public class PatchBookingTests {
    static String token;
    String id;

    @BeforeAll

    static void beforeAll() throws IOException {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        CreateTokenRequest request = CreateTokenRequest.builder()
                .username("admin")
                .password("password123")
                .build();
        properties.load(new FileInputStream(PROPERTIES_FILE_PATH));
        RestAssured.baseURI = properties.getProperty("base.url");

        token = given()//предусловия, подготовка
                .log()
                .all()
                .header("Content-Type", "application/json")
                .body(request)
                .expect()
                .statusCode(200)
                .body("token", is(CoreMatchers.not(nullValue())))
                .when()
                .post("/auth")//шаг(и)
                .prettyPeek()
                .body()
                .jsonPath()
                .get("token")
                .toString();
    }

    @BeforeEach
    void setUp() {
        //создает бронирование
        id = given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .body(
                        "{\n"
                                + "    \"firstname\" : \"Jim\",\n"
                                + "    \"lastname\" : \"Moris\",\n"
                                + "    \"totalprice\" : 111,\n"
                                + "    \"depositpaid\" : true,\n"
                                + "    \"bookingdates\" : {\n"
                                + "        \"checkin\" : \"2018-01-01\",\n"
                                + "        \"checkout\" : \"2019-01-01\"\n"
                                + "    },\n"
                                + "    \"additionalneeds\" : \"Breakfast\"\n"
                                + "}")
                .expect()
                .statusCode(200)
                .when()
                .post("/booking")
                .prettyPeek()
                .body()
                .jsonPath()
                .get("bookingid")
                .toString();
    }

    @AfterEach
    void deleteBooking() {
        given()
                .log()
                .all()
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .when()
                .delete("/booking/" + id)
                .prettyPeek();
    }

    @Test
    void patchBookingFirstnameChangePositiveTest() {
        given()
                .log()
                .all()
                .headers("Content-Type", "application/json", "Accept", "application/json", "Cookie", "token=" + token)
                .body("{\n"
                        + "    \"firstname\" : \"Mary\",\n"
                        + "    \"lastname\" : \"Brown\",\n"
                        + "    \"totalprice\" : 111,\n"
                        + "    \"depositpaid\" : true,\n"
                        + "    \"bookingdates\" : {\n"
                        + "        \"checkin\" : \"2018-01-01\",\n"
                        + "        \"checkout\" : \"2019-01-01\"\n"
                        + "    },\n"
                        + "    \"additionalneeds\" : \"Breakfast\"\n"
                        + "}")
                .when()
                .patch("/booking/" + id)
                .prettyPeek()
                .then()
                .statusCode(200)
                .body("firstname", equalTo("Mary"));
    }

    @Test
    void patchBookingLastnameChangePositiveTest() {
        given()
                .log()
                .all()
                .headers("Content-Type", "application/json", "Accept", "application/json", "Cookie", "token=" + token)
                .body(BookingRequest.builder()
                        .firstname("Jim")
                        .lastname("Moris")
                        .totalprice(111)
                        .depositpaid(true)
                        .bookingdates("2018-01-01", "2019-01-01")
                        .additionalneeds("Breakfast")
                        .build())
                .when()
                .patch("/booking/" + id)
                .prettyPeek()
                .then()
                .statusCode(200)
                .body("lastname", equalTo("Moris"));
    }

    @Test
    void patchBookingTotalPriceChangePositiveTest() {
        given()
                .log()
                .all()
                .headers("Content-Type", "application/json", "Accept", "application/json", "Cookie", "token=" + token)
                .body(BookingRequest.builder()
                        .firstname("Jim")
                        .lastname("Brown")
                        .totalprice(8546321)
                        .depositpaid(true)
                        .bookingdates("2018-01-01", "2019-01-01")
                        .additionalneeds("Breakfast")
                        .build())
                .when()
                .patch("/booking/" + id)
                .prettyPeek()
                .then()
                .statusCode(200)
                .body("totalprice", equalTo(8546321));
    }

    @Test
    void patchBookingDepositPaidChangePositiveTest() {
        given()
                .log()
                .all()
                .headers("Content-Type", "application/json", "Accept", "application/json", "Cookie", "token=" + token)
                .body(BookingRequest.builder()
                        .firstname("Jim")
                        .lastname("Brown")
                        .totalprice(111)
                        .depositpaid(false)
                        .bookingdates("2018-01-01", "2019-01-01")
                        .additionalneeds("Breakfast")
                        .build())
                .when()
                .patch("/booking/" + id)
                .prettyPeek()
                .then()
                .statusCode(200)
                .body("depositpaid", equalTo(false));
    }

    @Test
    void patchBookingAdditionalNeedsChangePositiveTest() {
        given()
                .log()
                .all()
                .headers("Content-Type", "application/json", "Accept", "application/json", "Cookie", "token=" + token)
                .body(BookingRequest.builder()
                        .firstname("Jim")
                        .lastname("Brown")
                        .totalprice(111)
                        .depositpaid(true)
                        .bookingdates("2018-01-01", "2019-01-01")
                        .additionalneeds("diner")
                        .build())
                .when()
                .patch("/booking/" + id)
                .prettyPeek()
                .then()
                .statusCode(200)
                .body("additionalneeds", equalTo("diner"));
    }

    @Test
    void patchBookingCheckInChangePositiveTest() {
        Response response = given()
                .log()
                .all()
                .headers("Content-Type", "application/json", "Accept", "application/json", "Cookie", "token=" + token)
                .body(BookingRequest.builder()
                        .firstname("Jim")
                        .lastname("Brown")
                        .totalprice(111)
                        .depositpaid(true)
                        .bookingdates("2018-03-04", "2019-01-01")
                        .additionalneeds("Breakfast")
                        .build())
                .when()
                .patch("/booking/" + id)
                .prettyPeek();
        assertThat(response.statusCode(), equalTo(200));
        assertThat(response.body().jsonPath().get("checkout"), nullValue());
    }

    @Test
    void patchBookingCheckOutChangePositiveTest() {
        Response response = given()
                .log()
                .all()
                .headers("Content-Type", "application/json", "Accept", "application/json", "Cookie", "token=" + token)
                .body(BookingRequest.builder()
                        .firstname("Jim")
                        .lastname("Brown")
                        .totalprice(111)
                        .depositpaid(true)
                        .bookingdates("2018-01-01", "2019-03-04")
                        .additionalneeds("Breakfast")
                        .build())
                .when()
                .patch("/booking/" + id)
                .prettyPeek();
        assertThat(response.statusCode(), equalTo(200));
        assertThat(response.body().jsonPath().get("checkout"), nullValue());
    }

    @Test
    void patchBookingTotalPriceNullNegativeTest() {
        Response response = given()
                .log()
                .all()
                .headers("Content-Type", "application/json", "Accept", "application/json", "Cookie", "token=" + token)
                .body(BookingRequest.builder()
                        .firstname("Jim")
                        .lastname("Brown")
                        .depositpaid(true)
                        .bookingdates("2018-01-01", "2019-01-01")
                        .additionalneeds("Breakfast")
                        .build())
                .when()
                .patch("/booking/" + id)
                .prettyPeek();
        assertThat(response.statusCode(), equalTo(200));
        assertThat(response.body().jsonPath().get("totalprice"), notNullValue());
    }

    @Test
    void patchBookingFirstNameChangeNullNegativeTest() {
        Response response = given()
                .log()
                .all()
                .headers("Content-Type", "application/json", "Accept", "application/json", "Cookie", "token=" + token)
                .body(BookingRequest.builder()
                        .lastname("Brown")
                        .totalprice(111)
                        .depositpaid(true)
                        .bookingdates("2018-01-01", "2019-03-04")
                        .additionalneeds("Breakfast")
                        .build())
                .when()
                .patch("/booking/" + id)
                .prettyPeek();
        assertThat(response.statusCode(), equalTo(200));
        assertThat(response.body().jsonPath().get("firstname"), notNullValue());
    }

    @Test
    void patchBookingLastnameChangeNullNegativeTest() {
        Response response = given()
                .log()
                .all()
                .headers("Content-Type", "application/json", "Accept", "application/json", "Cookie", "token=" + token)
                .body(BookingRequest.builder()
                        .firstname("Jim")
                        .totalprice(111)
                        .depositpaid(true)
                        .bookingdates("2018-01-01", "2019-01-01")
                        .additionalneeds("Breakfast")
                        .build())
                .when()
                .patch("/booking/" + id)
                .prettyPeek();
        assertThat(response.statusCode(), equalTo(200));
        assertThat(response.body().jsonPath().get("lastname"), notNullValue());
    }


}