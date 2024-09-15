package Lesson3;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Ex12 {

    @Test
    public void NameTest() {

        Response response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();
        response.prettyPrint();
        Headers headers = response.getHeaders();
        System.out.println(headers);
        String contentType = response.getHeader("Content-Type");
        String connect = response.getHeader("Connection");


        assertEquals (200, response.statusCode(), "Error status code");
        assertTrue(headers.exist(), "No headers in the response");
        assertTrue(headers.hasHeaderWithName("Date"), "No header 'Date' in the response");
        assertTrue(headers.hasHeaderWithName("Content-Type"), "Content-Type is not defined - no header 'Content-Type'");
        assertEquals(contentType, "application/json", "Content-Type must be 'application/json'");
        assertNotEquals("close" ,connect, "Connection must be 'keep-alive!'");

    }

}