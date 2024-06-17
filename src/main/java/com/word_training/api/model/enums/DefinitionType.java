package com.word_training.api.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@Getter
@AllArgsConstructor
public enum DefinitionType {
    NOUN("noun"),
    ADJ("adjective"),
    ADV("adverb"),
    VERB("verb");

    private final String value;
    private static final Map<String, DefinitionType> typeMap = new HashMap<>();

    static {
        for (DefinitionType type: DefinitionType.values()){
            typeMap.put(type.getValue(), type);
        }
    }

    public static DefinitionType getDefinitionType(String type) {
        return typeMap.get(type);
    }

    public static final Predicate<String> isValidDefinitionType = typeMap::containsKey;
}
