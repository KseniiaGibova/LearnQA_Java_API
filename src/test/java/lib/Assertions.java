package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.*;

public class Assertions {
public static void assertJsonByName (Response Response, String test, int expectedValue){
    Response
            .then()
            .assertThat()
            .body("$", hasKey(test));

    int value = Response.jsonPath().getInt(test);
    assertEquals(expectedValue, value, "Unexpected JSON value");
  }
}
