package com.word_training.api.model.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ResponseError {
    private Integer status;
    private String message;
}
