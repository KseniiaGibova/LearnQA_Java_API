package Lesson2;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import static java.lang.Thread.sleep;

public class EX8copy {

    String token;
    int seconds;

   @Test
   public void getToken() throws InterruptedException {

       System.out.println("Создание задачи");
       JsonPath response = RestAssured
               .given()
               .get("https://playground.learnqa.ru/ajax/api/longtime_job")
               .jsonPath();
       response.prettyPrint();
       token = response.get("token");
       seconds = response.get("seconds");

       System.out.println("Запрос до готовности задачи");
       JsonPath response2 = RestAssured
               .given()
               .queryParam("token", token)
               .get("https://playground.learnqa.ru/ajax/api/longtime_job")
               .jsonPath();
       response2.prettyPrint();
       String taskStatus = response2.get("status");
       if (taskStatus.equals("Job is NOT ready")) {
           System.out.println("Задача выполняется, ожидайте");
       } else {
           System.out.println("Ошибка выполнения задачи");
       }

       Thread.sleep(1000 * seconds);

       System.out.println("Запрос по готовности задачи");
       JsonPath response3 = RestAssured
               .given()
               .queryParam("token", token)
               .get("https://playground.learnqa.ru/ajax/api/longtime_job")
               .jsonPath();
       response3.prettyPrint();
       String taskStatus3 = response3.get("status");
       String result = response3.get("result");
       if ((taskStatus3.equals("Job is ready")) & (!result.equals(null))) {
           System.out.println("Задача № " + result + " выполнена успешно");
       } else {
           System.out.println("Ошибка выполнения задачи");
       }
   }
}



