import APITests.UserSteps;
import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static APITests.ApiEndpoints.GET_ORDER_INFO;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class InfoOrdersTests {
    @Test
    @DisplayName("Test get info user orders")
    @Description("Этот тест проверяет получение заказов конкретного авторизованного пользователя")
    public void testGetInfoLoginUserOrders() {
        final UserSteps userSteps = new UserSteps();
        final Faker faker = new Faker();

        // Создание пользователя и получение токена
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().fullName();

        // Создание пользователя
        userSteps.createUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

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
        final Faker faker = new Faker();

        // Создание пользователя и получение токена
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().fullName();

        // Создание пользователя
        userSteps.createUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

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
}