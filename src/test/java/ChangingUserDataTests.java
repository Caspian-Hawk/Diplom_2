import APITests.UserSteps;
import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import org.apache.http.HttpStatus;
import org.junit.Test;
import static org.hamcrest.Matchers.is;

public class ChangingUserDataTests {

    @Test
    @DisplayName("Test possibly update email user with login")
    @Description("Этот тест проверяет возможность обновление email авторизованного пользователя")
    public void testPossiblyUpdateEmailUserWithLogin() {

        final UserSteps userSteps = new UserSteps();
        final Faker faker = new Faker();

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().fullName();

        userSteps
                .createUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        // Логинимся и получаем токен
        String accessToken = userSteps
                .loginUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .extract().path("accessToken");

        // Получение информации о пользователе
        userSteps.getUserInfo(accessToken)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true))
                .body("user.email", is(email))
                .body("user.name", is(name));

        // Обновление информации о пользователе
        String updatedEmail = faker.internet().emailAddress();
        userSteps.updateUserInfoEmail(accessToken, updatedEmail)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true))
                .body("user.email", is(updatedEmail))
                .body("user.name", is(name));

        // Проверка (дублирование) получения информации о пользователе
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
        final Faker faker = new Faker();

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().fullName();

        userSteps.createUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        // Логинимся и получаем токен
        String accessToken = userSteps.loginUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .extract().path("accessToken");

        // Получение информации о пользователе
        userSteps.getUserInfo(accessToken)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true))
                .body("user.email", is(email))
                .body("user.name", is(name));

        // Обновление информации о пользователе
        String updatedPassword = faker.internet().password();
        userSteps.updateUserInfoPassword(accessToken, updatedPassword)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        // Проверка (дублирование) получения информации о пользователе
        userSteps.getUserInfo(accessToken)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true))
                .body("user.email", is(email))
                .body("user.name", is(name));

        // Заново входим с новым паролем, чтобы проверить, что обновление прошло успешно
        userSteps.loginUser(email, updatedPassword, name)
                .statusCode(HttpStatus.SC_OK)
                .extract().path("accessToken");
    }

    @Test
    @DisplayName("Test possibly update name user with login")
    @Description("Этот тест проверяет возможность обновление name авторизованного пользователя")
    public void testPossiblyUserUpdateNameWithLogin() {

        final UserSteps userSteps = new UserSteps();
        final Faker faker = new Faker();

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().fullName();

        userSteps
                .createUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        // Логинимся и получаем токен
        String accessToken = userSteps
                .loginUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .extract().path("accessToken");

        // Получение информации о пользователе
        userSteps.getUserInfo(accessToken)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true))
                .body("user.email", is(email))
                .body("user.name", is(name));

        // Обновление информации о пользователе
        String updatedName = faker.name().fullName();
        userSteps.updateUserInfoName(accessToken, updatedName)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true))
                .body("user.email", is(email))
                .body("user.name", is(updatedName));

        // Проверка (дублирование) получения информации о пользователе
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
        final Faker faker = new Faker();

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().fullName();

        // Создание пользователя
        userSteps
                .createUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        // Получение информации о пользователе без токена
        userSteps.getUserInfo("")
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("You should be authorised"));

        // Обновление информации о пользователе без токена
        String updatedEmail = faker.internet().emailAddress();
        userSteps.updateUserInfoEmail("", updatedEmail)
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }

    @Test
    @DisplayName("Test not possibly update password user without login")
    @Description("Этот тест проверяет, что обновление password пользователя невозможно без авторизации")
    public void testNotPossiblyUpdatePasswordUserWithoutLogin() {

        final UserSteps userSteps = new UserSteps();
        final Faker faker = new Faker();

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().fullName();

        // Создание пользователя
        userSteps
                .createUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        // Получение информации о пользователе без токена
        userSteps.getUserInfo("")
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("You should be authorised"));

        // Обновление информации о пользователе без токена
        String updatedPassword = faker.internet().password();
        userSteps.updateUserInfoPassword("", updatedPassword)
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }

    @Test
    @DisplayName("Test not possibly update name user without login")
    @Description("Этот тест проверяет, что обновление name пользователя невозможно без авторизации")
    public void testNotPossiblyUpdateNameUserWithoutLogin() {

        final UserSteps userSteps = new UserSteps();
        final Faker faker = new Faker();

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().fullName();

        // Создание пользователя
        userSteps
                .createUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        // Получение информации о пользователе без токена
        userSteps.getUserInfo("")
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("You should be authorised"));

        // Обновление информации о пользователе без токена
        String updatedName = faker.name().fullName();
        userSteps.updateUserInfoName("", updatedName)
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }

    @Test
    @DisplayName("Test not possibly update the same email user with login")
    @Description("Этот тест проверяет невозможность обновления email авторизованного пользователя на тот же самый ")
    public void testNotPossiblyUserUpdateTheSameEmailWithLogin() {

        final UserSteps userSteps = new UserSteps();
        final Faker faker = new Faker();

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().fullName();

        // Создание пользователя
        userSteps.createUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        // Логинимся и получаем токен
        String accessToken = userSteps.loginUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .extract().path("accessToken");

        // Получение информации о пользователе
        userSteps.getUserInfo(accessToken)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true))
                .body("user.email", is(email))
                .body("user.name", is(name));

        // Попытка обновления информации о пользователе на тот же email
        userSteps.updateUserInfoEmail(accessToken, email)
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", is("User with such email already exists"));

        // Проверка (дублирование) получения информации о пользователе
        userSteps.getUserInfo(accessToken)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true))
                .body("user.email", is(email))
                .body("user.name", is(name));
    }
}