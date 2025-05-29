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

public class LoginUserTests {
    private final UserSteps userSteps = new UserSteps();
    private final Faker faker = new Faker();
    private String email;
    private String password;
    private String name;

    @Test
    @DisplayName("Test possibly login user")
    @Description("Этот тест проверяет что можно авторизовать пользователя")
    public void TestPossiblyLoginUser() {

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
                .loginUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Test not possibly login user with wrong login")
    @Description("Этот тест проверяет что нельзя авторизовать пользователя с неправильным логином")
    public void testNotPossiblyLoginUserWithWrongLogin() {

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        email = faker.internet().emailAddress();
        password = faker.internet().password();
        name = faker.name().fullName();

        userSteps
                .createUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        email = faker.internet().emailAddress();

        userSteps
                .loginUser(email, password, name)
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", Matchers.is("email or password are incorrect"));
    }

    @Test
    @DisplayName("Test not possibly login user with wrong password")
    @Description("Этот тест проверяет что нельзя авторизовать пользователя с неправильным паролем")
    public void testNotPossiblyLoginUserWithWrongPassword() {

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        email = faker.internet().emailAddress();
        password = faker.internet().password();
        name = faker.name().fullName();

        userSteps
                .createUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        password = faker.internet().password();

        userSteps
                .loginUser(email, password, name)
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", Matchers.is("email or password are incorrect"));
    }

    @Test
    @DisplayName("Test not possibly login user with wrong email and password")
    @Description("Этот тест проверяет что нельзя авторизовать пользователя с неправильным email и паролем")
    public void testNotPossiblyLoginUserWithWrongEmailAndPassword() {

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        email = faker.internet().emailAddress();
        password = faker.internet().password();
        name = faker.name().fullName();

        userSteps
                .createUser(email, password, name)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        email = faker.internet().emailAddress();
        password = faker.internet().password();

        userSteps
                .loginUser(email, password, name)
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", Matchers.is("email or password are incorrect"));
    }

    public void tearDown() {
        Integer id = userSteps.loginUser(email, password, name).extract().path("id");
        userSteps.deleteUser(id);
    }
}