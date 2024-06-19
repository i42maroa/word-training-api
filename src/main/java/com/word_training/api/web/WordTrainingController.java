package com.word_training.api.web;

import com.mongodb.bulk.BulkWriteResult;
import com.word_training.api.domain.RecordDocument;
import com.word_training.api.model.input.RequestDefinition;
import com.word_training.api.model.input.RequestExample;
import com.word_training.api.model.input.RequestModifyDefinition;
import com.word_training.api.model.input.RequestRecord;
import com.word_training.api.model.output.Pagination;
import com.word_training.api.service.RecordService;
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
public class WordTrainingController {

    private final RecordService service;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<RecordDocument>> getRecordById(@PathVariable("id") ObjectId id) {
        return service.getRecord(id)
                .map(ResponseEntity::ok)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/page/{type}")
    public Mono<ResponseEntity<Pagination<RecordDocument>>> getRecordPage(
            @PathVariable("type") String type,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        return service.getRecordPage(type, PageRequest.of(page, size))
                .map(ResponseEntity::ok)
                .subscribeOn(Schedulers.boundedElastic());
    }

    // RECORD
    @PostMapping
    public Mono<ResponseEntity<RecordDocument>> createNewRecord(@RequestBody RequestRecord request) {
        return service.newRecord(request)
                .map(ResponseEntity::ok)
                .subscribeOn(Schedulers.boundedElastic());
    }


    @PutMapping("/{id}")
    public Mono<ResponseEntity<BulkWriteResult>> modifyRecord(@PathVariable("id") ObjectId id,
                                                              @RequestBody RequestRecord request) {
        return service.modifyRecord(id, request)
                .map(ResponseEntity::ok)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<RecordDocument>> removeRecord(
            @PathVariable("id") ObjectId id) {
        return Mono.just(ResponseEntity.ok().build());
    }

    // DEFINITIONS
    @PostMapping("/{id}/definition")
    public Mono<ResponseEntity<BulkWriteResult>> createNewDefinition(
            @PathVariable("id") ObjectId id,
            @RequestBody RequestDefinition request) {
        return service.newDefinitionToRecord(id, request)
                .map(ResponseEntity::ok)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @PutMapping("/{id}/definition/{definitionId}")
    public Mono<ResponseEntity<BulkWriteResult>> modifyDefinition(
            @PathVariable("id") ObjectId id,
            @PathVariable("definitionId") String definitionId,
            @RequestBody RequestModifyDefinition request) {
        return service.modifyDefinitionInRecord(id, definitionId, request)
                .map(ResponseEntity::ok)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @DeleteMapping("/{id}/definition/{definitionId}")
    public Mono<ResponseEntity<BulkWriteResult>> removeDefinition(
            @PathVariable("id") ObjectId id,
            @PathVariable("definitionId") String definitionId) {
        return service.deleteDefinitionInRecord(id, definitionId)
                .map(ResponseEntity::ok)
                .subscribeOn(Schedulers.boundedElastic());
    }

    // EXAMPLES
    @PostMapping("/{id}/definition/{definitionId}/example")
    public Mono<ResponseEntity<BulkWriteResult>> createNewExample(
            @PathVariable("id") ObjectId id,
            @PathVariable("definitionId") String definitionId,
            @RequestBody RequestExample request) {
        return service.newExampleToDefinition(id, definitionId, request)
                .map(ResponseEntity::ok)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @PutMapping("/{id}/definition/{definitionId}/example/{exampleId}")
    public Mono<ResponseEntity<BulkWriteResult>> modifyExample(
            @PathVariable("id") ObjectId id,
            @PathVariable("definitionId") String definitionId,
            @PathVariable("exampleId") String exampleId,
            @RequestBody RequestExample request) {
        return service.modifyExampleInDefinition(id, definitionId, exampleId, request)
                .map(ResponseEntity::ok)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @DeleteMapping("/{id}/definition/{definitionId}/example/{exampleId}")
    public Mono<ResponseEntity<BulkWriteResult>> deleteExample(
            @PathVariable("id") ObjectId id,
            @PathVariable("definitionId") String definitionId,
            @PathVariable("exampleId") String exampleId) {
        return service.deleteExampleInDefinition(id, definitionId, exampleId)
                .map(ResponseEntity::ok)
                .subscribeOn(Schedulers.boundedElastic());
    }
}
