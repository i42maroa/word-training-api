package com.word_training.api.mapper.record;

import com.word_training.api.domain.RecordDocument;
import com.word_training.api.model.Definition;
import com.word_training.api.model.Record;
import com.word_training.api.model.enums.RecordType;
import com.word_training.api.model.input.RequestDefinition;
import com.word_training.api.model.input.RequestRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Optional;

import static com.word_training.api.constants.WordTrainingConstants.*;
import static com.word_training.api.model.enums.DefinitionType.isValidDefinitionType;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordMapperImpl implements RecordMapper {

    private final Clock clock;

    @Override
    public RecordDocument generateNewRecord(RequestRecord request) {
        var record = new RecordDocument();

        setNecessaryEnum(request.getType(), RecordType.isValidRecordType, record::setType, "Record type not define");
        setNecessaryField(request.getValue(), record::setValue, "Record value not define");
        record.setCreationDate(OffsetDateTime.now(clock));
        record.setModificationDate(OffsetDateTime.now(clock));
        record.setRecordId(new ObjectId().toString());
        setDefinitions(request, record);

        return record;
    }

    @Override
    public Update generateUpateModifyRecord(String recordId, RequestRecord request) {

        var record = new Record();
        setNecessaryEnum(request.getType(), RecordType.isValidRecordType, record::setType, "Record type not define");
        setNecessaryField(request.getValue(), record::setValue, "Record value not define");
        setDefinitions(request, record);

        return new Update()
                .set(RECORD_VALUE, record.getValue())
                .set(RECORD_TYPE, record.getType())
                .set(RECORD_DEFINITIONS_FIELD, record.getDefinitions())
                .set(RECORD_REG_MODIFICATION_DATE_FIELD, OffsetDateTime.now(clock));
    }

    private void setDefinitions(RequestRecord request, Record record) {
        Optional.ofNullable(request.getDefinitions())
                .ifPresent(list -> {
                    var definitions = list.stream()
                            .map(this::mapDefinitionRequest)
                            .toList();
                    record.setDefinitions(definitions);
                });
    }

    private Definition mapDefinitionRequest(RequestDefinition req) {
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

        return definition;
    }
}
