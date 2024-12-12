package com.word_training.api.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("test")
@WebFluxTest(controllers = WordTrainingCrudController.class)
public class WordTrainingCrudController {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void dsa(){
        webTestClient
                .get()
                .uri("record/{id}", "123")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ResponseEntity.class);
    }
}
