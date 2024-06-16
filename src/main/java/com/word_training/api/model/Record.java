package com.word_training.api.model;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class Record {
    private String value;
    private String type;
    private List<RecordDefinition> definitions;
    private OffsetDateTime creationDate;
    private OffsetDateTime modificationDate;

}
