package com.word_training.api.mapper.definition;

import com.word_training.api.mapper.BaseMapper;
import com.word_training.api.model.input.RequestDefinition;
import org.springframework.data.mongodb.core.query.Update;

public interface DefinitionMapper extends BaseMapper {

    Update generateUpdateNewDefinition(String id, RequestDefinition definition);

    Update generateUpdateModifyDefinition(String id, String definitionId, RequestDefinition definition);

    Update generateUpdateDeleteDefinition(String id, String definitionId);
}
