package ru.my.task.libraryreaders.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.*;


public class ReceivedBookControllerTest {

    {
        getAuthToken();
    }

    @Test
    public void getAuthToken() {
        String body = "{\"username\":\"user\",\"password\":\"test\"}";
        Response response = RestAssured.given().
                contentType(ContentType.JSON).
                body(body).
                when().
                post("http://localhost:8080/api/v1/auth/login").
                then().
                statusCode(200).
                contentType("application/json;charset=UTF-8").
                body("$", hasKey("token")).
                body("any { it.key == 'token' }", is(notNullValue())).
                extract().response();

        String authToken = response.path("token").toString();
        RestAssured.authentication = RestAssured.oauth2(authToken);
    }

    @Test
    public void receivedBookFindById() {
        String createdElementId = createReceivedBookElement().path("id").toString();
        Response response = get("http://localhost:8080/api/receivedBooks/" + createdElementId);
        response.then().
                body("id", notNullValue()).
                body("bookName", equalTo("Жюль Верн - Дети капитана Гранта")).
                body("returned", equalTo("N")).
                body("dateBookReceived", notNullValue()).
                body("readerCardId", equalTo(3)).
                statusCode(200).
                assertThat();
    }

    @Test
    public void receivedBookCreate() {
        createReceivedBookElement();
    }

    private Response createReceivedBookElement() {
        String body = "{\n" +
                "\t\"bookName\" : \"Жюль Верн - Дети капитана Гранта\",\n" +
                "\t\"returned\" : \"N\",\n" +
                "\t\"dateBookReceived\" : \"23.03.2003\",\n" +
                "    \"readerCardId\": 3\n" +
                "}";

        return RestAssured.given().
                contentType(ContentType.JSON).
                body(body).
                when().
                post("http://localhost:8080/api/receivedBooks").
                then().
                statusCode(201).
                contentType("application/json;charset=UTF-8").
                body("id", notNullValue()).
                body("bookName", equalTo("Жюль Верн - Дети капитана Гранта")).
                body("returned", equalTo("N")).
                body("dateBookReceived", equalTo("2003-03-23T00:00:00.000+0000")).
                body("readerCardId", equalTo(3)).
                assertThat().
                extract().response();
    }

    @Test
    public void receivedBookUpdate() {
        String createdElementId = createReceivedBookElement().path("id").toString();

        String bodyWithChangedFieldReturned = "{\n" +
                "\t\"bookName\" : \"Жюль Верн - Путешествие к центру Земли\",\n" +
                "\t\"returned\" : \"N\",\n" +
                "\t\"dateBookReceived\" : \"23.03.2003\",\n" +
                "    \"readerCardId\": 1\n" +
                "}";
        RestAssured.given().
                contentType(ContentType.JSON).
                body(bodyWithChangedFieldReturned).
                when().
                put("http://localhost:8080/api/receivedBooks/" + createdElementId).
                then().
                statusCode(200).
                contentType("application/json;charset=UTF-8").
                body("id", notNullValue()).
                body("bookName", equalTo("Жюль Верн - Путешествие к центру Земли")).
                body("returned", equalTo("N")).
                body("dateBookReceived", equalTo("2003-03-23T00:00:00.000+0000")).
                body("readerCardId", equalTo(1)).
                assertThat();
    }

    @Test
    public void receivedBookDelete() {
        String createdElementId = createReceivedBookElement().path("id").toString();
        RestAssured.given().
                contentType(ContentType.JSON).
                when().
                delete("http://localhost:8080/api/receivedBooks/" + createdElementId).
                then().
                statusCode(202).
                assertThat();
    }
}
