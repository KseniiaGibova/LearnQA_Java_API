package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static io.restassured.RestAssured.given;


public class ApiCoreRequests {
    @Step("GET request with token and auth cookie")
    public Response makeGetRequest(String url, String token, String cookie) {
        return given()
            .filter(new AllureRestAssured())
            .header(new Header("x-csrf-token", token))
            .cookie("auth_sid", cookie)
            .get(url)
            .andReturn();
    }

    @Step("GET request with auth cookie only")
    public Response makeGetRequestWithCookie(String url, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("GET request with token only")
    public Response makeGetRequestWithToken(String url, String token) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .get(url)
                .andReturn();
    }
    @Step("POST request")
    public Response makePostRequest(String url, Map<String, String> data) {
        return given()
                .filter(new AllureRestAssured())
                .body(data)
                .post(url)
                .andReturn();
    }

    @Step ("POST: Ex15-1 Email without @")
    public Response postRequestEmailWithoutAt(String url, Map<String, String> data){
        return given()
                .filter(new AllureRestAssured())
                .body(data)
                .post(url)
                .andReturn();
    }

    @Step ("POST: Ex15-2 request with empty field")
    public Response postRequestWithEmptyField(String url, Map<String, String> data){
        return given()
                .filter(new AllureRestAssured())
                .body(data)
                .post(url)
                .andReturn();
    }

    @Step ("POST: Ex15-3 request with too short firstName")
    public Response postRequestOneSignFirstName(String url, Map<String, String> data){
        return given()
                .filter(new AllureRestAssured())
                .body(data)
                .post(url)
                .andReturn();
    }

    @Step ("POST: Ex15-4 request with too long firstName")
    public Response postRequestTooManySignsFirstName(String url, Map<String, String> data) {
        return given()
                .filter(new AllureRestAssured())
                .body(data)
                .post(url)
                .andReturn();
    }

    @Step("Ex16 step1: acquiring user_id 2 credentials")
    public Response makePostRequest_user2(String url, Map<String, String> data) {
        return given()
                .filter(new AllureRestAssured())
                .body(data)
                .post(url)
                .andReturn();
    }

    @Step("Ex16 step2: user_id 3 Authorization with user_id 2 credentials")
    public Response getRequestUser2Authorization(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step ("Ex17 - login new user")
    public Response postRequestNewUserLogin(String url, Map<String, String> data){
        return given()
                .filter(new AllureRestAssured())
                .body(data)
                .post(url)
                .andReturn();
    }
    @Step ("Ex17-1 - PUT Unauthorized")
    public Response putUnathorized(String url, Map<String, String> data) {
        return given()
                .filter(new AllureRestAssured())
                .body(data)
                .put(url)
                .andReturn();
    }
    @Step("Ex17 - GET request with token and auth cookie - user data check")
    public Response getRequestUserDataCheck(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step ("Ex17-2 PUT Authorized another user")
    public Response putAuthorizedAnotherUser(String url, String token, String cookie, Map<String, String> data) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .body(data)
                .put(url)
                .andReturn();
    }
    @Step("Ex17-2-3 Data check")
    public Response getRequestCheckData(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step ("Ex17-3 PUT email")
    public Response putAuthorizedUser(String url, String token, String cookie, Map<String, String> data) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .body(data)
                .put(url)
                .andReturn();
    }

    @Step ("Ex17-4 PUT firstName")
    public Response putAuthorizedUserWrongName(String url, String token, String cookie, Map<String, String> data) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .body(data)
                .put(url)
                .andReturn();
    }

}