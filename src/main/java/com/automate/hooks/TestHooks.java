package com.automate.hooks;

import com.automate.core.AppiumDriverFactory;
import com.automate.core.DriverManager;
import com.automate.reporting.ReportManager;
import com.automate.utils.ScreenshotUtils;
import io.appium.java_client.AppiumDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class TestHooks {
    @BeforeMethod(alwaysRun = true)
    public final void startTest() {
        AppiumDriver driver = AppiumDriverFactory.createAndroidDriver();
        DriverManager.setDriver(driver);
        initializeTestContext(driver);
    }

    @AfterMethod(alwaysRun = true)
    public final void finishTest(ITestResult result) {
        String screenshotPath = null;
        Throwable failure = result.getThrowable();
        try {
            if (result.getStatus() == ITestResult.FAILURE && DriverManager.isInitialized()) {
                try {
                    screenshotPath = ScreenshotUtils.capture(
                            result.getTestClass().getName() + "_" + result.getMethod().getMethodName());
                } catch (RuntimeException screenshotFailure) {
                    failure = combine(failure, screenshotFailure);
                }
            }
        } finally {
            try {
                DriverManager.quitDriver();
            } catch (RuntimeException driverFailure) {
                failure = combine(failure, driverFailure);
            }
        }

        if (failure != result.getThrowable()) {
            result.setThrowable(failure);
        }
        if (failure != null) {
            result.setStatus(ITestResult.FAILURE);
        }

        try {
            ReportManager.logResult(result, screenshotPath);
        } catch (RuntimeException reportFailure) {
            failure = combine(failure, reportFailure);
            result.setThrowable(failure);
            result.setStatus(ITestResult.FAILURE);
            throw reportFailure;
        }

    }

    protected void initializeTestContext(AppiumDriver driver) {
    }

    private static Throwable combine(Throwable original, Throwable additional) {
        if (original == null) {
            return additional;
        }
        if (original != additional) {
            original.addSuppressed(additional);
        }
        return original;
    }
}
