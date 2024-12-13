package com.word_training.api.repository.custom;

import com.word_training.api.domain.RecordDocument;
import com.word_training.api.model.queries.RecordPageInputQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomizedRecordRepository extends CustomizedBaseRepository {

    Flux<RecordDocument> findWordsPage(RecordPageInputQuery filters, Pageable pageable);

    Mono<Long> countWords(RecordPageInputQuery filters);

    Mono<RecordDocument> findAndModify(String id, Update update);
}
