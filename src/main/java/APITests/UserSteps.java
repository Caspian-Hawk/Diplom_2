package APITests;

import APITests.dto.UserModel;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static APITests.ApiEndpoints.*;

public class UserSteps {

    @Step("Create user")
    public ValidatableResponse createUser(String email, String password, String name) {
        UserModel user = new UserModel(email, password, name);
        return RestClient.getRequestSpecification()
                .body(user)
                .when()
                .post(CREATE_USER)
                .then();
    }

    @Step("Login user")
    public ValidatableResponse loginUser(String email, String password, String name) {
        UserModel user = new UserModel(email, password, name);
        return RestClient.getRequestSpecification()
                .body(user)
                .when()
                .post(LOGIN_USER)
                .then();
    }

    @Step("Create user whiteout email")
    public ValidatableResponse createUserWitheOutEmail(String password, String name) {
        UserModel user = new UserModel(null, password, name);
        return RestClient.getRequestSpecification()
                .body(user)
                .when()
                .post(CREATE_USER)
                .then();
    }

    @Step("Create user whiteout password")
    public ValidatableResponse createUserWitheOutPassword(String email, String name) {
        UserModel user = new UserModel(email, null, name);
        return RestClient.getRequestSpecification()
                .body(user)
                .when()
                .post(CREATE_USER)
                .then();
    }

    @Step("Create user whiteout name")
    public ValidatableResponse createUserWitheOutName(String email, String password) {
        UserModel user = new UserModel(email, password, null);
        return RestClient.getRequestSpecification()
                .body(user)
                .when()
                .post(CREATE_USER)
                .then();
    }

    @Step("Get user info")
    public ValidatableResponse getUserInfo(String accessToken) {
        return RestClient.getRequestSpecification()
                .header("Authorization", accessToken)
                .when()
                .get(GET_USER_INFO)
                .then();
    }

    @Step("Update user info email")
    public ValidatableResponse updateUserInfoEmail(String token, String updatedEmail) {
        return RestClient.getRequestSpecification()
                .header("Authorization", token)
                .body("{\"email\": \"" + updatedEmail + "\"}")
                .when()
                .patch("https://stellarburgers.nomoreparties.site/api/auth/user")
                .then();
    }

    @Step("Update user info password")
    public ValidatableResponse updateUserInfoPassword(String token, String updatedPassword) {
        return RestClient.getRequestSpecification()
                .header("Authorization", token)
                .body("{\"password\": \"" + updatedPassword + "\"}")
                .when()
                .patch("https://stellarburgers.nomoreparties.site/api/auth/user")
                .then();
    }

    @Step("Update user info name")
    public ValidatableResponse updateUserInfoName(String token, String updatedName) {
        return RestClient.getRequestSpecification()
                .header("Authorization", token)
                .body("{\"name\": \"" + updatedName + "\"}")
                .when()
                .patch("https://stellarburgers.nomoreparties.site/api/auth/user")
                .then();
    }

    @Step("Delete user")
    public ValidatableResponse deleteUser(int id) {
        return RestClient.getRequestSpecification()
                .pathParam("id", id)
                .when()
                .delete(DELETE_USER)
                .then();
    }
}
