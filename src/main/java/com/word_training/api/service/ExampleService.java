package com.word_training.api.service;

import com.word_training.api.domain.RecordDocument;
import com.word_training.api.model.input.RequestExample;
import com.word_training.api.model.input.RequestModifyExample;
import reactor.core.publisher.Mono;

public interface ExampleService {
    Mono<RecordDocument> newExampleToDefinition(String idRecord, String definitionId, RequestExample def);
    Mono<RecordDocument> modifyExampleInDefinition(String idRecord, String definitionId, String exampleId, RequestModifyExample def);
    Mono<RecordDocument> deleteExampleInDefinition(String idRecord, String definitionId, String exampleId);
}
