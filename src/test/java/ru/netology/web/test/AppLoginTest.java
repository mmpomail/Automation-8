package ru.netology.web.test;

import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.data.SQLHelper;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.web.data.SQLHelper.cleanDatabase;


public class AppLoginTest {

    LoginPage loginPage;

    @AfterEach
    void eraseAll() {
        cleanDatabase();
    }

    @AfterAll
    static void eraseDownAll() {
        cleanDatabase();
    }


    @BeforeEach
    void setUp() {
        loginPage = open("http://localhost:9999", LoginPage.class);
    }

    @Test
    @DisplayName("Should successfully log in to the dashboard using the provided login and password " +
            "from the system under test (SUT) test data.")
    void shouldSuccessLogin() {
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisibility();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    @DisplayName("Should receive an error notification when the user doesn't exist in the database.")
    void shouldGetErrorNotificationIfUserIsNotExistInBase() {
        var authInfo = DataHelper.generateRandomUser();
        loginPage.validLogin(authInfo);
        loginPage.verifyErrorNotification("Ошибка! " +
                "\nНеверно указан логин или пароль");
    }
}
