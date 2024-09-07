package Lesson3;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Ex10 {

    @ParameterizedTest
    @ValueSource(strings = {"Any text longer than 15 characters", "anything", "123456789012345"})
    //Первый тест - успех, второй - неуспех, третий - граничное значение 15 - неуспех;

    public void success(String str) {
        Map<String, String> queryParams = new HashMap<>();
        Response response = RestAssured
                .given()
                .queryParams(queryParams)
                .get()
                .andReturn();
        assertTrue(str.length() > 15, "Test failed: String length must be longer than 15 characters");

    }
}