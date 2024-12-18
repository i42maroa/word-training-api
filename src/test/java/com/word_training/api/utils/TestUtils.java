package com.word_training.api.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtils {

    public static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @SneakyThrows
    public static String getResourceAsString(String path){
        var filePath = Paths.get("src/test/resource", path);
        return Files.readString(filePath, StandardCharsets.UTF_8);
    }

    @SneakyThrows
    public static <T> T getResourceAsString(String path, TypeReference<T> typeReference){
        return mapper.readValue(getResourceAsString(path), typeReference);
    }

    @SneakyThrows
    public static String asString(Object object){
        return mapper.writeValueAsString(object);
    }
}
