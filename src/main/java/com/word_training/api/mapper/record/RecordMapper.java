package com.word_training.api.mapper.record;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOneModel;
import com.word_training.api.domain.RecordDocument;
import com.word_training.api.mapper.BaseMapper;
import com.word_training.api.model.input.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface RecordMapper extends BaseMapper {

    RecordDocument generateRecordByRequest(RequestRecord req);
    UpdateOneModel<Document> generateUpdateRecord(String id, RequestModifyRecord record);
}
