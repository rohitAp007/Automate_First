package com.automate.reporting;

import com.automate.core.DriverManager;
import com.automate.utils.ScreenshotUtils;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        String screenshot = ScreenshotUtils.capture(
                result.getTestClass().getName() + "_" + result.getMethod().getMethodName());
        if (screenshot != null) {
            result.setAttribute("screenshot", screenshot);
        }
        ReportManager.logFailure(
                result.getTestClass().getName(),
                result.getMethod().getMethodName(),
                result.getThrowable());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ReportManager.logCompleted(
                result.getTestClass().getName(),
                result.getMethod().getMethodName());
    }
}
