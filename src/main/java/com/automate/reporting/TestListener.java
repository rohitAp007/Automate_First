package com.automate.reporting;

import org.testng.ISuite;
import org.testng.ISuiteListener;

public class TestListener implements ISuiteListener {
    @Override
    public void onFinish(ISuite suite) {
        ReportManager.flush();
    }
}
