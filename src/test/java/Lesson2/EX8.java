package Lesson2;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

public class EX8 {

        @Test
        public void url()

        {
            Response response = RestAssured
                    //.given(token)
                    .when()
                    .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                    .andReturn();
             response.prettyPrint();

            // String abc = response.getBody(token);

        }
    }

