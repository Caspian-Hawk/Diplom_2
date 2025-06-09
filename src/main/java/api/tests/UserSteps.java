package api.tests;

import api.tests.dto.UserModel;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static api.tests.ApiEndpoints.*;

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

    @Step("Create user without name")
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
    public ValidatableResponse updateUserInfoEmail(String token, String updatedEmail, String existingPassword, String existingName) {
        UserModel user = new UserModel(updatedEmail, existingPassword, existingName);

        RequestSpecification request = RestClient.getRequestSpecification().body(user);
        if (!token.isEmpty()) {
            request.header("Authorization", token);
        }

        return request.when()
                .patch(UPDATE_USER_INFO)
                .then();
    }

    @Step("Update user info password")
    public ValidatableResponse updateUserInfoPassword(String token, String existingEmail, String updatedPassword, String existingName) {
        UserModel user = new UserModel(existingEmail, updatedPassword, existingName);

        RequestSpecification request = RestClient.getRequestSpecification().body(user);
        if (!token.isEmpty()) {
            request.header("Authorization", token);
        }

        return request.when()
                .patch(UPDATE_USER_INFO)
                .then();
    }

    @Step("Update user info name")
    public ValidatableResponse updateUserInfoName(String token, String existingEmail, String existingPassword, String updatedName) {
        UserModel user = new UserModel(existingEmail, existingPassword, updatedName);

        RequestSpecification request = RestClient.getRequestSpecification().body(user);
        if (!token.isEmpty()) {
            request.header("Authorization", token);
        }

        return request.when()
                .patch(UPDATE_USER_INFO)
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