package com.word_training.api.model.input;

import com.word_training.api.model.Definition;
import lombok.Data;

import java.util.List;

@Data
public class RequestRecord {

    private String value;
    private String type;
    private List<Definition> definitions;
}
