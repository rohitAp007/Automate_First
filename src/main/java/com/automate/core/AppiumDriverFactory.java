package com.automate.core;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import com.automate.config.ConfigManager;

import java.net.URI;

public final class AppiumDriverFactory {

    private AppiumDriverFactory() {
    }

    public static AndroidDriver createAndroidDriver() {

        String serverUrl = ConfigManager.get("appium.server.url");
        UiAutomator2Options options =
                new UiAutomator2Options();
        options.setPlatformName(ConfigManager.get("platformName", "Android"));
        options.setAutomationName(ConfigManager.get("automationName", "UiAutomator2"));
        options.setDeviceName(ConfigManager.get("deviceName"));
        options.setNewCommandTimeout(java.time.Duration.ofSeconds(
                ConfigManager.getInt("newCommandTimeout", 120)));
        options.setNoReset(ConfigManager.getBoolean("noReset", false));
        options.setFullReset(ConfigManager.getBoolean("fullReset", false));
        setOptional(options, "app", ConfigManager.getOptional("app.path"));
        setOptional(options, "appPackage", ConfigManager.getOptional("appPackage"));
        setOptional(options, "appActivity", ConfigManager.getOptional("appActivity"));

        try {

            return new AndroidDriver(
                    URI.create(serverUrl).toURL(),
                    options
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to create Android Appium driver",
                    e
            );
        }

    }

    private static void setOptional(UiAutomator2Options options, String capability, String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        switch (capability) {
            case "app" -> options.setApp(value);
            case "appPackage" -> options.setAppPackage(value);
            case "appActivity" -> options.setAppActivity(value);
            default -> throw new IllegalArgumentException("Unsupported capability: " + capability);
        }
    }
}