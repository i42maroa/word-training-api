package com.word_training.api.mapper;

import com.mongodb.client.model.UpdateOptions;
import com.word_training.api.exceptions.WordTrainingApiException;
import com.word_training.api.model.Example;
import com.word_training.api.model.input.RequestExample;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface BaseMapper {

    default void setOptionalField(String field, Consumer<String> fieldSet){
        Optional.ofNullable(field).ifPresent(fieldSet);
    }

    default void setNecessaryField(String field, Consumer<String> fieldSet, String errorText){
        Optional.ofNullable(field)
                .ifPresentOrElse(fieldSet, () -> {throw new WordTrainingApiException(errorText);});
    }

    default void setNecessaryEnum(String field, Predicate<String> enumFilter, Consumer<String> fieldSet, String errorText){
        Optional.ofNullable(field)
                .map(String::toUpperCase)
                .filter(enumFilter)
                .ifPresentOrElse(fieldSet, () -> {
                    throw new WordTrainingApiException(errorText);
                });
    }

    default void setOptionalEnum(String field, Predicate<String> enumFilter, Consumer<String> fieldSet){
        Optional.ofNullable(field)
                .map(String::toUpperCase)
                .filter(enumFilter)
                .ifPresentOrElse(fieldSet, () -> fieldSet.accept(""));
    }

    default Example setExampleId(RequestExample req) {
        var example = new Example();

        setNecessaryField(req.getSentence(), example::setSentence, "Example sentence not defined");
        setOptionalField(req.getTranslation(), example::setTranslation);
        setOptionalField(req.getInfo(), example::setInfo);
        Optional.ofNullable(req.getExampleId())
                .ifPresentOrElse(example::setExampleId, () -> example.setExampleId(new ObjectId().toString()));

        return example;
    }
}
