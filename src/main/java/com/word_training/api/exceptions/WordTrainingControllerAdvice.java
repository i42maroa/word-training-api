package com.word_training.api.exceptions;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class WordTrainingControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(WordTrainingApiException.class)
    public ResponseEntity<Void> handle(WordTrainingApiException e){
        log.error(e.getMessage());
        return ResponseEntity.badRequest().build();
    }
}
