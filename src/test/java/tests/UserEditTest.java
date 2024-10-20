package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserEditTest extends BaseTestCase {
@Test
    public void testEditJustCreatedTest() {
    //create User
    Map<String, String> data = DataGenerator.getRegistrationData();

    JsonPath responseCreateAuth = RestAssured
            .given()
            .body(data)
            .post("https://playground.learnqa.ru/api/user/")
            .jsonPath();

    String userId = responseCreateAuth.getString("id"); //сохр id нового пользователя

    // new user login
  Map <String, String>  authData = new HashMap<>();
authData.put("email", data.get("email"));
authData.put("password", data.get("password"));

Response responseGetAuth = RestAssured
        .given()
        .body(data)
        .post("https://playground.learnqa.ru/api/user/login")
        .andReturn();
//edit - что-то поменять новому пользователю - через put
    String newName = "Changed Name";
    Map <String, String> editData = new HashMap<>();
    editData.put("firstName", newName);

    Response responseEditUser = RestAssured
            .given()
            .header ("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
            .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
            .body(editData)
            .put("https://playground.learnqa.ru/api/user/" + userId)
            .andReturn();

    //Получение и сравнение новых данных пользователя

    Response responseUserData = RestAssured
            .given()
            .header ("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
            .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
            .get("https://playground.learnqa.ru/api/user/"+ userId)
            .andReturn();
    System.out.println(responseUserData.asString());
    Assertions.assertJsonByName(responseUserData, "firstName", newName);
  }
}
