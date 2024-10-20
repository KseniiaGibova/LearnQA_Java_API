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
}