package Lesson2;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

// URL, которые должны получиться в ответе (получила через инструмент разрпаботчика Google):
// https://playground.learnqa.ru/api/long_redirect - изначальный;
// https://playground.learnqa.ru/ - второй;
// https://learnqa.ru/ - третий;
// https://www.learnqa.ru/ - финальный
public class EX7 {
    @Test
    public void address() {
        String url = "https://playground.learnqa.ru/api/long_redirect";
        int code = 301;
        int cycle = 1;
        while (code == 301) {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(url)
                    .andReturn();
            code = response.getStatusCode();
            url = response.getHeader("Location");

               System.out.println("Цикл "+cycle);
               System.out.println("URL for redirection: "+url);
               System.out.println("http code: "+ code);
            cycle++;
        }
    }
}