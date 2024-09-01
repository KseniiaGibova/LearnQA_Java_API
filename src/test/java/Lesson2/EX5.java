package Lesson2;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;


public class EX5 {


        @Test
        public void printAll()
        //Полученный JSON необходимо распечатать и изучить - распечатываем весь массив
        {
           JsonPath response = RestAssured
                    .given()
                    .get("https://playground.learnqa.ru/api/get_json_homework")
                    .jsonPath();

            response.prettyPrint();
        }

        @Test
        public void printSecond() {
        // Выводим для печати текст второго сообщения

            JsonPath response = RestAssured
                    .given()
                    .get("https://playground.learnqa.ru/api/get_json_homework")
                    .jsonPath();

            String answer = response.get("messages.message[1]");
            System.out.println(answer);
        }
    }


