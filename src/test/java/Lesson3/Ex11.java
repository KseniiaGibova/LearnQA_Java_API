package Lesson3;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Ex11 {

    @Test
    public void NameTest() {

        Response response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        response.prettyPrint();
        Map<String, String> cookies = response.getCookies();
               System.out.println(cookies);

               assertFalse(cookies.isEmpty(), "No cookies in the response");
               assertTrue(cookies.containsKey("HomeWork"), "No 'HomeWork' cookie in the response");
               assertFalse(cookies.containsValue(""), "'HomeWork' value is empty");
               assertTrue(cookies.containsValue("hw_value"), "Wrong cookies");

    }
}
