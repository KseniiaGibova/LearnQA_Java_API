package testsDev;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


public class UserDeleteTestDev extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    String cookie;
    String header;
    int userId;

    @Test
    @Description("Ex18-1 Negative - Попытка удалить юзера с id=2")

    public void userTwo() {

        Map<String, String> data = new HashMap<>();
        data.put("email", "vinkotov@example.com");
        data.put("password", "1234");
        data = DataGenerator.getRegistrationData(data);

        //User 2 authorization
        Response auth = apiCoreRequests.postLoginUser2(
                "https://playground.learnqa.ru/api_dev/user/login", data);

        this.cookie = this.getCookie(auth, "auth_sid");
        this.header = this.getHeader(auth, "x-csrf-token");

        System.out.println(auth.asString());

        //User 2 delete
        Response responseDeleteUser = apiCoreRequests.deleteUser2(
                "https://playground.learnqa.ru/api_dev/user/2", this.header, this.cookie);
        System.out.println(responseDeleteUser.statusCode());
        System.out.println(responseDeleteUser.asString());

        //Проверка http кода и текста ошибки при удалении юзера 2
        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        Assertions.assertResponseTextEquals(responseDeleteUser,
                "{\"error\":\"Please, do not delete test users with ID 1, 2, 3, 4 or 5.\"}");


        //Get data user 2 - удаления не было, данные есть
        Response getDataUser2 = apiCoreRequests.checkUser2NotDeleted(
                "https://playground.learnqa.ru/api_dev/user/2", this.header, this.cookie);

        //Проверка наличия полей в авторизованном get запросе после неуспешного удаления юзера 2
        System.out.println(getDataUser2.asString());
        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        Assertions.assertJsonHasFields(getDataUser2, expectedFields);
    }

    @Test
    @Description("Ex18-2 Positive - Создание, авторизация, удаление пользователя")

    public void newUser() {
        //Create new user
        Map<String, String> data = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(data)
                .post("https://playground.learnqa.ru/api_dev/user/")
                .jsonPath();

        String userId = responseCreateAuth.getString("id"); //сохр id нового пользователя
        System.out.println(userId);

        //New user authorization

        Map<String, String> authData = new HashMap<>();
        authData.put("email", data.get("email"));
        authData.put("password", data.get("password"));

        Response auth = apiCoreRequests.post18NewUserLogin (
                "https://playground.learnqa.ru/api_dev/user/login", authData);

        this.cookie = this.getCookie(auth, "auth_sid");
        this.header = this.getHeader(auth, "x-csrf-token");
        this.userId = this.getIntFromJson(auth, "user_id");

        //Delete new user
        Response responseDeleteNewUser = apiCoreRequests.deleteNewUser(
                "https://playground.learnqa.ru/api_dev/user/" + userId, this.header, this.cookie);

        System.out.println(responseDeleteNewUser.asString());

        //проверка кода и текста ответа
        Assertions.assertResponseCodeEquals(responseDeleteNewUser, 200);
        Assertions.assertResponseTextEquals(responseDeleteNewUser,
                "{\"success\":\"!\"}");

        // Авторизованным GET-запросом проверяем, что пользователя и его данных больше нет
        Response responseUserData = apiCoreRequests.checkNoUser(
        "https://playground.learnqa.ru/api_dev/user/" + userId, this.header, this.cookie);

        System.out.println(responseUserData.asString());
        Assertions.assertResponseCodeEquals(responseUserData, 404);
        Assertions.assertResponseTextEquals(responseUserData,
                "User not found");

    }

    @Test
    @Description("Ex18-3 Negative - Попытка удалить другого юзера")

    public void deleteOtherUser() {

        Map<String, String> data = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(data)
                .post("https://playground.learnqa.ru/api_dev/user/")
                .jsonPath();

        String userId = responseCreateAuth.getString("id"); //сохр id нового пользователя
        System.out.println(userId);
        //С кредами данного юзера попытаюсь удалить пользователя 22. Проверим, что userId не равен 22
        if (userId == "22") {
            System.out.println("Please choose another userId for negative test!");
        }

        //New user authorization

        Map<String, String> authData = new HashMap<>();
        authData.put("email", data.get("email"));
        authData.put("password", data.get("password"));

        Response auth = apiCoreRequests.post18_3NewUserLogin (
                "https://playground.learnqa.ru/api_dev/user/login", authData);

        this.cookie = this.getCookie(auth, "auth_sid");
        this.header = this.getHeader(auth, "x-csrf-token");
        this.userId = this.getIntFromJson(auth, "user_id");

        //Delete new user
        Response responseDeleteOtherUser = apiCoreRequests.deleteUser22(
                "https://playground.learnqa.ru/api_dev/user/22" , this.header, this.cookie);

        System.out.println(responseDeleteOtherUser.asString());

                //Проверка http кода и текста ошибки при удалении юзера 2
        Assertions.assertResponseCodeEquals(responseDeleteOtherUser, 400);
        Assertions.assertResponseTextEquals(responseDeleteOtherUser,
                "{\"error\":\"This user can only delete their own account.\"}");
    }

    }


