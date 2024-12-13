package com.word_training.api.mapper.example;

import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.Updates;
import com.word_training.api.model.Definition;
import com.word_training.api.model.Example;
import com.word_training.api.model.input.RequestExample;
import com.word_training.api.model.input.RequestModifyExample;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.HashMap;

import static com.word_training.api.constants.WordTrainingConstants.*;
import static com.word_training.api.model.enums.DefinitionType.isValidDefinitionType;

@Service
@RequiredArgsConstructor
public class ExampleMapperImpl implements ExampleMapper {

    private final Clock clock;

    @Override
    public Update generateUpdateNewExample(String id, String definitionId, RequestExample req) {
        var example = new Example();

        setNecessaryField(req.getSentence(), example::setSentence, "Example sentence not defined");
        setOptionalField(req.getTranslation(), example::setTranslation);
        setOptionalField(req.getInfo(), example::setInfo);
        example.setExampleId(new ObjectId().toString());

        return new Update()
                .set(RECORD_REG_MODIFICATION_DATE_FIELD, OffsetDateTime.now(clock))
                .addToSet(RECORD_DEFINITIONS_FIELD + DOT + ARRAY_FILTER_PARAM + DOT + DEF_EXAMPLES_FIELD, example)
                .filterArray(PARAM + DOT + DEF_DEFINITION_ID_FIELD, definitionId);
    }

    @Override
    public Update generateUpdateModifyExample(String id, String definitionId, String exampleId, RequestModifyExample req) {
        var prefix = RECORD_DEFINITIONS_FIELD + DOT + ARRAY_FILTER_PARAM + DOT + DEF_EXAMPLES_FIELD + DOT + ARRAY_FILTER_PARAM2 + DOT;

        return new Update()
                .set(RECORD_REG_MODIFICATION_DATE_FIELD, OffsetDateTime.now(clock))
                .set(prefix + EX_SENTENCE_FIELD, req.getSentence())
                .set(prefix + EX_TRANSLATION_FIELD, req.getTranslation())
                .set(prefix + EX_INFO_FIELD, req.getInfo())
                .filterArray(PARAM + DOT + DEF_DEFINITION_ID_FIELD, definitionId)
                .filterArray(PARAM2 + DOT + EX_EXAMPLE_ID_FIELD, exampleId);
    }

    @Override
    public Update generateUpdateDeleteExample(String id, String definitionId, String exampleId) {
        var examplePathArray = RECORD_DEFINITIONS_FIELD + DOT + ARRAY_FILTER_PARAM + DOT + DEF_EXAMPLES_FIELD;

        return new Update()
                .set(RECORD_REG_MODIFICATION_DATE_FIELD, OffsetDateTime.now(clock))
                .pull(examplePathArray, Query.query(Criteria.where(PARAM + DOT + DEF_DEFINITION_ID_FIELD).is(definitionId)));
    }
}
