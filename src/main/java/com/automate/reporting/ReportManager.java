package com.automate.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.automate.core.FrameworkConstants;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ReportManager {
    private static final ExtentReports REPORTS = createReports();

    private ReportManager() {
    }

    public static synchronized void logCompleted(String className, String testName) {
        REPORTS.createTest(className + "." + testName).pass("Test completed");
        REPORTS.flush();
    }

    public static synchronized void logFailure(String className, String testName, Throwable error) {
        REPORTS.createTest(className + "." + testName).fail(error);
        REPORTS.flush();
    }

    private static ExtentReports createReports() {
        Path reportDirectory = Path.of(FrameworkConstants.REPORT_DIRECTORY);
        reportDirectory.toFile().mkdirs();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        ExtentSparkReporter reporter =
                new ExtentSparkReporter(reportDirectory.resolve("mobile-" + timestamp + ".html").toString());
        ExtentReports reports = new ExtentReports();
        reports.attachReporter(reporter);
        return reports;
    }
}
