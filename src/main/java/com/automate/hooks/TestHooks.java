package com.automate.hooks;

import com.automate.core.AppiumDriverFactory;
import com.automate.core.DriverManager;

public final class TestHooks {
    private TestHooks() {
    }

    public static void beforeMethod() {
        DriverManager.setDriver(AppiumDriverFactory.createAndroidDriver());
    }

    public static void afterMethod(Object testInstance, String testName) {
        DriverManager.quitDriver();
    }
}
