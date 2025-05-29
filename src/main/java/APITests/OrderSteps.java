package APITests;

import APITests.dto.OrderModel;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static APITests.ApiEndpoints.CREATE_ORDER;

public class OrderSteps {

    @Step
    public ValidatableResponse createOrder(String [] ingredients) {
        OrderModel order = new OrderModel(ingredients);
        return RestClient.getRequestSpecification()
                .body(order)
                .when()
                .post(CREATE_ORDER)
                .then();
    }
}