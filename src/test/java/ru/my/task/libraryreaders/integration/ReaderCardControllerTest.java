package ru.my.task.libraryreaders.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.*;


public class ReaderCardControllerTest {

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
    public void readerCardRead() {
        Response response = get("http://localhost:8080/api/readerCards");
        response.then().
                body("id", hasItem(1)).
                body("lastName", hasItem("иванов")).
                body("firstName", hasItem("иван")).
                body("middleName", hasItem("иванович")).
                body("birthDate", hasItem("14.07.1990")).
                body("gender", hasItem("male")).
                body("insuranceNumber", hasItem("160-722-773 54")).
                statusCode(200).
                assertThat();
    }

    @Test
    public void readerCardFindById() {
        String createdElementId = createReaderCardElement().path("id").toString();
        Response response = get("http://localhost:8080/api/readerCards/" + createdElementId);
        response.then().
                body("id", notNullValue()).
                body("lastName", equalTo("анатольев")).
                body("firstName", equalTo("анатолий")).
                body("middleName", equalTo("анатольевич")).
                body("birthDate", notNullValue()).
                body("gender", equalTo("male")).
                body("insuranceNumber", equalTo("160-722-773 54")).
                body("receivedBooks.id", notNullValue()).
                body("receivedBooks.bookName", hasItem("Жюль Верн - Пятнадцатилетний капитан")).
                body("receivedBooks.returned", hasItem("Y")).
                body("receivedBooks.dateBookReceived", notNullValue()).
                body("receivedBooks.readerCardId", notNullValue()).
                body("receivedBooks.id", notNullValue()).
                body("receivedBooks.bookName", hasItem("Жюль Верн - Пять недель на воздушном шаре")).
                body("receivedBooks.returned", hasItem("N")).
                body("receivedBooks.dateBookReceived", notNullValue()).
                body("receivedBooks.readerCardId", notNullValue()).
                statusCode(200).
                assertThat();
    }

    @Test
    public void readerCardCreate() {
        createReaderCardElement();
    }

    private Response createReaderCardElement() {
        String body = "{\n" +
                "\t\"lastName\" : \"анатольев\",\n" +
                "\t\"firstName\" : \"анатолий\",\n" +
                "\t\"middleName\" : \"анатольевич\",\n" +
                "\t\"birthDate\" : \"01.01.1990\",\n" +
                "\t\"gender\" : \"male\",\n" +
                "\t\"insuranceNumber\" : \"160-722-773 54\",\n" +
                "    \"receivedBooks\": [\n" +
                "        {\n" +
                "            \"bookName\": \"Жюль Верн - Пятнадцатилетний капитан\",\n" +
                "            \"returned\": \"Y\",\n" +
                "            \"dateBookReceived\": \"20.03.2020\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"bookName\": \"Жюль Верн - Пять недель на воздушном шаре\",\n" +
                "            \"returned\": \"N\",\n" +
                "            \"dateBookReceived\": \"20.03.2020\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        return RestAssured.given().
                contentType(ContentType.JSON).
                body(body).
                when().
                post("http://localhost:8080/api/readerCards").
                then().
                statusCode(201).
                contentType("application/json;charset=UTF-8").
                body("id", notNullValue()).
                body("lastName", equalTo("анатольев")).
                body("firstName", equalTo("анатолий")).
                body("middleName", equalTo("анатольевич")).
                body("birthDate", equalTo("01.01.1990")).
                body("gender", equalTo("male")).
                body("insuranceNumber", equalTo("160-722-773 54")).
                body("receivedBooks.id", notNullValue()).
                body("receivedBooks.bookName", hasItem("Жюль Верн - Пятнадцатилетний капитан")).
                body("receivedBooks.returned", hasItem("Y")).
                body("receivedBooks.dateBookReceived", notNullValue()).
                body("receivedBooks.readerCardId", notNullValue()).
                body("receivedBooks.id", notNullValue()).
                body("receivedBooks.bookName", hasItem("Жюль Верн - Пять недель на воздушном шаре")).
                body("receivedBooks.returned", hasItem("N")).
                body("receivedBooks.dateBookReceived", notNullValue()).
                body("receivedBooks.readerCardId", notNullValue()).
                assertThat().
                extract().response();
    }

    @Test
    public void readerCardUpdate() {
        Response responseCreatedElement = createReaderCardElement();
        String createdElementId = responseCreatedElement.path("id").toString();
        String createdElementReceivedBooksIds = responseCreatedElement.path("receivedBooks.id").toString();
        String[] receivedBooksIds = createdElementReceivedBooksIds
                .replace("[", "")
                .replace("]", "")
                .split(", ");

        String bodyWithChangedFieldReturnedAndBirthDate = "{\n" +
                "    \"lastName\" : \"анатольев\",\n" +
                "\t\"firstName\" : \"анатолий\",\n" +
                "\t\"middleName\" : \"анатольевич\",\n" +
                "\t\"birthDate\" : \"01.01.1992\",\n" +
                "\t\"gender\" : \"male\",\n" +
                "\t\"insuranceNumber\" : \"160-722-773 54\",\n" +
                "    \"receivedBooks\": [\n" +
                "        {\n" +
                "            \"id\": " + receivedBooksIds[0] + ",\n" +
                "            \"bookName\": \"Жюль Верн - Пятнадцатилетний капитан\",\n" +
                "            \"returned\": \"Y\",\n" +
                "            \"dateBookReceived\": \"20.03.2020\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": " + receivedBooksIds[1] + ",\n" +
                "            \"bookName\": \"Жюль Верн - Пять недель на воздушном шаре\",\n" +
                "            \"returned\": \"Y\",\n" +
                "            \"dateBookReceived\": \"20.03.2020\"\n" +
                "        }\n" +
                "    ]\n" +
                "    \n" +
                "}";

        RestAssured.given().
                contentType(ContentType.JSON).
                body(bodyWithChangedFieldReturnedAndBirthDate).
                when().
                put("http://localhost:8080/api/readerCards/" + createdElementId).
                then().
                statusCode(200).
                contentType("application/json;charset=UTF-8").
                body("id", notNullValue()).
                body("lastName", equalTo("анатольев")).
                body("firstName", equalTo("анатолий")).
                body("middleName", equalTo("анатольевич")).
                body("birthDate", equalTo("01.01.1992")).
                body("gender", equalTo("male")).
                body("insuranceNumber", equalTo("160-722-773 54")).
                body("receivedBooks.id", notNullValue()).
                body("receivedBooks.bookName", hasItem("Жюль Верн - Пятнадцатилетний капитан")).
                body("receivedBooks.returned", hasItem("Y")).
                body("receivedBooks.dateBookReceived", notNullValue()).
                body("receivedBooks.readerCardId", notNullValue()).
                body("receivedBooks.id", notNullValue()).
                body("receivedBooks.bookName", hasItem("Жюль Верн - Пять недель на воздушном шаре")).
                body("receivedBooks.returned", hasItem("Y")).
                body("receivedBooks.dateBookReceived", notNullValue()).
                body("receivedBooks.readerCardId", notNullValue()).
                assertThat();
    }

    @Test
    public void readerCardDelete() {
        String createdElementId = createReaderCardElement().path("id").toString();
        RestAssured.given().
                contentType(ContentType.JSON).
                when().
                delete("http://localhost:8080/api/readerCards/" + createdElementId).
                then().
                statusCode(202).
                assertThat();
    }
}
