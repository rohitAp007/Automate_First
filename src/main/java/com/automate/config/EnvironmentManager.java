package com.automate.config;

import io.github.cdimascio.dotenv.Dotenv;

public final class EnvironmentManager {
    private EnvironmentManager() {
    }

    public static String resolveEnvironment() {
        String value = System.getProperty("environment");
        if (value == null || value.isBlank()) {
            value = System.getenv("ENVIRONMENT");
        }
        if (value == null || value.isBlank()) {
            value = Dotenv.configure().ignoreIfMissing().load().get("ENVIRONMENT", "qa");
        }
        return value.trim().toLowerCase();
    }
}
