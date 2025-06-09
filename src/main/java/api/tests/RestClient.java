package api.tests;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class RestClient {
    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site";

    public static RequestSpecification getRequestSpecification() {
        return RestAssured.given()
                .baseUri(BASE_URI)
                .contentType("application/json");
    }
}