package com.word_training.api.model.input;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class RequestRecord {

    @NotNull
    private String value;
    @NotNull
    private String type;

    private List<RequestDefinition> definitions;
}
