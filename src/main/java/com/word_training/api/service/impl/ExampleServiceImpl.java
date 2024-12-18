package com.word_training.api.service.impl;

import com.word_training.api.domain.RecordDocument;
import com.word_training.api.exceptions.WordTrainingApiException;
import com.word_training.api.mapper.example.ExampleMapper;
import com.word_training.api.model.input.RequestExample;
import com.word_training.api.model.input.RequestModifyExample;
import com.word_training.api.repository.RecordRepository;
import com.word_training.api.service.ExampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class ExampleServiceImpl implements ExampleService {

    private final ExampleMapper exampleMapper;
    private final RecordRepository repository;
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
