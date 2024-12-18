package com.word_training.api.service;

import com.word_training.api.domain.RecordDocument;
import com.word_training.api.model.input.RequestDefinition;
import reactor.core.publisher.Mono;

public interface DefinitionService {

    Mono<RecordDocument> newDefinitionToRecord(String id, RequestDefinition def);
    Mono<RecordDocument> modifyDefinitionInRecord(String id, String definitionId, RequestDefinition def);
    Mono<RecordDocument> deleteDefinitionInRecord(String idRecord, String definitionId);
}
