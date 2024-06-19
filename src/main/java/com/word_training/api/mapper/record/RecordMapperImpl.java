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

        record.setCreationDate(OffsetDateTime.now());
        record.setModificationDate(OffsetDateTime.now());

        Optional.ofNullable(request)
                .map(RequestRecord::getType)
                .ifPresentOrElse(record::setType, () -> {
                    throw new WordTrainingApiException("Record type not define");
                });

        Optional.ofNullable(request)
                .map(RequestRecord::getValue)
                .ifPresentOrElse(record::setValue, () -> {
                    throw new WordTrainingApiException("Record value not define");
                });

        Optional.ofNullable(request)
                .map(RequestRecord::getDefinitions)
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

        Optional.ofNullable(record.getValue())
                .ifPresent(v -> update.set(REC_VALUE_FIELD, v));
        Optional.ofNullable(record.getType())
                .ifPresent(value ->
                        Optional.of(value)
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
        definition.setDefinitionId(new ObjectId().toString());

        Optional.ofNullable(req.getTranslation())
                .ifPresentOrElse(definition::setTranslation, () -> {
                    throw new WordTrainingApiException("Definition translation not defined");
                });

        Optional.ofNullable(req.getType())
                .filter(isValidDefinitionType)
                .ifPresentOrElse(definition::setTranslation, () -> {
                    throw new WordTrainingApiException("Definition type not defined");
                });

        Optional.ofNullable(req.getInfo()).ifPresent(definition::setInfo);

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

        Optional.ofNullable(req.getTranslation())
                .ifPresentOrElse(example::setSentence, () -> {
                    throw new WordTrainingApiException("Example sentence not defined");
                });

        Optional.ofNullable(req.getTranslation()).ifPresent(example::setTranslation);
        Optional.ofNullable(req.getInfo()).ifPresent(example::setInfo);
        example.setExampleId(new ObjectId().toString());
        return example;
    }


}
