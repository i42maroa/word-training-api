package com.word_training.api.mapper.example;

import com.mongodb.client.model.UpdateOneModel;
import com.word_training.api.mapper.BaseMapper;
import com.word_training.api.model.input.RequestExample;
import com.word_training.api.model.input.RequestModifyExample;
import org.bson.Document;

public interface ExampleMapper extends BaseMapper {

    UpdateOneModel<Document> generateUpdateNewExample(String id, String definitionId, RequestExample example);
    UpdateOneModel<Document> generateUpdateModifyExample(String id, String definitionId, String exampleId, RequestModifyExample example);
    UpdateOneModel<Document> generateUpdateDeleteExample(String id, String definitionId, String exampleId);



}
