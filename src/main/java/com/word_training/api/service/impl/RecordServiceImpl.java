package com.word_training.api.service.impl;

import com.mongodb.bulk.BulkWriteResult;
import com.word_training.api.domain.RecordDocument;
import com.word_training.api.exceptions.WordTrainingApiException;
import com.word_training.api.mapper.definition.DefinitionMapper;
import com.word_training.api.mapper.example.ExampleMapper;
import com.word_training.api.mapper.record.RecordMapper;
import com.word_training.api.model.input.*;
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

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final RecordMapper recordMapper;
    private final DefinitionMapper definitionMapper;
    private final ExampleMapper exampleMapper;
    private final RecordRepository repository;

    @Override
    public Mono<RecordDocument> getRecord(String id) {
        return repository.findById(new ObjectId(id));
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
        var record = recordMapper.generateRecordByRequest(request);
        return repository.insert(record);
    }

    @Override
    public Mono<BulkWriteResult> modifyRecord(String id, RequestModifyRecord record) {
        isValidObjectId(id);
        Optional.ofNullable(record)
                .filter(r -> nonNull(r.getValue()) && nonNull(r.getType()))
                .orElseThrow(() -> new WordTrainingApiException("Request of change is empty"));

        var update = recordMapper.generateUpdateRecord(id, record);
        return repository.bulkUpdate(List.of(update));
    }

    @Override
    public Mono<BulkWriteResult> newDefinitionToRecord(String id, RequestDefinition def) {
        isValidObjectId(id);
        var update = definitionMapper.generateUpdateNewDefinition(id, def);

        return repository.bulkUpdate(update);
    }

    @Override
    public Mono<BulkWriteResult> modifyDefinitionInRecord(String idRecord, String definitionId, RequestModifyDefinition req) {
        isValidObjectId(idRecord);
        isValidObjectId(definitionId);
        var update = definitionMapper.generateUpdateModifyDefinition(idRecord, definitionId, req);
        return repository.bulkUpdate(List.of(update));
    }

    @Override
    public Mono<BulkWriteResult> deleteDefinitionInRecord(String idRecord, String definitionId) {
        isValidObjectId(idRecord);
        isValidObjectId(definitionId);
        var update = definitionMapper.generateUpdateDeleteDefinition(idRecord, definitionId);
        return repository.bulkUpdate(List.of(update));
    }

    @Override
    public Mono<BulkWriteResult> newExampleToDefinition(String idRecord, String definitionId, RequestExample def) {
        isValidObjectId(idRecord);
        isValidObjectId(definitionId);
        var update = exampleMapper.generateUpdateNewExample(idRecord, definitionId, def);
        return repository.bulkUpdate(List.of(update));
    }

    @Override
    public Mono<BulkWriteResult> modifyExampleInDefinition(String idRecord, String definitionId, String exampleId, RequestModifyExample req) {
        isValidObjectId(idRecord);
        isValidObjectId(definitionId);
        isValidObjectId(exampleId);

        Optional.ofNullable(req)
                .filter(r -> nonNull(r.getTranslation()) && nonNull(r.getInfo()) && nonNull(r.getSentence()))
                .orElseThrow(() -> new WordTrainingApiException("Request of change empty"));

        var update = exampleMapper.generateUpdateModifyExample(idRecord, definitionId, exampleId, req);
        return repository.bulkUpdate(List.of(update));
    }

    @Override
    public Mono<BulkWriteResult> deleteExampleInDefinition(String idRecord, String definitionId, String exampleId) {
        isValidObjectId(idRecord);
        isValidObjectId(definitionId);
        isValidObjectId(exampleId);
        var update = exampleMapper.generateUpdateDeleteExample(idRecord, definitionId, exampleId);
        return repository.bulkUpdate(List.of(update));
    }
}
