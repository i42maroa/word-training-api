package com.word_training.api.service.impl;

import com.word_training.api.domain.RecordDocument;
import com.word_training.api.exceptions.WordTrainingApiException;
import com.word_training.api.mapper.definition.DefinitionMapper;
import com.word_training.api.mapper.example.ExampleMapper;
import com.word_training.api.mapper.record.RecordMapper;
import com.word_training.api.model.enums.RecordType;
import com.word_training.api.model.input.*;
import com.word_training.api.model.output.Pagination;
import com.word_training.api.model.queries.RecordPageInputQuery;
import com.word_training.api.repository.RecordRepository;
import com.word_training.api.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

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
        return repository.findByRecordId(id);
    }

    @Override
    public Mono<Pagination<RecordDocument>> getRecordPage(RecordPageInputQuery query, Pageable pageRequest) {

        //TODO: clean
       var cleanQuery = Optional.ofNullable(query)
                .map(q -> {
                    var listType = Stream.ofNullable(query.getTypeIn())
                            .flatMap(Collection::stream)
                            .map(String::toUpperCase)
                            .filter(RecordType.isValidRecordType)
                            .toList();

                    query.setTypeIn(listType);
                    return query;
                })
               .orElse(new RecordPageInputQuery());

        var listWordsMono = repository.findWordsPage(cleanQuery, pageRequest).collectList();
        var countMono = repository.countWords(cleanQuery);

        return Mono.zip(listWordsMono, countMono, (items, count) -> new Pagination<>(items, pageRequest, count));
    }

    @Override
    public Mono<RecordDocument> newRecord(RequestRecord request) {
        var record = recordMapper.generateNewRecord(request);
        return repository.insert(record);
    }

    @Override
    public Mono<RecordDocument> modifyRecord(String recordId, RequestRecord record) {
        isValidObjectId(recordId);

        Optional.ofNullable(record)
                .orElseThrow(() -> new WordTrainingApiException("Request of change is empty"));

        var update = recordMapper.generateUpateModifyRecord(recordId, record);
        return repository.findAndModify(recordId, update);
    }

    @Override
    public Mono<Void> deleteRecord(String recordId) {
        return repository.deleteByRecordId(recordId);
    }

    @Override
    public Mono<RecordDocument> newDefinitionToRecord(String recordId, RequestDefinition def) {
        var update = definitionMapper.generateUpdateNewDefinition(recordId, def);
        return repository.findAndModify(recordId, update);
    }

    @Override
    public Mono<RecordDocument> modifyDefinitionInRecord(String recordId, String definitionId, RequestDefinition req) {
        var update = definitionMapper.generateUpdateModifyDefinition(recordId, definitionId, req);
        return repository.findAndModify(recordId, update);
    }

    @Override
    public Mono<RecordDocument> deleteDefinitionInRecord(String recordId, String definitionId) {
        var update = definitionMapper.generateUpdateDeleteDefinition(recordId, definitionId);
        return repository.findAndModify(recordId, update);
    }

    @Override
    public Mono<RecordDocument> newExampleToDefinition(String idRecord, String definitionId, RequestExample def) {
        var update = exampleMapper.generateUpdateNewExample(idRecord, definitionId, def);
        return repository.findAndModify(idRecord, update);
    }

    @Override
    public Mono<RecordDocument> modifyExampleInDefinition(String idRecord, String definitionId, String exampleId, RequestModifyExample req) {
        Optional.ofNullable(req)
                .filter(r -> nonNull(r.getTranslation()) || nonNull(r.getInfo()) || nonNull(r.getSentence()))
                .orElseThrow(() -> new WordTrainingApiException("Request of change is empty"));

        var update = exampleMapper.generateUpdateModifyExample(idRecord, definitionId, exampleId, req);
        return repository.findAndModify(idRecord, update);
    }

    @Override
    public Mono<RecordDocument> deleteExampleInDefinition(String idRecord, String definitionId, String exampleId) {
        var update = exampleMapper.generateUpdateDeleteExample(idRecord, definitionId, exampleId);
        return repository.findAndModify(idRecord, update);
    }
}
