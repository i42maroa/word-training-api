package com.word_training.api.model;

import lombok.Data;

@Data
public class Example {
    private String exampleId;
    private String sentence;
    private String translation;
    private String info;
}
