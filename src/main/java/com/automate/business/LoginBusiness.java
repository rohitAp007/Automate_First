package com.automate.business;

import com.automate.pages.mobile.HomeScreen;
import com.automate.pages.mobile.LoginScreen;
import io.appium.java_client.AppiumDriver;

public class LoginBusiness {

    private final LoginScreen loginScreen;
    private final HomeScreen homeScreen;

    public LoginBusiness(
            AppiumDriver driver) {

        loginScreen =
                new LoginScreen(driver);

        homeScreen =
                new HomeScreen(driver);
    }

    public void login(
            String username,
            String password) {

        loginScreen.enterUsername(
                username
        );

        loginScreen.enterPassword(
                password
        );

        loginScreen.tapLogin();
    }

    public void completeOnboarding() {
        while (loginScreen.isOnboardingNextDisplayed()) {
            loginScreen.tapNext();
        }
        if (loginScreen.isBeginDisplayed()) {
            loginScreen.tapBegin();
        }
    }

    public boolean isLoginFormDisplayed() {
        return loginScreen.isLoginFormDisplayed();
    }

    public boolean isLoginSuccessful() {

        return homeScreen.isDisplayed();
    }
}