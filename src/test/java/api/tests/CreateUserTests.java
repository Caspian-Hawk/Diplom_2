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
import org.junit.Test;
import static org.hamcrest.core.Is.is;

public class CreateUserTests {

    private final UserSteps userSteps = new UserSteps();
    private final Faker faker = new Faker();
    private String email;
    private String password;
    private String name;
    private Integer userId;

    @Test
    @DisplayName("Test possibly create user")
    @Description("Этот тест проверяет что можно создать пользователя")
    public void testPossiblyCreateUser() {

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        email = faker.internet().emailAddress();
        password = faker.internet().password();
        name = faker.name().fullName();

        userSteps
                .createUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Test not possibly create existing user")
    @Description("Этот тест проверяет что нельзя создать пользователя, который уже существует")
    public void testNotPossiblyCreateExistingUser() {

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        email = faker.internet().emailAddress();
        password = faker.internet().password();
        name = faker.name().fullName();

        userSteps
                .createUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        userSteps
                .createUser(email, password, name)
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", Matchers.is("User already exists"));
    }

    @Test
    @DisplayName("Test not possibly create user without email")
    @Description("Этот тест проверяет что нельзя создать пользователя, без email")
    public void testNotPossiblyCreateUserWithoutEmail() {

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        password = faker.internet().password();
        name = faker.name().fullName();

        userSteps
                .createUserWitheOutEmail(password, name)
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", Matchers.is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Test not possibly create user without password")
    @Description("Этот тест проверяет что нельзя создать пользователя, без пароля")
    public void testNotPossiblyCreateUserWithoutPassword() {

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        email = faker.internet().emailAddress();
        name = faker.name().fullName();

        userSteps
                .createUserWitheOutPassword(email, name)
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", Matchers.is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Test not possibly create user without name")
    @Description("Этот тест проверяет что нельзя создать пользователя, без name")
    public void testNotPossiblyCreateUserWithoutName() {

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        email = faker.internet().emailAddress();
        password = faker.internet().password();

        userSteps
                .createUserWitheOutName(email, password)
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", Matchers.is("Email, password and name are required fields"));
    }

    @After
    public void tearDown() {
        if (userId != null) {
            userSteps.deleteUser(userId)
                    .statusCode(HttpStatus.SC_OK)
                    .body("success", Is.is(true));
        }
    }
}