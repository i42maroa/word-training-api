package com.word_training.api.model.input;

import lombok.Data;

@Data
public class RequestModifyDefinition {
    private String translation;
    private String type;
    private String info;
}
