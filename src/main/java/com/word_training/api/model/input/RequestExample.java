package com.word_training.api.model.input;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestExample {
    private String exampleId;
    @NotNull
    private String sentence;
    private String translation;
    private String info;
}
