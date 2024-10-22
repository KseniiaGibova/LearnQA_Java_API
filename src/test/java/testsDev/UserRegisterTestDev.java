package testsDev;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterTestDev extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";

        Map<String, String> data = new HashMap<>();
        data.put("email", email);
        data = DataGenerator.getRegistrationData(data);

        Response responseCreateAuth = RestAssured
                .given()
                .body(data)
                .post("https://playground.learnqa.ru/api_dev/user/")
                .andReturn();

        System.out.println(responseCreateAuth.asString());
        System.out.println(responseCreateAuth.statusCode());

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");

    }

    @Test
    public void testCreateUserSuccess() {
        String email = DataGenerator.getRandomEmail();
        Map<String, String> data = DataGenerator.getRegistrationData();

        Response responseCreateAuth = RestAssured
                .given()
                .body(data)
                .post("https://playground.learnqa.ru/api_dev/user/")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        //Печать необязательно
        System.out.println(responseCreateAuth.asString());
        Assertions.assertJsonHasField(responseCreateAuth, "id");

    }

    @Test
    @Description("Ex15-1 Создание пользователя без @ в email - нет регистрации")
    @DisplayName("Ex15-1 Negative - no '@' in email")
    public void createUserEmailWithoutAt(){
           String email = "vinkotovexample.com";
           Map<String, String> data = DataGenerator.getRegistrationData();
           data.put("email", email);

        Response responseCreateAuth = apiCoreRequests.postRequestEmailWithoutAt(
                "https://playground.learnqa.ru/api_dev/user/", data);

        System.out.println(responseCreateAuth.asString()); //Необязвательно, но проще отслеживать текст ошибки
        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");
    }


    @ParameterizedTest
    @Description("Ex15-2 Создание пользователя без указания одного из полей - нет регистрации")
    @DisplayName("Ex15-2 Negative - missing mandatory field")

    @ValueSource (strings = {"email", "password", "username", "firstName", "lastName"})
    public void createUserWithoutRequiredField(String emptyField){

        Map<String, String> data = DataGenerator.getRegistrationData();
        data.put(emptyField, null);
        Response responseCreateAuth = apiCoreRequests.postRequestWithEmptyField(
                "https://playground.learnqa.ru/api_dev/user/", data);

        System.out.println(responseCreateAuth.asString()); //Хочу посмотерть данные
        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth,
                "The following required params are missed: "+ emptyField);
    }

    @Test
    @Description("Ex15-3 Создание пользователя с именем в 1 символ - без регистрации")
    @DisplayName("Ex15-3 Negative - 1 sign firstName")
    public void createUserShortName(){
        String email = DataGenerator.getRandomEmail();
        String firstName = "A";
        Map<String, String> data = DataGenerator.getRegistrationData();
        data.put("firstName", firstName);
        Response responseCreateAuth = apiCoreRequests.postRequestOneSignFirstName(
                "https://playground.learnqa.ru/api_dev/user/", data);

        System.out.println(responseCreateAuth.asString()); //Необязвательно, но проще отслеживать текст ошибки
        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'firstName' field is too short");
    }

    @Test
    @Description("Ex15-4 Создание пользователя с именем в 251 символ (гранчное значение, с 250 - успех) - без регистрации")
    @DisplayName("Ex15-4 Negative - 251 sign firstName")
    public void createUserLongName(){
        String email = DataGenerator.getRandomEmail();
        String firstName = "JunitfivisthecurrentgenerationoftheJunittestingframeworkwhichprovidesamodernfoundationfordevelopersidetestingontheJVMThisincludesfocusingonJavaeightandaboveaswellasenablingmanydifferentstylesoftestingJUnitfivebeganastheresultoftheJUnitLambdaprojectttt";
        Map<String, String> data = DataGenerator.getRegistrationData();
        data.put("firstName", firstName);
        Response responseCreateAuth = apiCoreRequests.postRequestTooManySignsFirstName(
                        "https://playground.learnqa.ru/api_dev/user/", data);

        System.out.println(responseCreateAuth.asString()); //Необязвательно, но проще отслеживать текст ошибки
        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'firstName' field is too long");
    }
}