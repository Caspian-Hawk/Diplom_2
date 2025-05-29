import APITests.OrderSteps;

import APITests.UserSteps;
import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Test;
import static org.hamcrest.core.Is.is;

public class CreateOrderTests {

    private final OrderSteps orderSteps = new OrderSteps();
    private final UserSteps userSteps = new UserSteps();
    private final Faker faker = new Faker();

//    @Test
//    public void testGetIngredients() {
//        RestAssured.given()
//                .when()
//                .get("https://stellarburgers.nomoreparties.site/api/ingredients")
//                .then()
//                .statusCode(HttpStatus.SC_OK)
//                .log().all();
//    }

    @Test
    @DisplayName("Test possibly create order without login")
    @Description("Этот тест проверяет, что можно создать заказ без авторизации")
    public void testPossiblyCreateOrderWithoutLogin() {

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        String[] ingredients = new String[] {
                "61c0c5a71d1f82001bdaaa6d",
                "61c0c5a71d1f82001bdaaa6f"
        };

        orderSteps.createOrder(ingredients)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Test possibly create order with login")
    @Description("Этот тест проверяет, что можно создать заказ после авторизации")
    public void testPossiblyCreateOrderWithLogin() {

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        String[] ingredients = new String[] {
                "61c0c5a71d1f82001bdaaa6d",
                "61c0c5a71d1f82001bdaaa6f"
        };

        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().fullName();

        userSteps
                .createUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
        userSteps
                .loginUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        orderSteps.createOrder(ingredients)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Test not possibly create order without ingredients")
    @Description("Этот тест проверяет, что нельзя создать заказ без ингредиентов")
    public void testNotPossiblyCreateOrderWithoutIngredients() {

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        String[] ingredients = new String[] {
        };

        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().fullName();

        userSteps
                .createUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
        userSteps
                .loginUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        orderSteps.createOrder(ingredients)
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", Matchers.is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Test not possibly create order with wrong hash ingredients")
    @Description("Этот тест проверяет, что нельзя создать заказ с неверным хешом ингредиента")
    public void testNotPossiblyCreateOrderWithWrongHashIngredients() {

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        String[] ingredients = new String[] {
                "61c0c5a71d1f82001bdaaa6d",
                "61c0c5a71d1f8g001bdaaa6f"
        };

        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().fullName();

        userSteps
                .createUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
        userSteps
                .loginUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        orderSteps.createOrder(ingredients)
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}