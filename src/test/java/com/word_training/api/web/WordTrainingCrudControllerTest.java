package com.word_training.api.web;

import com.word_training.api.domain.RecordDocument;
import com.word_training.api.moks.ApiRequestMock;
import com.word_training.api.moks.RecordMoks;
import com.word_training.api.service.DefinitionService;
import com.word_training.api.service.ExampleService;
import com.word_training.api.service.RecordService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

@ActiveProfiles("test")
@WebFluxTest(controllers = WordTrainingCRUDController.class)
public class WordTrainingCrudControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private RecordService recordService;
    @MockBean
    private DefinitionService definitionService;
    @MockBean
    private ExampleService exampleService;

    @Test
    void createNewRecordOK(){
        var request = ApiRequestMock.requestRecordMock();
        var recordResponse = RecordMoks.recordMock();

        Mockito.when(recordService.newRecord(ArgumentMatchers.eq(request))).thenReturn(Mono.just(recordResponse));
        webTestClient.post()
                .uri("/record")
                .body(BodyInserters.fromValue(request))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(RecordDocument.class)
                .isEqualTo(recordResponse);
    }


  /* @Test
    void createNewRecordOK(){
        webTestClient
                .post()
                .uri("record/{id}", "123")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ResponseEntity.class);
    }*/
}
