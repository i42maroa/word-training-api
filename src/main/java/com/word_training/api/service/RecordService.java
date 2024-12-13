package com.word_training.api.service;

import com.word_training.api.domain.RecordDocument;
import com.word_training.api.exceptions.WordTrainingApiException;
import com.word_training.api.model.input.*;
import com.word_training.api.model.output.Pagination;
import com.word_training.api.model.queries.RecordPageInputQuery;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.function.Predicate;

public interface RecordService {

    Mono<RecordDocument> getRecord(String id);
    Mono<Pagination<RecordDocument>> getRecordPage(RecordPageInputQuery inputQuery, Pageable pageRequest);

    Mono<RecordDocument> newRecord(RequestRecord record);
    Mono<RecordDocument> modifyRecord(String id, RequestRecord record);
    Mono<Void> deleteRecord(String recordId);

    Mono<RecordDocument> newDefinitionToRecord(String id, RequestDefinition def);
    Mono<RecordDocument> modifyDefinitionInRecord(String id, String definitionId, RequestDefinition def);
    Mono<RecordDocument> deleteDefinitionInRecord(String idRecord, String definitionId);

    Mono<RecordDocument> newExampleToDefinition(String idRecord, String definitionId, RequestExample def);
    Mono<RecordDocument> modifyExampleInDefinition(String idRecord, String definitionId, String exampleId, RequestModifyExample def);
    Mono<RecordDocument> deleteExampleInDefinition(String idRecord, String definitionId, String exampleId);

    default void isValidObjectId(String... ids){
        Arrays.stream(ids)
                .filter(Predicate.not(ObjectId::isValid))
                .findAny()
                .ifPresent(id -> { throw new  WordTrainingApiException(id + " is an invalid request id.");});
    }
}
