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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UserEditTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    String cookie;
    String header;
    int userId;
    String oldMail;
    String email;


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
        Map<String, String> authData = new HashMap<>();
        authData.put("email", data.get("email"));
        authData.put("password", data.get("password"));

        Response responseGetAuth = RestAssured
                .given()
                .body(data)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
//edit - что-то поменять новому пользователю - через put
        String newName = "Ananas";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .body(editData)
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        //Получение и сравнение новых данных пользователя

        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();
        System.out.println(responseUserData.asString());
        Assertions.assertJsonByName(responseUserData, "firstName", newName);
    }



    //Ex 17 starts here


    @BeforeEach
    @Description("Создаем нового юзера, затем авторизуем его")
    public void testEditJustCreatedTest2() {
        //Create new user
        Map<String, String> data = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(data)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();

        String userId = responseCreateAuth.getString("id"); //сохр id нового пользователя
        System.out.println(userId);

        //New user authorization

        Map<String, String> authData = new HashMap<>();
        authData.put("email", data.get("email"));
        authData.put("password", data.get("password"));


        Response auth = apiCoreRequests.postRequestNewUserLogin(
                "https://playground.learnqa.ru/api/user/login", authData);

        this.cookie = this.getCookie(auth, "auth_sid");
        this.header = this.getHeader(auth, "x-csrf-token");
        this.userId = this.getIntFromJson(auth, "user_id");

                  }


    @Test
    @Description("Ex 17-1 - Негативный кейс - изменение данных пользователя, будучи неавторизованным")
    @DisplayName("Ex 17-1 Negative PUT Request - unauthorized")
    public void putUnauthorized() {

        //меняем фамилию новому пользователю - через put - будучи неавторизованными
        String newName = "Kapibarov";
        Map<String, String> editData = new HashMap<>();
        editData.put("lastName", newName);

        Response responseEditUser = apiCoreRequests.putUnathorized("https://playground.learnqa.ru/api/user/" + userId,
                editData);

        System.out.println(responseEditUser.asString());
        //проверка ошибки в негативном PUT запросе
        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser, "{\"error\":\"Auth token not supplied\"}");

        // Авторизованным GET-запросом проверяем данные пользователя
        Response responseUserData = apiCoreRequests.getRequestUserDataCheck(
                "https://playground.learnqa.ru/api/user/" + userId, this.header, this.cookie);

        System.out.println(responseUserData.asString());
        //проверка: lastName не поменялось
        Assertions.assertJsonByName(responseUserData, "lastName", "learnqa");

    }

    @Test
    @Description("Ex 17-2 - Негативный кейс - изменение данных пользователя, будучи авторизованным под другим юзером")
    @DisplayName("Ex 17-2 Negative PUT Request - authorized as another user")
    public void authorizedAsAnotherUser() {

        //меняем имя новому пользователю - через put - будучи неавторизованными
        String newName = "Kapibara";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);
     //   userId = userId - 1;
//С кредами данного юзера попытаюсь изменить пользователя 23. Проверим, что userId не равен 23
        if (userId == 23) {
            System.out.println("Please choose another userId for negative test!");
        }
        Response responseEditUser = apiCoreRequests.putAuthorizedAnotherUser(
                "https://playground.learnqa.ru/api/user/23", this.header, this.cookie,
                editData);

        System.out.println(responseEditUser.asString());
        //проверка ошибки в негативном PUT запросе
        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser,
                "{\"error\":\"This user can only edit their own data.\"}");

        // Авторизованным GET-запросом проверяем данные пользователя
        Response responseUserData = apiCoreRequests.getRequestUserDataCheck(
                "https://playground.learnqa.ru/api/user/" + userId, this.header, this.cookie);

        System.out.println(responseUserData.asString());
        //проверка: lastName не поменялось
        Assertions.assertJsonByName(responseUserData, "firstName", "learnqa");
    }


//Тест 17-3

    @Test
    @Description("Ex 17-3 - Негативный кейс - изменение email авторизованного пользователя на значение без @")
    @DisplayName("Ex 17-3 Negative PUT Request - change email - wrong format")
    public void changeEmailWrongFormat() {

        //меняем email новому пользователю - через put - будучи авторизованными
        String newEmail = "kapibaratest.com";
        Map<String, String> editData = new HashMap<>();
        editData.put("email", newEmail);

        Response responseEditUser = apiCoreRequests.putAuthorizedUser(
                "https://playground.learnqa.ru/api/user/" + userId, this.header, this.cookie,
                editData);

        System.out.println(responseEditUser.asString());
        //проверка ошибки в негативном PUT запросе
        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser,
               "{\"error\":\"Invalid email format\"}");

        // Авторизованным GET-запросом проверяем данные пользователя
        Response responseUserData = apiCoreRequests.getRequestUserDataCheck(
                "https://playground.learnqa.ru/api/user/" + userId, this.header, this.cookie);

        System.out.println(responseUserData.asString());

        //проверка: email не поменялся
        assertNotEquals("kapibaratest.com" ,email, "wrong email");
       }

    @Test
    @Description("Ex 17-4 - Негативный кейс - изменение fristName авторизованного пользователя на значение в 1 символ")
    @DisplayName("Ex 17-4 Negative PUT Request - change firstName - 1 sign")
    public void changeFirstNameWrongFormat() {

        //меняем firstName новому пользователю - через put - будучи авторизованными
        String newName = "B";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests.putAuthorizedUserWrongName(
                "https://playground.learnqa.ru/api/user/" + userId, this.header, this.cookie,
                editData);

        System.out.println(responseEditUser.asString());
        //проверка ошибки в негативном PUT запросе
        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser,
                "{\"error\":\"The value for field `firstName` is too short\"}");

        // Авторизованным GET-запросом проверяем данные пользователя
        Response responseUserData = apiCoreRequests.getRequestUserDataCheck(
                "https://playground.learnqa.ru/api/user/" + userId, this.header, this.cookie);

        System.out.println(responseUserData.asString());

        //проверка: lastName не поменялось
        Assertions.assertJsonByName(responseUserData, "firstName", "learnqa");
    }

}



