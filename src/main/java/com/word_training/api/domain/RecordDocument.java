package com.word_training.api.domain;

import com.word_training.api.model.Record;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "records")
@NoArgsConstructor
public class RecordDocument extends Record {

    @MongoId
    ObjectId id;
}
