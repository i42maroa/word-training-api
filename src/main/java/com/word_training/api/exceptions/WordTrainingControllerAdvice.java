package com.word_training.api.exceptions;


import com.mongodb.MongoException;
import com.word_training.api.model.error.ResponseError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class WordTrainingControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(WordTrainingApiException.class)
    public ResponseEntity<ResponseError> handle(WordTrainingApiException e){
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(MongoException.class)
    public ResponseEntity<String> handleMongoError(MongoException e){
        return ResponseEntity.status(HttpStatusCode.valueOf(502)).body(e.getMessage());
    }
}
