package com.word_training.api.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("test")
@WebFluxTest(controllers = WordTrainingCRUDController.class)
public class WordTrainingSearchingControllerTest {

    @Autowired
    private WebTestClient webTestClient;
}
