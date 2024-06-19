package com.word_training.api.model.input;

import lombok.Data;

@Data
public class RequestModifyExample {
    private String sentence;
    private String translation;
    private String info;
}
