package com.word_training.api.repository;

import com.word_training.api.domain.RecordDocument;
import com.word_training.api.repository.custom.CustomizedRecordRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface RecordRepository extends ReactiveMongoRepository<RecordDocument, ObjectId>, CustomizedRecordRepository {

}
