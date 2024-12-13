package com.word_training.api.mapper.example;

import com.mongodb.client.model.UpdateOneModel;
import com.word_training.api.mapper.BaseMapper;
import com.word_training.api.model.input.RequestExample;
import com.word_training.api.model.input.RequestModifyExample;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Update;

public interface ExampleMapper extends BaseMapper {

    Update generateUpdateNewExample(String id, String definitionId, RequestExample example);
    Update generateUpdateModifyExample(String id, String definitionId, String exampleId, RequestModifyExample example);
    Update generateUpdateDeleteExample(String id, String definitionId, String exampleId);
}
