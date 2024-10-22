package testsDev;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

@Epic("Authorization cases")
@Feature("Authorization")
public class UserAuthTestDev extends BaseTestCase {

     String cookie;
     String header;
     int userIdOnAuth;
     private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

     @BeforeEach
     public void loginUser() {
         Map<String, String> data = new HashMap<>();
         data.put("email", "vinkotov@example.com");
         data.put("password", "1234");

         Response auth = apiCoreRequests
                 .makePostRequest("https://playground.learnqa.ru/api/user/login", data);

         this.cookie = this.getCookie(auth,"auth_sid");
         this.header = this.getHeader(auth,"x-csrf-token");
         this.userIdOnAuth = this.getIntFromJson(auth,"user_id");
     }
       @Test
       @Description("Successful authorization by email and password")
       @DisplayName("Test positive auth user")
       public void testAuthUser(){
           Response responseCheckAuth = apiCoreRequests
                   .makeGetRequest("https://playground.learnqa.ru/api/user/auth",
                           this.header,
                           this.cookie);

           Assertions.assertJsonByName(responseCheckAuth, "user_id", this.userIdOnAuth);
                                 }
@Description("Check authorization without sending auth cookie or token")
@DisplayName("Test negative auth user")
       @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    public void testNegativeAuthUser (String condition) {

    if (condition.equals("cookie")) {
        Response responseForCheck = apiCoreRequests.makeGetRequestWithCookie(
                "https://playground.learnqa.ru/api/user/auth",
                this.cookie);
        Assertions.assertJsonByName(responseForCheck, "user_id", 0);
    } else if (condition.equals("headers")) {
        Response responseForCheck = apiCoreRequests.makeGetRequestWithToken(
                "https://playground.learnqa.ru/api/user/auth",
                this.header);
        Assertions.assertJsonByName(responseForCheck, "user_id", 0);
    } else {
        throw new IllegalArgumentException("Condition value is not known: " + condition);
    }
}
}
