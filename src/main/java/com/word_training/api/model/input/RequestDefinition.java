package com.word_training.api.model.input;

import com.word_training.api.model.Example;
import com.word_training.api.model.enums.DefinitionType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class RequestDefinition {
    private String definitionId;
    @NotNull
    private String translation;
    @NotNull
    private String type;
    private String info;
    private List<RequestExample> examples;
}
