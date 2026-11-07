package com.automate.pages.mobile;

import com.automate.core.FrameworkConstants;
import com.automate.core.LocatorReader;
import com.automate.core.WaitUtils;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

import java.util.Properties;

public class HomeScreen {

    private final Properties locators;
    private final WaitUtils wait;

    public HomeScreen(
            AppiumDriver driver) {

        this.locators =
                LocatorReader.load(
                        FrameworkConstants
                                .HOME_LOCATOR_FILE
                );

        this.wait =
                new WaitUtils();
    }

    public boolean isDisplayed() {

        By locator =
                LocatorReader.get(
                        locators,
                        "homeTitle"
                );

        return wait.visible(locator)
                .isDisplayed();
    }
}