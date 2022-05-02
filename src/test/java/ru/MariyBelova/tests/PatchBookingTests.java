package ru.MariyBelova.tests;

import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class PatchBookingTests {
    static String token;
    String id;

    @BeforeAll
    static void beforeAll() {
        token = given()//предусловия, подготовка
                .log()
                .all()
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
                .body("{\n"
                        + "    \"firstname\" : \"Jim\",\n"
                        + "    \"lastname\" : \"Brown\",\n"
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
                .post("https://restful-booker.herokuapp.com/booking")
                .prettyPeek()
                .body()
                .jsonPath()
                .get("bookingid")
                .toString();
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
                .patch("https://restful-booker.herokuapp.com/booking/" + id)
                .prettyPeek()
                .then()
                .statusCode(200)
                .body("firstname", equalTo("Mary"));}
    @Test
    void patchBookingLastnameChangePositiveTest() {
        given()
                .log()
                .all()
                .headers("Content-Type", "application/json", "Accept", "application/json", "Cookie", "token=" + token)
                .body("{\n"
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
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/" + id)
                .prettyPeek()
                .then()
                .statusCode(200)
                .body("lastname", equalTo("Moris"));}
    @Test
    void patchBookingTotalPriceChangePositiveTest() {
        given()
                .log()
                .all()
                .headers("Content-Type", "application/json", "Accept", "application/json", "Cookie", "token=" + token)
                .body("{\n"
                        + "    \"firstname\" : \"Jim\",\n"
                        + "    \"lastname\" : \"Brown\",\n"
                        + "    \"totalprice\" : 8546321,\n"
                        + "    \"depositpaid\" : true,\n"
                        + "    \"bookingdates\" : {\n"
                        + "        \"checkin\" : \"2018-01-01\",\n"
                        + "        \"checkout\" : \"2019-01-01\"\n"
                        + "    },\n"
                        + "    \"additionalneeds\" : \"Breakfast\"\n"
                        + "}")
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/" + id)
                .prettyPeek()
                .then()
                .statusCode(200)
                .body("totalprice", equalTo(8546321));}
    @Test
    void patchBookingDepositPaidChangePositiveTest() {
        given()
                .log()
                .all()
                .headers("Content-Type", "application/json", "Accept", "application/json", "Cookie", "token=" + token)
                .body("{\n"
                        + "    \"firstname\" : \"Jim\",\n"
                        + "    \"lastname\" : \"Brown\",\n"
                        + "    \"totalprice\" : 111,\n"
                        + "    \"depositpaid\" : false,\n"
                        + "    \"bookingdates\" : {\n"
                        + "        \"checkin\" : \"2018-01-01\",\n"
                        + "        \"checkout\" : \"2019-01-01\"\n"
                        + "    },\n"
                        + "    \"additionalneeds\" : \"Breakfast\"\n"
                        + "}")
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/" + id)
                .prettyPeek()
                .then()
                .statusCode(200)
                .body("depositpaid", equalTo(false));}
    @Test
    void patchBookingAdditionalNeedsChangePositiveTest() {
        given()
                .log()
                .all()
                .headers("Content-Type", "application/json", "Accept", "application/json", "Cookie", "token=" + token)
                .body("{\n"
                        + "    \"firstname\" : \"Jim\",\n"
                        + "    \"lastname\" : \"Brown\",\n"
                        + "    \"totalprice\" : 111,\n"
                        + "    \"depositpaid\" : true,\n"
                        + "    \"bookingdates\" : {\n"
                        + "        \"checkin\" : \"2018-05-08\",\n"
                        + "        \"checkout\" : \"2019-01-01\"\n"
                        + "    },\n"
                        + "    \"additionalneeds\" : \"diner\"\n"
                        + "}")
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/" + id)
                .prettyPeek()
                .then()
                .statusCode(200)
                .body("additionalneeds", equalTo("diner"));}
    @Test
    void patchBookingCheckInChangePositiveTest() {
        Response response = given()
                .log()
                .all()
                .headers("Content-Type", "application/json", "Accept", "application/json", "Cookie", "token=" + token)
                .body("{\n"
                        + "    \"firstname\" : \"Jim\",\n"
                        + "    \"lastname\" : \"Brown\",\n"
                        + "    \"totalprice\" : 111,\n"
                        + "    \"depositpaid\" : true,\n"
                        + "    \"bookingdates\" : {\n"
                        + "        \"checkin\" : \"2018-05-08\",\n"
                        + "        \"checkout\" : \"2019-01-01\"\n"
                        + "    },\n"
                        + "    \"additionalneeds\" : \"Breakfast\"\n"
                        + "}")
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/" + id)
                .prettyPeek();
               assertThat(response.statusCode(), equalTo(200));
        assertThat(response.body().jsonPath().get("checking"), nullValue());}

        @Test
        void patchBookingCheckOutChangePositiveTest() {
            Response response = given()
                    .log()
                    .all()
                    .headers("Content-Type", "application/json", "Accept", "application/json", "Cookie", "token=" + token)
                    .body("{\n"
                            + "    \"firstname\" : \"Jim\",\n"
                            + "    \"lastname\" : \"Brown\",\n"
                            + "    \"totalprice\" : 111,\n"
                            + "    \"depositpaid\" : true,\n"
                            + "    \"bookingdates\" : {\n"
                            + "        \"checkin\" : \"2018-05-08\",\n"
                            + "        \"checkout\" : \"2019-01-01\"\n"
                            + "    },\n"
                            + "    \"additionalneeds\" : \"Breakfast\"\n"
                            + "}")
                    .when()
                    .patch("https://restful-booker.herokuapp.com/booking/" + id)
                    .prettyPeek();
            assertThat(response.statusCode(), equalTo(200));
            assertThat(response.body().jsonPath().get("checkout"), nullValue());}
    @Test
    void patchBookingFirstnameNullNegativeTest() {
        Response response =given()
                .log()
                .all()
                .headers("Content-Type", "application/json", "Accept", "application/json", "Cookie", "token=" + token)
                .body("{\n"
                        + "    \"firstname\" : ,\n"
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
                .patch("https://restful-booker.herokuapp.com/booking/" + id)
                .prettyPeek();
                        assertThat(response.statusCode(), equalTo(400));}
    @Test
    void patchBookingLastnameChangeNullNegativeTest() {
        Response response =given()
                .log()
                .all()
                .headers("Content-Type", "application/json", "Accept", "application/json", "Cookie", "token=" + token)
                .body("{\n"
                        + "    \"firstname\" : \"Jim\",\n"
                        + "    \"lastname\" : ,\n"
                        + "    \"totalprice\" : 111,\n"
                        + "    \"depositpaid\" : true,\n"
                        + "    \"bookingdates\" : {\n"
                        + "        \"checkin\" : \"2018-01-01\",\n"
                        + "        \"checkout\" : \"2019-01-01\"\n"
                        + "    },\n"
                        + "    \"additionalneeds\" : \"Breakfast\"\n"
                        + "}")
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/" + id)
                .prettyPeek();
        assertThat(response.statusCode(), equalTo(400));}
        @Test
        void patchBookingTotalPriceChangeNullNegativeTest() {
            Response response =given()
                    .log()
                    .all()
                    .headers("Content-Type", "application/json", "Accept", "application/json", "Cookie", "token=" + token)
                    .body("{\n"
                            + "    \"firstname\" : \"Jim\",\n"
                            + "    \"lastname\" : \"Brown\",\n"
                            + "    \"totalprice\" : ,\n"
                            + "    \"depositpaid\" : true,\n"
                            + "    \"bookingdates\" : {\n"
                            + "        \"checkin\" : \"2018-01-01\",\n"
                            + "        \"checkout\" : \"2019-01-01\"\n"
                            + "    },\n"
                            + "    \"additionalneeds\" : \"Breakfast\"\n"
                            + "}")
                    .when()
                    .patch("https://restful-booker.herokuapp.com/booking/" + id)
                    .prettyPeek();
            assertThat(response.statusCode(), equalTo(400));
        }
    }