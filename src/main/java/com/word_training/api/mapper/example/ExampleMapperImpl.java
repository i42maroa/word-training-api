package com.word_training.api.mapper.example;

import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.word_training.api.exceptions.WordTrainingApiException;
import com.word_training.api.model.input.RequestExample;
import com.word_training.api.model.input.RequestModifyExample;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.word_training.api.constants.WordTrainingConstants.*;
@Service
@RequiredArgsConstructor
public class ExampleMapperImpl implements ExampleMapper{

    private final Clock clock;

    @Override
    public UpdateOneModel<Document> generateUpdateNewExample(String id, String definitionId, RequestExample request) {
        var update = new Document();

        Optional.ofNullable(request.getSentence())
                .ifPresentOrElse(v -> update.append(EX_SENTENCE_FIELD, v), () -> {
                    throw new WordTrainingApiException("Example sentence not defined");
                });

        setOptionalUpdate(update, request.getTranslation(), EX_TRANSLATION_FIELD);
        setOptionalUpdate(update, request.getInfo(), EX_INFO_FIELD);

        update.append(EX_EXAMPLE_ID_FIELD, new ObjectId().toString());

        var arrayFilter = new Document(PARAM + DOT + DEF_DEFINITIONID_FIELD, definitionId);

        var pushUpdate = Updates.addToSet(DEFINITIONS_FIELD + DOT + ARRAY_FILTER_PARAM + DOT + DEF_EXAMPLES_FIELD, update);

        return new UpdateOneModel<>(filterById(id), Updates.combine(pushUpdate, updateModificationDate(clock)),
                new UpdateOptions().arrayFilters(Collections.singletonList(arrayFilter)));
    }

    @Override
    public UpdateOneModel<Document> generateUpdateModifyExample(String id, String definitionId, String exampleId, RequestModifyExample request) {
        var update = new Update();
        var pathArray = DEFINITIONS_FIELD + DOT + ARRAY_FILTER_PARAM + DOT + DEF_EXAMPLES_FIELD + DOT + ARRAY_FILTER_PARAM2 + DOT;

        Optional.ofNullable(request.getSentence())
                .ifPresent(v -> update.set(pathArray + EX_SENTENCE_FIELD, v));
        Optional.ofNullable(request.getTranslation())
                .ifPresent(v -> update.set(pathArray + EX_TRANSLATION_FIELD, v));
        Optional.ofNullable(request.getInfo())
                .ifPresent(v -> update.set(pathArray + EX_INFO_FIELD, v));

        var arrayFilterDefinition = new Document(PARAM + DOT + DEF_DEFINITIONID_FIELD, definitionId);
        var arrayFilterExample = new Document(PARAM2 + DOT + EX_EXAMPLE_ID_FIELD, exampleId);
        return new UpdateOneModel<>(filterById(id), Updates.combine(update.getUpdateObject(), updateModificationDate(clock)),
                new UpdateOptions().arrayFilters(List.of(arrayFilterDefinition, arrayFilterExample)));
    }

    @Override
    public UpdateOneModel<Document> generateUpdateDeleteExample(String id, String definitionId, String exampleId) {
        var update = new Update();
        var pathArray = DEFINITIONS_FIELD + DOT + ARRAY_FILTER_PARAM + DOT + DEF_EXAMPLES_FIELD;

        update.pull(pathArray, new Document().append("exampleId", exampleId));

        var arrayFilterDefinition = new Document(PARAM + DOT + DEF_DEFINITIONID_FIELD, definitionId);
        return new UpdateOneModel<>(filterById(id), Updates.combine(update.getUpdateObject(), updateModificationDate(clock)),
                new UpdateOptions().arrayFilters(Collections.singletonList(arrayFilterDefinition)));
    }
}
