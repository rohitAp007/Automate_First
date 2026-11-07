package com.automate.pages.mobile;

import com.automate.core.FrameworkConstants;
import com.automate.core.LocatorReader;
import com.automate.core.WaitUtils;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

import java.util.Properties;

public class LoginScreen {

    private final AppiumDriver driver;

    private final Properties locators;

    private final WaitUtils wait;

    public LoginScreen(
            AppiumDriver driver) {

        this.driver = driver;

        this.locators =
                LocatorReader.load(
                        FrameworkConstants
                                .LOGIN_LOCATOR_FILE
                );

        this.wait =
                new WaitUtils();
    }

    public void enterUsername(
            String username) {

        By locator =
                LocatorReader.get(
                        locators,
                        "username"
                );

        var element = wait.clickable(locator);
        element.click();
        element.clear();
        element.sendKeys(username);
    }

    public void enterPassword(
            String password) {

        By locator =
                LocatorReader.get(
                        locators,
                        "password"
                );

        var element = wait.clickable(locator);
        element.click();
        element.clear();
        element.sendKeys(password);
    }

    public void tapLogin() {

        By locator =
                LocatorReader.get(
                        locators,
                        "loginButton"
                );

        wait.clickable(locator)
                .click();
    }

    public void enterOtp(
            String otp) {

        By locator =
                LocatorReader.get(
                        locators,
                        "otpField"
                );

        wait.visible(locator)
                .sendKeys(otp);
    }

    public boolean isOnboardingNextDisplayed() {
        By locator = LocatorReader.get(locators, "onboardingNext");
        return wait.screenDisplayed(locator);
    }

    public void tapNext() {
        By locator = LocatorReader.get(locators, "onboardingNext");
        wait.clickable(locator).click();
    }

    public boolean isBeginDisplayed() {
        By locator = LocatorReader.get(locators, "onboardingBegin");
        return wait.screenDisplayed(locator);
    }

    public void tapBegin() {
        By locator = LocatorReader.get(locators, "onboardingBegin");
        wait.clickable(locator).click();
    }

    public boolean isLoginFormDisplayed() {
        By username = LocatorReader.get(locators, "username");
        By password = LocatorReader.get(locators, "password");
        return wait.exists(username) && wait.exists(password);
    }
}