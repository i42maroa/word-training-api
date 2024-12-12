package com.word_training.api.mapper.definition;

import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.word_training.api.model.Definition;
import com.word_training.api.model.input.RequestDefinition;
import com.word_training.api.model.input.RequestModifyDefinition;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.word_training.api.constants.WordTrainingConstants.*;
import static com.word_training.api.model.enums.DefinitionType.isValidDefinitionType;

@Service
@RequiredArgsConstructor
public class DefinitionMapperImpl implements DefinitionMapper {

    private final Clock clock;

    @Override
    public List<UpdateOneModel<Document>> generateUpdateNewDefinition(String id, RequestDefinition request) {
        var update = new Document();

        setNecesaryUpdate(update, request.getTranslation(), DEF_TRANSLATION_FIELD, "Definition translation not defined");
        setOptionalUpdateEnum(update, isValidDefinitionType, request.getType(), DEF_TYPE_FIELD);
        setOptionalUpdate(update, request.getInfo(), DEF_INFO_FIELD);

        var definitionId = setIdAutogenerated(update, DEF_DEFINITIONID_FIELD);
        var pushUpdate = Updates.addToSet(DEFINITIONS_FIELD, update);
        var updateOneList = new ArrayList<UpdateOneModel<Document>>();
        updateOneList.add(new UpdateOneModel<>(filterById(id), Updates.combine(pushUpdate, updateModificationDate(clock))));

        Optional.ofNullable(request.getExamples())
                .map(examples -> {
                    var exampleUpdate = new Document();
                    examples.forEach(e -> {
                        setNecesaryUpdate(exampleUpdate, e.getSentence(), EX_SENTENCE_FIELD, "Example sentence not defined");
                        setOptionalUpdate(exampleUpdate, e.getTranslation(), EX_TRANSLATION_FIELD);
                        setOptionalUpdate(exampleUpdate, e.getInfo(), EX_INFO_FIELD);
                        setIdAutogenerated(exampleUpdate, EX_EXAMPLE_ID_FIELD);
                    });

                    return exampleUpdate;
                })
                .map(v -> {
                    var pushUpdateExamples = Updates.addToSet(DEFINITIONS_FIELD + DOT + ARRAY_FILTER_PARAM + DOT + DEF_EXAMPLES_FIELD, v);

                    return new UpdateOneModel<Document>(filterById(id), Updates.combine(pushUpdateExamples, updateModificationDate(clock)),
                            this.generateSingletonArrayFilter(PARAM + DOT + DEF_DEFINITIONID_FIELD, definitionId));
                })
                .ifPresent(updateOneList::add);

        return updateOneList;
    }

    @Override
    public Update generateUpdateNewDefinition2(String id, RequestDefinition req) {
        var definition = new Definition();

        setNecessaryUpdate(req.getTranslation(), definition::setTranslation, "Definition translation not defined");
        setOptionalUpdateEnum(req.getType(), isValidDefinitionType, definition::setType);
        setOptionalUpdate(req.getInfo(), definition::setInfo);
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
                .addToSet("definitions", definition)
                .set("regModificationDate", OffsetDateTime.now(clock));
    }

    @Override
    public UpdateOneModel<Document> generateUpdateModifyDefinition(String id, String definitionId, RequestModifyDefinition request) {
        var updateFields = new Document();
        var pathArray = DEFINITIONS_FIELD + DOT + ARRAY_FILTER_PARAM + DOT;

        setOptionalUpdate(updateFields, request.getTranslation(), pathArray + DEF_TRANSLATION_FIELD);
        setOptionalUpdate(updateFields, request.getInfo(), pathArray + DEF_INFO_FIELD);
        Optional.ofNullable(request.getType())
                .ifPresent(value -> setNecesaryUpdateEnum(updateFields, isValidDefinitionType, value, pathArray + DEF_TYPE_FIELD, "Definition type not valid"));

        var update = generateSetUpdate(updateFields);

        return new UpdateOneModel<>(filterById(id), Updates.combine(update, updateModificationDate(clock)),
                this.generateSingletonArrayFilter(PARAM + DOT + DEF_DEFINITIONID_FIELD, definitionId));
    }

    @Override
    public UpdateOneModel<Document> generateUpdateDeleteDefinition(String id, String definitionId) {
        var update = new Update();
        update.pull(DEFINITIONS_FIELD, new Document().append(DEF_DEFINITIONID_FIELD, definitionId));

        return new UpdateOneModel<>(filterById(id), Updates.combine(update.getUpdateObject(), updateModificationDate(clock)));
    }
}
