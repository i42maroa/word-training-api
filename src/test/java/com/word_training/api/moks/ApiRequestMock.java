package com.word_training.api.moks;

import com.word_training.api.model.input.RequestDefinition;
import com.word_training.api.model.input.RequestExample;
import com.word_training.api.model.input.RequestRecord;

import java.util.List;

public class ApiRequestMock {

    public static RequestRecord requestRecordMock(){
        var recordRequest = new RequestRecord();
        recordRequest.setValue("car");
        recordRequest.setType("WORD");
        recordRequest.setDefinitions(List.of(requestDefinitionMock()));
        return recordRequest;
    }

    public static RequestDefinition requestDefinitionMock(){
        var definitionRequest = new RequestDefinition();
        definitionRequest.setType("NOUN");
        definitionRequest.setTranslation("coche");
        definitionRequest.setExamples(List.of(requestExampleMock()));
        return definitionRequest;
    }

    public static RequestExample requestExampleMock(){
        var exampleRequest = new RequestExample();
        exampleRequest.setSentence("The car is red");
        exampleRequest.setTranslation("El coche es rojo");
        exampleRequest.setInfo("Additional info");

        return exampleRequest;
    }
}
