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
import static org.hamcrest.Matchers.is;


public class ChangingUserDataTests {

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
    @DisplayName("Test possibly update email user with login")
    @Description("Этот тест проверяет возможность обновление email авторизованного пользователя")
    public void testPossiblyUpdateEmailUserWithLogin() {

        final UserSteps userSteps = new UserSteps();
        final Faker faker = new Faker();

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        // Логинимся и получаем токен
        String accessToken = userSteps
                .loginUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .extract().path("accessToken");

        // Обновление информации о пользователе
        String updatedEmail = faker.internet().emailAddress();
        userSteps.updateUserInfoEmail(accessToken, updatedEmail, password, name)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true))
                .body("user.email", is(updatedEmail))
                .body("user.name", is(name));

        // Получение информации о пользователе
        userSteps.getUserInfo(accessToken)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true))
                .body("user.email", is(updatedEmail))
                .body("user.name", is(name));
    }

    @Test
    @DisplayName("Test possibly update password user with login")
    @Description("Этот тест проверяет возможность обновление password авторизованного пользователя")
    public void testPossiblyUpdatePasswordUserWithLogin() {

        final UserSteps userSteps = new UserSteps();

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        // Логинимся и получаем токен
        String accessToken = userSteps.loginUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .extract().path("accessToken");

        // Обновление информации о пользователе
        String updatedPassword = faker.internet().password();
        userSteps.updateUserInfoPassword(accessToken, email, updatedPassword, name)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        // Проверка входа с новым паролем
        userSteps.loginUser(email, updatedPassword, name)
                .statusCode(HttpStatus.SC_OK)
                .extract().path("accessToken");
    }

    @Test
    @DisplayName("Test possibly update name user with login")
    @Description("Этот тест проверяет возможность обновление name авторизованного пользователя")
    public void testPossiblyUserUpdateNameWithLogin() {

        final UserSteps userSteps = new UserSteps();

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        // Логинимся и получаем токен
        String accessToken = userSteps
                .loginUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .extract().path("accessToken");

        // Обновление информации о пользователе
        String updatedName = faker.name().fullName();
        userSteps.updateUserInfoName(accessToken, email, password, updatedName)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true))
                .body("user.email", is(email))
                .body("user.name", is(updatedName));

        // Получение информации о пользователе
        userSteps.getUserInfo(accessToken)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true))
                .body("user.email", is(email))
                .body("user.name", is(updatedName));
    }

    @Test
    @DisplayName("Test not possibly update email user without login")
    @Description("Этот тест проверяет, что обновление email пользователя невозможно без авторизации")
    public void testNotPossiblyUpdateEmailUserWithoutLogin() {
        final UserSteps userSteps = new UserSteps();

        // Обновление информации о пользователе без токена
        String updatedEmail = faker.internet().emailAddress();
        userSteps.updateUserInfoEmail("", updatedEmail, null, null)
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }

    @Test
    @DisplayName("Test not possibly update password user without login")
    @Description("Этот тест проверяет, что обновление password пользователя невозможно без авторизации")
    public void testNotPossiblyUpdatePasswordUserWithoutLogin() {
        final UserSteps userSteps = new UserSteps();

        // Обновление информации о пользователе без токена
        String updatedPassword = faker.internet().password();
        userSteps.updateUserInfoPassword("", null, updatedPassword, null)
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }

    @Test
    @DisplayName("Test not possibly update name user without login")
    @Description("Этот тест проверяет, что обновление name пользователя невозможно без авторизации")
    public void testNotPossiblyUpdateNameUserWithoutLogin() {
        final UserSteps userSteps = new UserSteps();

        // Обновление информации о пользователе без токена
        String updatedName = faker.name().fullName();
        userSteps.updateUserInfoName("", null, null, updatedName)
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }

    @Test
    @DisplayName("Test not possibly update the same email user with login")
    @Description("Этот тест проверяет невозможность обновления email авторизованного пользователя на тот же самый ")
    public void testNotPossiblyUserUpdateTheSameEmailWithLogin() {

        final UserSteps userSteps = new UserSteps();

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        // Логинимся и получаем токен
        String accessToken = userSteps.loginUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .extract().path("accessToken");

        // Попытка обновления информации о пользователе на тот же email
        userSteps.updateUserInfoEmail(accessToken, email, name, password)
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", is("User with such email already exists"));
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