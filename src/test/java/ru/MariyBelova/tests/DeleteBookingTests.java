package ru.MariyBelova.tests;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;


public class DeleteBookingTests {
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
        void deleteBookingPositiveTest() {
            given()
                    .log()
                    .all()
                    .header("Cookie", "token="+token)
                    .when()
                    .delete("https://restful-booker.herokuapp.com/booking/"+id)
                    .prettyPeek()
                    .then()
                    .statusCode(201);
        }

        @Test
        void deleteBookingAuthorisationPositiveTest() {
            given()
                    .log()
                    .all()
                    .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                    .when()
                    .delete("https://restful-booker.herokuapp.com/booking/"+id)
                    .prettyPeek()
                    .then()
                    .statusCode(201);
        }
    }
