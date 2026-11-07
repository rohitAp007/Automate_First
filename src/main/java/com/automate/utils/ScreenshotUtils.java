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

            String fileName =
                    testName
                            + "_"
                            + timestamp
                            + ".png";

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
                    StandardCopyOption
                            .REPLACE_EXISTING);

            return destination.toString();

        } catch (Exception e) {
            throw new FrameworkException("Screenshot capture failed for test: " + testName, e);
        }
    }
}