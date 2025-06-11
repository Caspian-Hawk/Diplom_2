package api.tests;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.core.Is.is;

public class CreateOrderTests {

    private final OrderSteps orderSteps = new OrderSteps();
    private final UserSteps userSteps = new UserSteps();
    private final Faker faker = new Faker();

    // Поля для хранения данных пользователя
    private String email;
    private String password;
    private String name;
    private Integer userId;

    @Before
    public void setUp() {
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        email = faker.internet().emailAddress();
        password = faker.internet().password();
        name = faker.name().fullName();

        // Создание пользователя
        userSteps.createUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        // Логин пользователя
        userSteps.loginUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Test possibly create order without login")
    @Description("Этот тест проверяет, что можно создать заказ без авторизации")
    public void testPossiblyCreateOrderWithoutLogin() {
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
        String[] ingredients = new String[] {
                "61c0c5a71d1f82001bdaaa6d",
                "61c0c5a71d1f82001bdaaa6f"
        };

        orderSteps.createOrder(ingredients)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Test not possibly create order without ingredients")
    @Description("Этот тест проверяет, что нельзя создать заказ без ингредиентов")
    public void testNotPossiblyCreateOrderWithoutIngredients() {
        String[] ingredients = new String[] {};

        orderSteps.createOrder(ingredients)
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", Matchers.is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Test not possibly create order with wrong hash ingredients")
    @Description("Этот тест проверяет, что нельзя создать заказ с неверным хешом ингредиента")
    public void testNotPossiblyCreateOrderWithWrongHashIngredients() {
        String[] ingredients = new String[] {
                "61c0c5a71d1f82001bdaaa6d",
                "61c0c5a71d1f8g001bdaaa6f"
        };

        orderSteps.createOrder(ingredients)
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @After
    public void tearDown() {
        // Удаляем пользователя после теста
        if (userId != null) {
            userSteps.deleteUser(userId)
                    .statusCode(HttpStatus.SC_OK)
                    .body("success", Is.is(true));
        }
    }
}