package com.word_training.api.repository.custom.impl;


import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.UpdateResult;
import com.word_training.api.domain.RecordDocument;
import com.word_training.api.model.queries.RecordPageInputQuery;
import com.word_training.api.repository.custom.CustomizedRecordRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.word_training.api.constants.WordTrainingConstants.RECORD_COLLECTION;

@RequiredArgsConstructor
public class CustomizedRecordRepositoryImpl implements CustomizedRecordRepository {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Flux<RecordDocument> findWordsPage(RecordPageInputQuery filters, Pageable pageable) {
        var criteria = getAllCriteria(filters, this::getRecordPageCriterionList);
        var query = new Query()
                .with(pageable)
                .addCriteria(criteria);

        return reactiveMongoTemplate.find(query, RecordDocument.class);
    }

    @Override
    public Mono<Long> countWords(RecordPageInputQuery filters) {
        var criteria = getAllCriteria(filters, this::getRecordPageCriterionList);
        var query = new Query().addCriteria(criteria);
        return reactiveMongoTemplate.count(query, RecordDocument.class);
    }

    @Override
    public Mono<BulkWriteResult> bulkUpdate(List<UpdateOneModel<Document>> writeModels) {
        return reactiveMongoTemplate
                .getCollection(RECORD_COLLECTION)
                .flatMap(collection -> Mono.from(collection.bulkWrite(writeModels)));
    }

    private List<Criteria> getRecordPageCriterionList(RecordPageInputQuery filters) {
        return Stream.of(regexCriteria("value", filters.getValue()),
                        equalsCriteria("type", filters.getType()))
                .flatMap(Function.identity())
                .toList();
    }
}
