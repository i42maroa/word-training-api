package com.word_training.api.web;

import com.word_training.api.domain.RecordDocument;
import com.word_training.api.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class WordTrainingController {

    private final RecordRepository repository;

    @GetMapping("/record")
    public Mono<RecordDocument> getRecordById(@RequestParam ObjectId id){
        return repository.findById(id);
    }

}
