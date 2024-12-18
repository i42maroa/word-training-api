package com.word_training.api.service.impl;

import com.word_training.api.domain.RecordDocument;
import com.word_training.api.mapper.definition.DefinitionMapper;
import com.word_training.api.model.input.RequestDefinition;
import com.word_training.api.repository.RecordRepository;
import com.word_training.api.service.DefinitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DefinitionServiceImpl implements DefinitionService {

    private final RecordRepository repository;
    private final DefinitionMapper definitionMapper;

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
}
