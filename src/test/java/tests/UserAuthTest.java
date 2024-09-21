package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import lib.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UserAuthTest extends BaseTestCase {

    String cookie;
    String header;
    int userIdOnAuth;

    @BeforeEach
    public void login() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response auth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        this.cookie = this.getCookie(auth, "auth_sid");
        this.header = this.getHeader(auth, "x-csrf-token");
        this.userIdOnAuth = this.getIntFromJson(auth, "user_id");
    }

@Test
    public void testAuth(){
    Response test = RestAssured
            .given()
            .header("x-csrf-token", this.header)
            .cookie("auth_sid", this.cookie)
            .get("https://playground.learnqa.ru/api/user/auth")
            .andReturn();

    Assertions.assertJsonByName(test, "user_id", this.userIdOnAuth);


}

@ParameterizedTest
@ValueSource(strings={"cookie", "headers"})
    public void negTest(String abc) {
    RequestSpecification spec = RestAssured.given();
    spec.baseUri("https://playground.learnqa.ru/api/user/auth");

    if(abc.equals("cookie")){
        spec.cookie("auth_sid", this.cookie);}
    else if (abc.equals("headers")) {
        spec.header("x-csrf-token", this.header);}
    else {
        throw new IllegalArgumentException("abc value: "+abc+" is wrong");
    }

    Response respForCheck =  spec.get().andReturn();
    Assertions.assertJsonByName(respForCheck, "user_id", 0);
  }
}
