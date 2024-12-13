package com.word_training.api.repository.custom.impl;


import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.UpdateOneModel;
import com.word_training.api.domain.RecordDocument;
import com.word_training.api.model.queries.RecordPageInputQuery;
import com.word_training.api.repository.custom.CustomizedRecordRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
                .addCriteria(criteria)
                .with(Sort.by(Sort.Direction.DESC, "creationDate"));

        return reactiveMongoTemplate.find(query, RecordDocument.class);
    }

    @Override
    public Mono<Long> countWords(RecordPageInputQuery filters) {
        var criteria = getAllCriteria(filters, this::getRecordPageCriterionList);
        var query = new Query().addCriteria(criteria);
        return reactiveMongoTemplate.count(query, RecordDocument.class);
    }

    @Override
    public Mono<RecordDocument> findAndModify(String id, Update update) {
        var query = new Query().addCriteria(Criteria.where("recordId").is(id));
        var options = new FindAndModifyOptions().returnNew(true).upsert(true);
        return reactiveMongoTemplate.findAndModify(query, update, options, RecordDocument.class);
    }


    private List<Criteria> getRecordPageCriterionList(RecordPageInputQuery filters) {
        return Stream.of(
                        inCriteria("type", filters.getTypeIn()),
                        emptyArray("definitions", filters.getPending()),
                        orOperator(Stream.concat(
                                regexCriteria("value", filters.getText()),
                                regexCriteria("definitions.translation", filters.getText())))
                )
                .flatMap(Function.identity())
                .toList();
    }
}
