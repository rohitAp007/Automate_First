package com.automate.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class ConfigManager {
    private static final Properties PROPERTIES = loadProperties();

    private ConfigManager() {
    }

    public static String get(String key) {
        return get(key, "");
    }

    public static String get(String key, String defaultValue) {
        String explicit = System.getenv(toEnvironmentName(key));
        String value = System.getProperty(toPropertyName(key));
        if (value == null || value.isBlank()) {
            value = explicit;
        }
        if (value == null || value.isBlank()) {
            value = PROPERTIES.getProperty(toPropertyName(key));
        }
        if (value == null || value.isBlank()) {
            value = System.getenv(toEnvironmentName(key));
        }
        if (value == null || value.isBlank()) {
            value = explicit == null ? loadDotenv().get(toEnvironmentName(key)) : explicit;
        }
        return value == null || value.isBlank() ? defaultValue : value.trim();
    }

    public static String getOptional(String key) {
        String value = get(key, "");
        return value.isBlank() ? null : value;
    }

    public static int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(get(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Configuration value must be an integer: " + key);
        }
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key, String.valueOf(defaultValue));
        return Boolean.parseBoolean(value);
    }

    private static Properties loadProperties() {
        String environment = EnvironmentManager.resolveEnvironment();
        Properties properties = new Properties();
        load(properties, Path.of("config/config.properties"));
        load(properties, Path.of("config/" + environment + ".properties"));
        load(properties, Path.of("config/android.properties"));
        return properties;
    }

    private static void load(Properties properties, Path path) {
        if (!Files.isRegularFile(path)) {
            return;
        }
        try (var input = Files.newInputStream(path)) {
            properties.load(input);
        } catch (IOException e) {
            throw new com.automate.core.FrameworkException("Unable to load configuration: " + path, e);
        }
    }

    private static Dotenv loadDotenv() {
        return Dotenv.configure().ignoreIfMissing().load();
    }

    private static String toPropertyName(String key) {
        return key.replace('_', '.').toLowerCase();
    }

    private static String toEnvironmentName(String key) {
        String separated = key.replace('.', '_')
                .replace('-', '_')
                .replaceAll("([a-z0-9])([A-Z])", "$1_$2");
        return separated.toUpperCase();
    }
}
