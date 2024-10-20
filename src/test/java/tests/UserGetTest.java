package tests;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserGetTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
@Test
    public void testGetUserDataNotAuth() {
    Response responseUserData = RestAssured
            .get("https://playground.learnqa.ru/api/user/2")
            .andReturn();
    System.out.println(responseUserData.asString());

    Assertions.assertJsonHasField(responseUserData, "username");
    Assertions.assertJsonHasNotField(responseUserData, "firstName");
    Assertions.assertJsonHasNotField(responseUserData, "lastName");
    Assertions.assertJsonHasNotField(responseUserData, "email");
}

@Test
    public void testGetUserDetailsAuthAsSameUser() {
    Map<String, String> authData = new HashMap<>();
    authData.put("email", "vinkotov@example.com");
    authData.put("password", "1234");

    Response responseGetAuth = RestAssured
            .given()
            .body(authData)
            .post("https://playground.learnqa.ru/api/user/login")
            .andReturn();

   String header = this.getHeader(responseGetAuth, "x-csrf-token");
   String cookie = this.getCookie(responseGetAuth, "auth_sid");
//Получили первым запросом токен и куки, пошли делать второй запрос

    Response responseUserData = RestAssured
            .given()
            .header("x-csrf-token", header)
            .cookie("auth_sid", cookie)
            .get("https://playground.learnqa.ru/api/user/2")
            .andReturn();
//проверка наличия полей в авторизованном запросе
    System.out.println(responseUserData.asString());
    String[] expectedFields = {"username", "firstName", "lastName", "email"};
    Assertions.assertJsonHasFields(responseUserData, expectedFields);
    }


    String cookie;
    String header;
    @Test
    @Description("Ex16 Negative: попытка авторизоваться под пользователем 3 с кредами пользователя 2")
    public void testGetUserDetailsAuthAsOtherUser() {
        Map<String, String> data = new HashMap<>();
        data.put("email", "vinkotov@example.com");
        data.put("password", "1234");

        Response responseGetAuth = apiCoreRequests.makePostRequest_user2(
                "https://playground.learnqa.ru/api/user/login", data);

        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
// Получили первым запросом токен и куки юзера с id=2,
// с ними пытаемся авторизоваться и получить данные юзера с id=3

        Response responseUserData = apiCoreRequests.getRequestUser2Authorization(
                "https://playground.learnqa.ru/api/user/3", this.header, this.cookie);

        System.out.println(responseUserData.asString());
//проверка наличия полей в авторизованном запросе - долженн прийти только username
        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "lastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");
    }
}
