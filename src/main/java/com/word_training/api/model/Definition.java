package com.word_training.api.model;

import com.word_training.api.model.enums.DefinitionType;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

@Data
public class Definition {

    @Indexed
    private String definitionId;

    private String translation;
    private String type;
    private String info;
    private List<Example> examples;
}
