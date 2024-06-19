package com.word_training.api.service;

import com.mongodb.bulk.BulkWriteResult;
import com.word_training.api.domain.RecordDocument;
import com.word_training.api.exceptions.WordTrainingApiException;
import com.word_training.api.model.input.*;
import com.word_training.api.model.output.Pagination;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface RecordService {

    Mono<RecordDocument> getRecord(String id);
    Mono<Pagination<RecordDocument>> getRecordPage(String type, Pageable pageRequest);

    Mono<RecordDocument> newRecord(RequestRecord record);
    Mono<BulkWriteResult> modifyRecord(String id, RequestModifyRecord record);

    Mono<BulkWriteResult> newDefinitionToRecord(String id, RequestDefinition def);
    Mono<BulkWriteResult> modifyDefinitionInRecord(String id, String definitionId, RequestModifyDefinition def);
    Mono<BulkWriteResult> deleteDefinitionInRecord(String idRecord, String definitionId);

    Mono<BulkWriteResult> newExampleToDefinition(String idRecord, String definitionId, RequestExample def);
    Mono<BulkWriteResult> modifyExampleInDefinition(String idRecord, String definitionId, String exampleId, RequestModifyExample def);
    Mono<BulkWriteResult> deleteExampleInDefinition(String idRecord, String definitionId, String exampleId);

    default boolean isValidObjectId(String id){
        return Optional.ofNullable(id)
                .filter(ObjectId::isValid)
                .map(isTrue -> true)
                .orElseThrow(()-> new WordTrainingApiException(id + " is an invalid request id."));
    }
}
