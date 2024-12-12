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
        record.setRecordId(new ObjectId().toString());

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
    public Update generateUpdateToRecord(String recordId, RequestRecord request) {

        var record = new Record();
        setNecessaryUpdateEnum(request.getType(), RecordType.isValidRecordType, record::setType, "Record type not define");
        setNecessaryUpdate(request.getValue(), record::setValue, "Record value not define");

        Optional.ofNullable(request.getDefinitions())
                .ifPresent(list -> {
                    var definitions = list.stream()
                            .map(this::setDefinitionIdAndExampleId)
                            .toList();
                    record.setDefinitions(definitions);
                });

        return new Update()
                .set("value", record.getValue())
                .set("type", record.getType())
                .set("definitions", record.getDefinitions())
                .set("regModificationDate", OffsetDateTime.now(clock));
    }


    private Definition setDefinitionIdAndExampleId(RequestDefinition req) {
        var definition = new Definition();

        setNecessaryUpdate(req.getTranslation(), definition::setTranslation, "Definition translation not defined");
        setOptionalUpdateEnum(req.getType(), isValidDefinitionType, definition::setType);
        setOptionalUpdate(req.getInfo(), definition::setInfo);

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
