package com.word_training.api.mapper.record;

import com.word_training.api.domain.RecordDocument;
import com.word_training.api.mapper.BaseMapper;
import com.word_training.api.model.input.RequestRecord;
import org.springframework.data.mongodb.core.query.Update;

public interface RecordMapper extends BaseMapper {

    RecordDocument generateNewRecord(RequestRecord req);
    Update generateUpateModifyRecord(String id, RequestRecord record);
}
