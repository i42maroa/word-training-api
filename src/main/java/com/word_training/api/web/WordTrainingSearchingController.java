package com.word_training.api.web;

import com.word_training.api.domain.RecordDocument;
import com.word_training.api.model.output.Pagination;
import com.word_training.api.service.RecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/record")
public class WordTrainingSearchingController {

    private final RecordService service;

    @Tag(name = "Records")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<RecordDocument>> getRecordById(@PathVariable("id") String id) {
        return service.getRecord(id)
                .map(ResponseEntity::ok)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Tag(name = "Records")
    @GetMapping("/page/{type}")
    public Mono<ResponseEntity<Pagination<RecordDocument>>> getRecordPage(
            @PathVariable("type") String type,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        return service.getRecordPage(type, PageRequest.of(page, size))
                .map(ResponseEntity::ok)
                .subscribeOn(Schedulers.boundedElastic());
    }
}
