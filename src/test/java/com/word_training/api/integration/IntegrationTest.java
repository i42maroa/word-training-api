package com.word_training.api.integration;

import com.word_training.api.config.ClockTestConfiguration;
import com.word_training.api.config.MongoDbTestConfig;
import com.word_training.api.config.MongoDbTestPopulatorConfig;
import com.word_training.api.domain.RecordDocument;
import com.word_training.api.moks.ApiRequestMock;
import com.word_training.api.moks.RecordMoks;
import de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@SpringBootTest
@ActiveProfiles("test")
//@EnableAutoConfiguration(exclude = {EmbeddedMongoAutoConfiguration.class})
@Import({MongoDbTestConfig.class, MongoDbTestPopulatorConfig.class, ClockTestConfiguration.class})
@AutoConfigureWebTestClient
public class IntegrationTest {

    @Autowired
    private WebTestClient webTestClient;


    @Test
    void createNewRecordOK(){
        var request = ApiRequestMock.requestRecordMock();
        var recordResponse = RecordMoks.recordMock();

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
}
