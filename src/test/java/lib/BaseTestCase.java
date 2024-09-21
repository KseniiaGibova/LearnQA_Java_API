package lib;

import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.hasKey;

public class BaseTestCase {
    protected String getHeader(Response Response, String test) {

        Headers headers = Response.getHeaders();

        assertTrue(headers.hasHeaderWithName(test), "No header " + test + " in Response");
        return headers.getValue(test);
    }

    protected String getCookie(Response Response, String test) {
Map<String, String> cookies = Response.getCookies();
assertTrue(cookies.containsKey(test), "No cookies " + test + " in Response");
return cookies.get(test);
    }

    protected int getIntFromJson(Response Response, String test) {
        Response.then().assertThat().body("$", hasKey(test));
        return Response.jsonPath().getInt(test);
    }
}