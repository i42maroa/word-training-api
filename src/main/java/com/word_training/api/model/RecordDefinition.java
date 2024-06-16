package com.word_training.api.model;

import com.word_training.api.model.enums.DefinitionType;
import lombok.Data;

import java.util.List;

@Data
public class RecordDefinition {
    private String translation;
    private DefinitionType type;
    private String info;
    private List<Example> examples;
}
