package com.automate.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileUtils {
    private FileUtils() {
    }

    public static Path createDirectories(String directory) {
        try {
            return Files.createDirectories(Path.of(directory));
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create directory: " + directory, e);
        }
    }
}
