package com.word_training.api.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@Getter
@AllArgsConstructor
public enum RecordType {
    WORD("WORD"),
    PHRASAL_VERB("PHRASAL"),
    EXPRESSION("EXPRESSION");

    private final String value;
    private static final Map<String, RecordType> recordTypeMap = new HashMap<>();

    static {
        for (RecordType type: RecordType.values()){
            recordTypeMap.put(type.getValue(), type);
        }
    }

    public static RecordType getRecordType(String type) {
        return recordTypeMap.get(type);
    }

    public static final Predicate<String> isValidRecordType = recordTypeMap::containsKey;
}
