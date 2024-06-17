package com.word_training.api.service.impl;

import com.mongodb.bulk.BulkWriteResult;
import com.word_training.api.domain.RecordDocument;
import com.word_training.api.exceptions.WordTrainingApiException;
import com.word_training.api.mapper.RecordMapper;
import com.word_training.api.model.input.RequestDefinition;
import com.word_training.api.model.input.RequestExample;
import com.word_training.api.model.input.RequestRecord;
import com.word_training.api.model.output.Pagination;
import com.word_training.api.model.queries.RecordPageInputQuery;
import com.word_training.api.repository.RecordRepository;
import com.word_training.api.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;


@RequiredArgsConstructor
@Service
public class RecordServiceImpl implements RecordService {

    private final RecordMapper mapper;
    private final RecordRepository repository;

    @Override
    public Mono<RecordDocument> getRecord(ObjectId id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Pagination<RecordDocument>> getRecordPage(String type, Pageable pageRequest) {
        var query = new RecordPageInputQuery();
        query.setType(type);

        var listWordsMono = repository.findWordsPage(query, pageRequest).collectList();
        var countMono = repository.countWords(query);

        return Mono.zip(listWordsMono, countMono, (items, count) -> new Pagination<>(items, pageRequest, count));
    }

    @Override
    public Mono<RecordDocument> newRecord(RequestRecord request) {
        var record = mapper.generateRecordByRequest(request);
        return repository.insert(record);
    }

    @Override
    public Mono<BulkWriteResult> modifyRecord(ObjectId id, RequestRecord record) {
        Optional.ofNullable(record)
                .filter(r -> nonNull(r.getValue()) && nonNull(r.getType()) && nonNull(r.getDefinitions()))
                .orElseThrow(() -> new WordTrainingApiException("Request of change empty"));

        var update = mapper.generateUpdateRecord(id, record);
        return repository.bulkUpdate(List.of(update));
    }

    @Override
    public Mono<BulkWriteResult> newDefinitionToRecord(ObjectId id, RequestDefinition def) {
        var update = mapper.generateUpdateNewDefinition(id, def);

        return repository.bulkUpdate(update);
    }

    @Override
    public Mono<BulkWriteResult> newExampleToDefinition(ObjectId idRecord, String definitionId, RequestExample def) {
        var update = mapper.generateUpdateNewExample(idRecord, definitionId, def);
        return repository.bulkUpdate(List.of(update));
    }

    @Override
    public Mono<BulkWriteResult> modifyExampleInDefinition(ObjectId idRecord, String definitionId, String exampleId, RequestExample req) {
        Optional.ofNullable(req)
                .filter(r -> nonNull(r.getTranslation()) && nonNull(r.getInfo()) && nonNull(r.getSentence()))
                .orElseThrow(() -> new WordTrainingApiException("Request of change empty"));

        var update = mapper.generateUpdateModifyExample(idRecord, definitionId, exampleId, req);
        return repository.bulkUpdate(List.of(update));
    }
}
