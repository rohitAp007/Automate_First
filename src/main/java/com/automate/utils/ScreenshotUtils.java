package com.automate.utils;

import com.automate.core.FrameworkConstants;
import com.automate.core.DriverManager;
import com.automate.core.FrameworkException;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public final class ScreenshotUtils {

    private ScreenshotUtils() {
    }

    public static String capture(
            String testName) {

        if (!DriverManager.isInitialized()) {
            return null;
        }

        try {

            Path directory =
                    Path.of(
                            FrameworkConstants
                                    .SCREENSHOT_DIRECTORY);

            Files.createDirectories(directory);

            String timestamp =
                    LocalDateTime.now()
                            .format(
                                    DateTimeFormatter
                                            .ofPattern(
                                                    "yyyyMMdd_HHmmss"));

            String safeTestName = testName.replaceAll("[^a-zA-Z0-9._-]", "_");
            String fileName = safeTestName + "_" + timestamp + "_" +
                    UUID.randomUUID().toString().substring(0, 8) + ".png";

            Path destination =
                    directory.resolve(fileName);

            File screenshot =
                    ((TakesScreenshot)
                            DriverManager
                                    .getDriver())
                            .getScreenshotAs(
                                    OutputType.FILE);

            Files.copy(
                    screenshot.toPath(),
                    destination,
                    StandardCopyOption.COPY_ATTRIBUTES);

            return destination.toString();

        } catch (Exception e) {
            throw new FrameworkException("Screenshot capture failed for test: " + testName, e);
        }
    }
}