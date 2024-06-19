package com.word_training.api.mapper.definition;

import com.mongodb.client.model.UpdateOneModel;
import com.word_training.api.mapper.BaseMapper;
import com.word_training.api.model.input.RequestDefinition;
import com.word_training.api.model.input.RequestModifyDefinition;
import org.bson.Document;

import java.util.List;

public interface DefinitionMapper extends BaseMapper {

    List<UpdateOneModel<Document>> generateUpdateNewDefinition(String id, RequestDefinition definition);
    UpdateOneModel<Document> generateUpdateModifyDefinition(String id, String definitionId, RequestModifyDefinition definition);
    UpdateOneModel<Document> generateUpdateDeleteDefinition(String id, String definitionId);
}
