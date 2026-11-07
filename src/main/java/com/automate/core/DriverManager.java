package com.automate.core;

import io.appium.java_client.AppiumDriver;

public final class DriverManager {

    private static final ThreadLocal<AppiumDriver> DRIVER =
            new ThreadLocal<>();

    private DriverManager() {
    }

    public static void setDriver(
            AppiumDriver driver) {

        DRIVER.set(driver);
    }

    public static AppiumDriver getDriver() {

        AppiumDriver driver = DRIVER.get();

        if (driver == null) {
            throw new IllegalStateException(
                    "Appium driver is not initialized."
            );
        }

        return driver;
    }

    public static boolean isInitialized() {
        return DRIVER.get() != null;
    }

    public static void quitDriver() {

        AppiumDriver driver = DRIVER.get();

        if (driver != null) {

            driver.quit();
            DRIVER.remove();
        }
    }
}