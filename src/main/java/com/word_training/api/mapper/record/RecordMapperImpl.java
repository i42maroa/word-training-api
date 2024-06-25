package com.word_training.api.mapper.record;

import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.Updates;
import com.word_training.api.domain.RecordDocument;
import com.word_training.api.exceptions.WordTrainingApiException;
import com.word_training.api.model.Definition;
import com.word_training.api.model.Example;
import com.word_training.api.model.enums.RecordType;
import com.word_training.api.model.input.RequestDefinition;
import com.word_training.api.model.input.RequestExample;
import com.word_training.api.model.input.RequestModifyRecord;
import com.word_training.api.model.input.RequestRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Optional;

import static com.word_training.api.constants.WordTrainingConstants.REC_TYPE_FIELD;
import static com.word_training.api.constants.WordTrainingConstants.REC_VALUE_FIELD;
import static com.word_training.api.model.enums.DefinitionType.isValidDefinitionType;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordMapperImpl implements RecordMapper {

    private final Clock clock;

    @Override
    public RecordDocument generateRecordByRequest(RequestRecord request) {
        var record = new RecordDocument();

        setNecessaryUpdateEnum(request.getType(), RecordType.isValidRecordType, record::setType, "Record type not define");
        setNecessaryUpdate(request.getValue(), record::setValue, "Record value not define");
        record.setCreationDate(OffsetDateTime.now(clock));
        record.setModificationDate(OffsetDateTime.now(clock));

        Optional.ofNullable(request.getDefinitions())
                .ifPresent(list -> {
                    var definitions = list.stream()
                            .map(this::setDefinitionIdAndExampleId)
                            .toList();
                    record.setDefinitions(definitions);
                });

        return record;
    }

    @Override
    public UpdateOneModel<Document> generateUpdateRecord(String id, RequestModifyRecord record) {
        var update = new Update();

        addOptionalSetUpdate(update, record.getValue(), REC_VALUE_FIELD);

        Optional.ofNullable(record.getType())
                .ifPresent(value ->
                        Optional.of(value)
                                .map(String::toUpperCase)
                                .filter(RecordType.isValidRecordType)
                                .ifPresentOrElse(v -> update.set(REC_TYPE_FIELD, v),
                                        () -> {
                                            throw new WordTrainingApiException("Record type is invalid");
                                        })
                );

        return new UpdateOneModel<>(filterById(id), Updates.combine(update.getUpdateObject(), updateModificationDate(clock)));
    }


    private Definition setDefinitionIdAndExampleId(RequestDefinition req) {
        var definition = new Definition();

        setNecessaryUpdate(req.getTranslation(), definition::setTranslation, "Definition translation not defined");
        setNecessaryUpdateEnum(req.getType(), isValidDefinitionType, definition::setType, "Definition type not defined");
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

        return definition;
    }

    private Example setExampleId(RequestExample req) {
        var example = new Example();

        setNecessaryUpdate(req.getTranslation(), example::setSentence, "Example sentence not defined");
        setOptionalUpdate(req.getTranslation(), example::setTranslation);
        setOptionalUpdate(req.getInfo(), example::setInfo);
        example.setExampleId(new ObjectId().toString());

        return example;
    }
}
