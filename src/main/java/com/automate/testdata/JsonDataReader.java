package com.automate.testdata;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class JsonDataReader {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonDataReader() {
    }

    public static <T> List<T> read(String path, Class<T> type) {
        try {
            return MAPPER.readValue(
                    new File(path),
                    MAPPER.getTypeFactory().constructCollectionType(List.class, type));
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read JSON test data: " + path, e);
        }
    }
}
