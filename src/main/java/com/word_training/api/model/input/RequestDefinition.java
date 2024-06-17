package com.word_training.api.model.input;

import com.word_training.api.model.Example;
import com.word_training.api.model.enums.DefinitionType;
import lombok.Data;

import java.util.List;

@Data
public class RequestDefinition {
    private String translation;
    private String type;
    private String info;
    private List<Example> examples;
}
