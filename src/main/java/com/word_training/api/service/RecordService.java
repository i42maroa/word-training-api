package com.word_training.api.service;

import com.mongodb.bulk.BulkWriteResult;
import com.word_training.api.domain.RecordDocument;
import com.word_training.api.model.input.RequestDefinition;
import com.word_training.api.model.input.RequestExample;
import com.word_training.api.model.input.RequestModifyDefinition;
import com.word_training.api.model.input.RequestRecord;
import com.word_training.api.model.output.Pagination;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

public interface RecordService {

    Mono<RecordDocument> getRecord(ObjectId id);
    Mono<Pagination<RecordDocument>> getRecordPage(String type, Pageable pageRequest);

    Mono<RecordDocument> newRecord(RequestRecord record);
    Mono<BulkWriteResult> modifyRecord(ObjectId id, RequestRecord record);

    Mono<BulkWriteResult> newDefinitionToRecord(ObjectId id, RequestDefinition def);
    Mono<BulkWriteResult> modifyDefinitionInRecord(ObjectId id, String definitionId, RequestModifyDefinition def);
    Mono<BulkWriteResult> deleteDefinitionInRecord(ObjectId idRecord, String definitionId);

    Mono<BulkWriteResult> newExampleToDefinition(ObjectId idRecord, String definitionId, RequestExample def);
    Mono<BulkWriteResult> modifyExampleInDefinition(ObjectId idRecord, String definitionId, String exampleId, RequestExample def);
    Mono<BulkWriteResult> deleteExampleInDefinition(ObjectId idRecord, String definitionId, String exampleId);
}
