package api.tests;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import org.apache.http.HttpStatus;
import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class InfoOrdersTests {

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
                .body("success", Is.is(true));
    }

    @Test
    @DisplayName("Test get info user orders")
    @Description("Этот тест проверяет получение заказов конкретного авторизованного пользователя")
    public void testGetInfoLoginUserOrders() {

        final UserSteps userSteps = new UserSteps();


        // Логинимся для получения токена
        String accessToken = userSteps.loginUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .path("accessToken"); // Предполагая, что метод login возвращает токен

        // Успешный запрос получения заказов
        given()
                .header("Authorization", accessToken) // Передаем токен в заголовках
                .when()
                .get("https://stellarburgers.nomoreparties.site/api/orders")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Test get info not login user orders")
    @Description("Этот тест проверяет получение заказов конкретного не авторизованного пользователя")
    public void testGetInfoNotLoginUserOrders() {

        final UserSteps userSteps = new UserSteps();

        // Логинимся для получения токена
        String accessToken = userSteps.loginUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .path("accessToken"); // Предполагая, что метод login возвращает токен

        given()
                .when()
                .get("https://stellarburgers.nomoreparties.site/api/orders")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED) // Ожидаем 401 UNAUTHORIZED
                .body("success", is(false))
                .body("message", is("You should be authorised"));
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