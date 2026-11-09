package com.automate.mobile;

import com.automate.business.LoginBusiness;
import com.automate.hooks.TestHooks;
import com.automate.pages.mobile.HomeScreen;
import com.automate.testdata.LoginCredentials;
import com.automate.testdata.JsonDataReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumDriver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LoginTest extends TestHooks {
    private LoginBusiness loginBusiness;
    private HomeScreen homeScreen;

    @Override
    protected void initializeTestContext(AppiumDriver driver) {
        loginBusiness = new LoginBusiness(driver);
        homeScreen = new HomeScreen(driver);
    }

    @DataProvider(name = "loginCredentials")
    public Object[][] loginCredentials() {
        List<LoginCredentials> credentials = loadCredentials();
        Object[][] data = new Object[credentials.size()][1];
        for (int index = 0; index < credentials.size(); index++) {
            data[index][0] = index;
        }
        return data;
    }

    @Test(dataProvider = "loginCredentials")
    public void verifyValidLogin(int dataIndex) {
        LoginCredentials credentials = loadCredentials().get(dataIndex);
        loginBusiness.completeOnboarding();
        Assert.assertTrue(loginBusiness.isLoginFormDisplayed(), "Login form should be displayed");
        loginBusiness.login(credentials.username(), credentials.password());

        Assert.assertTrue(
                homeScreen.isDisplayed(),
                "Home screen should be displayed after login"
        );
    }

    private List<LoginCredentials> loadCredentials() {
        Path dataFile = Path.of("test-data/login-data.local.json");
        if (!Files.isRegularFile(dataFile)) {
            throw new IllegalStateException(
                    "Login data file is missing. Create it with: "
                            + "cp test-data/login-data.json test-data/login-data.local.json");
        }
        List<LoginCredentials> credentials = JsonDataReader.read(dataFile.toString(), LoginCredentials.class);
        if (credentials == null || credentials.isEmpty()) {
            throw new IllegalStateException("Login data file contains no records");
        }
        for (int index = 0; index < credentials.size(); index++) {
            LoginCredentials record = credentials.get(index);
            if (record.username() == null || record.username().isBlank()
                    || record.password() == null || record.password().isBlank()) {
                throw new IllegalStateException(
                        "Login data record " + (index + 1) + " must contain a username and password");
            }
        }
        return credentials;
    }
}