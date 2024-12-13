package com.word_training.api.mapper.definition;

import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.Updates;
import com.word_training.api.model.Definition;
import com.word_training.api.model.input.RequestDefinition;
import com.word_training.api.model.input.RequestModifyDefinition;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Optional;

import static com.word_training.api.constants.WordTrainingConstants.*;
import static com.word_training.api.model.enums.DefinitionType.isValidDefinitionType;

@Service
@RequiredArgsConstructor
public class DefinitionMapperImpl implements DefinitionMapper {

    private final Clock clock;

    @Override
    public Update generateUpdateNewDefinition(String id, RequestDefinition req) {
        var definition = new Definition();

        setNecessaryField(req.getTranslation(), definition::setTranslation, "Definition translation not defined");
        setOptionalEnum(req.getType(), isValidDefinitionType, definition::setType);
        setOptionalField(req.getInfo(), definition::setInfo);
        definition.setDefinitionId(new ObjectId().toString());

        Optional.ofNullable(req.getExamples())
                .ifPresent(examples -> {
                            var ex = examples.stream()
                                    .map(this::setExampleId)
                                    .toList();
                            definition.setExamples(ex);
                        }
                );
        return new Update()
                .addToSet(RECORD_DEFINITIONS_FIELD, definition)
                .set(RECORD_REG_MODIFICATION_DATE_FIELD, OffsetDateTime.now(clock));
    }

    @Override
    public Update generateUpdateModifyDefinition(String id, String definitionId, RequestDefinition req) {
        var definition = new Definition();

        setNecessaryField(req.getTranslation(), definition::setTranslation, "Definition translation not defined");
        setOptionalEnum(req.getType(), isValidDefinitionType, definition::setType);
        setOptionalField(req.getInfo(), definition::setInfo);

        Optional.ofNullable(req.getDefinitionId())
                .ifPresentOrElse(definition::setDefinitionId, () -> definition.setDefinitionId(new ObjectId().toString()));

        Optional.ofNullable(req.getExamples())
                .ifPresent(examples -> {
                            var ex = examples.stream()
                                    .map(this::setExampleId)
                                    .toList();
                            definition.setExamples(ex);
                        }
                );

        var prefix = RECORD_DEFINITIONS_FIELD + DOT + ARRAY_FILTER_PARAM + DOT;

        return new Update()
                .set(RECORD_REG_MODIFICATION_DATE_FIELD, OffsetDateTime.now(clock))
                .set(prefix + DEF_TYPE_FIELD, definition.getType())
                .set(prefix + DEF_TRANSLATION_FIELD, definition.getTranslation())
                .set(prefix + DEF_EXAMPLES_FIELD, definition.getExamples())
                .filterArray(PARAM + DOT + DEF_DEFINITION_ID_FIELD, definitionId);
    }

    @Override
    public Update generateUpdateDeleteDefinition(String id, String definitionId) {
        return new Update()
                .set(RECORD_REG_MODIFICATION_DATE_FIELD, OffsetDateTime.now(clock))
                .pull(RECORD_DEFINITIONS_FIELD, Query.query(Criteria.where(RECORD_DEFINITIONS_FIELD).is(definitionId)));
    }
}
