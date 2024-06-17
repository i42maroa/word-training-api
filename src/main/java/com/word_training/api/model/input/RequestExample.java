package com.word_training.api.model.input;

import lombok.Data;

@Data
public class RequestExample {
    private String sentence;
    private String translation;
    private String info;
}
