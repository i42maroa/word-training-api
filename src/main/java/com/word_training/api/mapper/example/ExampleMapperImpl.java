package com.word_training.api.mapper.example;

import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.Updates;
import com.word_training.api.exceptions.WordTrainingApiException;
import com.word_training.api.model.Example;
import com.word_training.api.model.input.RequestExample;
import com.word_training.api.model.input.RequestModifyExample;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Optional;

import static com.word_training.api.constants.WordTrainingConstants.*;

@Service
@RequiredArgsConstructor
public class ExampleMapperImpl implements ExampleMapper {

    private final Clock clock;

    @Override
    public Update generateUpdateNewExample(String id, String definitionId, RequestExample req) {
        var example = new Example();

        setNecessaryUpdate(req.getSentence(), example::setSentence, "Example sentence not defined");
        setOptionalUpdate(req.getTranslation(), example::setTranslation);
        setOptionalUpdate(req.getInfo(), example::setInfo);
        example.setExampleId(new ObjectId().toString());

        return new Update()
                .set("regModificationDate", OffsetDateTime.now(clock))
                .addToSet(DEFINITIONS_FIELD + DOT + ARRAY_FILTER_PARAM + DOT + DEF_EXAMPLES_FIELD, example)
                .filterArray(PARAM + DOT + DEF_DEFINITIONID_FIELD, definitionId);
    }

    @Override
    public UpdateOneModel<Document> generateUpdateModifyExample(String id, String definitionId, String exampleId, RequestModifyExample request) {
        var updateFields = new Document();
        var pathArray = DEFINITIONS_FIELD + DOT + ARRAY_FILTER_PARAM + DOT + DEF_EXAMPLES_FIELD + DOT + ARRAY_FILTER_PARAM2 + DOT;

        setOptionalUpdate(updateFields, request.getSentence(), pathArray + EX_SENTENCE_FIELD);
        setOptionalUpdate(updateFields, request.getTranslation(), pathArray + EX_TRANSLATION_FIELD);
        setOptionalUpdate(updateFields, request.getInfo(), pathArray + EX_INFO_FIELD);

        var arraysFilters = new HashMap<String, String>();
        arraysFilters.put(PARAM + DOT + DEF_DEFINITIONID_FIELD, definitionId);
        arraysFilters.put(PARAM2 + DOT + EX_EXAMPLE_ID_FIELD, exampleId);

        var update = generateSetUpdate(updateFields);

        return new UpdateOneModel<>(filterById(id), Updates.combine(update, updateModificationDate(clock)),
                this.generateArrayFilters(arraysFilters));
    }

    @Override
    public UpdateOneModel<Document> generateUpdateDeleteExample(String id, String definitionId, String exampleId) {
        var update = new Update();
        var pathArray = DEFINITIONS_FIELD + DOT + ARRAY_FILTER_PARAM + DOT + DEF_EXAMPLES_FIELD;

        update.pull(pathArray, new Document().append(EX_EXAMPLE_ID_FIELD, exampleId));

        return new UpdateOneModel<>(filterById(id), Updates.combine(update.getUpdateObject(), updateModificationDate(clock)),
                this.generateSingletonArrayFilter(PARAM + DOT + DEF_DEFINITIONID_FIELD, definitionId));
    }
}
