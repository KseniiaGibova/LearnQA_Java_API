package lib;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.hasKey;

public class BaseTestCase
// Методы для получения значений куки и хедеров из (распарсенного) ответа сервера по имени
 {
    protected String getHeader(Response Response, String name) {

        Headers headers = Response.getHeaders();

        assertTrue(headers.hasHeaderWithName(name), "No header " + name + " in Response");
        return headers.getValue(name);
    }
    protected String getCookie(Response Response, String name) {
        Map<String, String> cookies = Response.getCookies();
        assertTrue(cookies.containsKey(name), "No cookies " + name + " in Response");
        return cookies.get(name);
    }

    protected int getIntFromJson(Response Response, String name) {
        Response.then().assertThat().body("$", hasKey(name));
        return Response.jsonPath().getInt(name);
    }
}