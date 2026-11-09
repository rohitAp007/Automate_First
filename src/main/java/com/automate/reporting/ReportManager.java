package com.automate.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.automate.core.FrameworkConstants;
import com.automate.core.FrameworkException;
import org.testng.ITestResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public final class ReportManager {
    private static final ExtentReports REPORTS = createReports();

    private ReportManager() {
    }

    public static synchronized void logResult(ITestResult result, String screenshotPath) {
        String testName = result.getTestClass().getName() + "."
                + result.getMethod().getMethodName() + "-" + result.getStartMillis();
        ExtentTest test = REPORTS.createTest(testName);
        switch (result.getStatus()) {
            case ITestResult.SUCCESS -> test.pass("Test passed");
            case ITestResult.SKIP -> test.skip("Test skipped");
            default -> {
                Throwable failure = result.getThrowable();
                if (failure == null) {
                    test.fail("Test failed");
                } else {
                    test.fail(failure);
                }
            }
        }

        if (screenshotPath != null) {
            test.addScreenCaptureFromPath(screenshotPath);
        }
        REPORTS.flush();
    }

    public static synchronized void flush() {
        REPORTS.flush();
    }

    private static ExtentReports createReports() {
        Path reportDirectory = Path.of(FrameworkConstants.REPORT_DIRECTORY);
        try {
            Files.createDirectories(reportDirectory);
        } catch (IOException e) {
            throw new FrameworkException("Unable to create report directory: " + reportDirectory, e);
        }
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        ExtentSparkReporter reporter =
                new ExtentSparkReporter(reportDirectory.resolve(
                        "mobile-" + timestamp + "-" + UUID.randomUUID().toString().substring(0, 8) + ".html")
                        .toString());
        ExtentReports reports = new ExtentReports();
        reports.attachReporter(reporter);
        return reports;
    }
}
