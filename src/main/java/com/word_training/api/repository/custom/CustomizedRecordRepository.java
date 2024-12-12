package com.word_training.api.repository.custom;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.UpdateOneModel;
import com.word_training.api.domain.RecordDocument;
import com.word_training.api.model.queries.RecordPageInputQuery;
import org.bson.Document;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CustomizedRecordRepository extends CustomizedBaseRepository{

    Flux<RecordDocument> findWordsPage(RecordPageInputQuery filters, Pageable pageable);

    Mono<Long> countWords(RecordPageInputQuery filters);

    Mono<BulkWriteResult> bulkUpdate(List<UpdateOneModel<Document>> writeModels);

    Mono<RecordDocument> findAndModify(String id, Update update);
}
