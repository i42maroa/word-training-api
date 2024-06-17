package com.word_training.api.mapper;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOneModel;
import com.word_training.api.domain.RecordDocument;
import com.word_training.api.model.input.RequestDefinition;
import com.word_training.api.model.input.RequestExample;
import com.word_training.api.model.input.RequestRecord;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface RecordMapper {

    RecordDocument generateRecordByRequest(RequestRecord req);

    UpdateOneModel<Document> generateUpdateRecord(ObjectId id, RequestRecord record);

    List<UpdateOneModel<Document>> generateUpdateNewDefinition(ObjectId id, RequestDefinition definition);

    UpdateOneModel<Document> generateUpdateNewExample(ObjectId id, String definitionId, RequestExample example);

    UpdateOneModel<Document> generateUpdateModifyExample(ObjectId id, String definitionId, String exampleId, RequestExample example);


    default Document updateModificationDate(Clock clock){
        return Optional.of(OffsetDateTime.now(clock))
                .map(date -> "{modificationDate:{'$date':'"+ date +"'}}")
                .map(Document::parse)
                .map(dateUpdate -> new Document().append("$set", dateUpdate))
                .orElseGet(Document::new);
    }

    default Bson filterById(ObjectId id){
        return Filters.eq("_id", id);
    }

}
