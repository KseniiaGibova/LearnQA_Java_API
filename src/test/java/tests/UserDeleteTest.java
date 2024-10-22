package tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


public class UserDeleteTest extends BaseTestCase  {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    String cookie;
    String header;
    int userId;

@Test
    @Description("Попытка удалить юзера с id=2")

    public void userTwo(){

    Map<String, String> data = new HashMap<>();
    data.put("email", "vinkotov@example.com");
    data.put("password", "1234");
    data = DataGenerator.getRegistrationData(data);

       Response auth = apiCoreRequests.postRequestNewUserLogin(
            "https://playground.learnqa.ru/api/user/login", data);

    this.cookie = this.getCookie(auth, "auth_sid");
    this.header = this.getHeader(auth, "x-csrf-token");
    this.userId = this.getIntFromJson(auth, "user_id");

    Response responseDeleteUser = apiCoreRequests.deleteUser2(
            "https://playground.learnqa.ru/api/user/2", this.header, this.cookie, data);


}



 //   https://playground.learnqa.ru/api/user/2    - delete




}
