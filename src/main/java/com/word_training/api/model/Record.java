package com.word_training.api.model;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class Record {
    @Indexed
    private String recordId;
    private String value;
    private String type;
    private List<Definition> definitions;
    private OffsetDateTime creationDate;
    private OffsetDateTime modificationDate;
}
