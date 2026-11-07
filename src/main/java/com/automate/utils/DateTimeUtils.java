package com.automate.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateTimeUtils {
    private static final DateTimeFormatter TIMESTAMP =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private DateTimeUtils() {
    }

    public static String timestamp() {
        return LocalDateTime.now().format(TIMESTAMP);
    }
}
