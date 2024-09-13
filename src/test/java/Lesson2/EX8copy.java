package Lesson2;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class EX8copy {

    String token;
    int seconds;

    //Map<String, String> queryParam = new HashMap<>();
   @Test
   public void getToken() {

       JsonPath response = RestAssured
               .given()
               .when()
               .get("https://playground.learnqa.ru/ajax/api/longtime_job")
               .jsonPath();
       response.prettyPrint();
       token = response.get("token");
       seconds = response.get("seconds");
       System.out.println(token);}

    @Test
    public void sendRequest()
       //первый запуск ок - данные сохранились - но как из первого теста во второй подтянуть токен?
            //Через Hashmap и переменную не получилось, второй тест не видит сохраненную пернеменную из первого(
            //Выводоим token на экран проверила, что после 1 теста переменная сохранилась
            //Если запускаю 1 тест, беру токен, подставляю вручную во 2 тест - получается

               {

                   JsonPath response2 = RestAssured
                    .given()
                    .queryParam("token",token)
                    .when()
                    .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                    .jsonPath();
            response2.prettyPrint(); }

     //       if (resp == "Job is NOT ready") {
       //         Thread.sleep(1000*seconds);
         //   }

           // response.prettyPrint();

}



