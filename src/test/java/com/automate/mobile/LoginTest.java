package com.automate.mobile;

import com.automate.business.LoginBusiness;
import com.automate.core.DriverManager;
import com.automate.hooks.TestHooks;
import com.automate.pages.mobile.HomeScreen;
import com.automate.config.ConfigManager;
import com.automate.reporting.TestListener;
import com.automate.testdata.LoginCredentials;
import com.automate.testdata.JsonDataReader;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.annotations.Listeners;

@Listeners(TestListener.class)
public class LoginTest {

    private LoginBusiness loginBusiness;
    private HomeScreen homeScreen;

    @BeforeMethod
    public void setUp() {
        TestHooks.beforeMethod();
        loginBusiness = new LoginBusiness(DriverManager.getDriver());
        homeScreen = new HomeScreen(DriverManager.getDriver());
    }

    @Test
    public void verifyValidLogin() {

        LoginCredentials credentials = new LoginCredentials(
                ConfigManager.get("test.username"),
                ConfigManager.get("test.password"));
        if (credentials.password().isBlank()) {
            throw new IllegalStateException("TEST_PASSWORD must be configured in .env");
        }
        loginBusiness.completeOnboarding();
        Assert.assertTrue(loginBusiness.isLoginFormDisplayed(), "Login form should be displayed");
        loginBusiness.login(credentials.username(), credentials.password());

        Assert.assertTrue(
                homeScreen.isDisplayed(),
                "Home screen should be displayed after login"
        );
    }

    @AfterMethod
    public void tearDown() {

        TestHooks.afterMethod(this, "verifyValidLogin");
    }
}