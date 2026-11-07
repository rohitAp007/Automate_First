package com.automate.core;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

import java.io.InputStream;
import java.util.Properties;

public final class LocatorReader {

    private LocatorReader() {
    }

    public static Properties load(
            String fileName) {

        try (InputStream input = LocatorReader.class
                    .getClassLoader()
                    .getResourceAsStream(fileName)) {

            if (input == null) {

                throw new RuntimeException(
                        "Locator file not found: "
                                + fileName
                );
            }

            Properties properties =
                    new Properties();

            properties.load(input);

            return properties;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to load locator repository: "
                            + fileName,
                    e
            );
        }
    }

    public static By get(
            Properties properties,
            String key) {

        String locator =
                properties.getProperty(key);

        if (locator == null) {

            throw new RuntimeException(
                    "Locator not found for key: "
                            + key
            );
        }

        return convertToBy(locator);
    }

    private static By convertToBy(
            String locator) {

        String[] parts =
                locator.split("=", 2);

        if (parts.length != 2) {

            throw new IllegalArgumentException(
                    "Invalid locator format: "
                            + locator
            );
        }

        String strategy =
                parts[0].trim().toLowerCase();

        String value =
                parts[1].trim();

        return switch (strategy) {

            case "id" ->
                    AppiumBy.id(value);

            case "xpath" ->
                    AppiumBy.xpath(value);

            case "accessibility_id" ->
                    AppiumBy.accessibilityId(value);

            case "class_name" ->
                    AppiumBy.className(value);

            case "android_uiautomator" ->
                    AppiumBy.androidUIAutomator(value);

            default ->
                    throw new IllegalArgumentException(
                            "Unsupported locator strategy: "
                                    + strategy
                    );
        };
    }
}