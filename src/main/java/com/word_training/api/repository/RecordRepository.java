package com.word_training.api.repository;

import com.word_training.api.domain.RecordDocument;
import com.word_training.api.model.Record;
import com.word_training.api.repository.custom.CustomizedRecordRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Update;
import reactor.core.publisher.Mono;

public interface RecordRepository extends ReactiveMongoRepository<RecordDocument, ObjectId>, CustomizedRecordRepository {

    Mono<RecordDocument> findByRecordId(String recordId);

    Mono<Void> deleteByRecordId(String recordId);


}
