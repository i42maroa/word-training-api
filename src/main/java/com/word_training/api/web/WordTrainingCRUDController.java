package com.word_training.api.web;

import com.mongodb.bulk.BulkWriteResult;
import com.word_training.api.domain.RecordDocument;
import com.word_training.api.model.error.ResponseError;
import com.word_training.api.model.input.*;
import com.word_training.api.service.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/record")
public class WordTrainingCRUDController {

    private final RecordService service;

    // RECORD
    @Tag(name = "Records")
    @Operation(summary = "Create a new record", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Record created")})
    @PostMapping
    public Mono<ResponseEntity<RecordDocument>> createNewRecord(
            @Parameter(description = "Record information", required = true)
            @RequestBody RequestRecord request) {
        return service.newRecord(request)
                .map(ResponseEntity::ok)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Tag(name = "Records")
    @Operation(summary = "Modify an existing record")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Record modified"),
            @ApiResponse(responseCode = "400", description = "Request data not valid",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class))}),
            @ApiResponse(responseCode = "404", description = "Record not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class))})
    })
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Object>> modifyRecord(
            @Parameter(description = "Record id", required = true)
            @PathVariable("id") String id,
            @Parameter(description = "Changes to apply in select record", required = true)
            @RequestBody RequestModifyRecord request) {
        return service.modifyRecord(id, request)
                .map(this::handleMongoResults)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Tag(name = "Records")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<RecordDocument>> removeRecord(
            @PathVariable("id") String id) {
        return Mono.just(ResponseEntity.ok().build());
    }

    // DEFINITIONS
    @Tag(name = "Definitions")
    @Operation(summary = "Create a new definition into a record")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "New definition added"),
            @ApiResponse(responseCode = "400", description = "Request data not valid",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class))}),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class))})
    })
    @PostMapping("/{id}/definition")
    public Mono<ResponseEntity<Object>> createNewDefinition(
            @Parameter(description = "Record id", required = true)
            @PathVariable("id") String id,
            @Parameter(description = "Definition information", required = true)
            @RequestBody RequestDefinition request) {
        return service.newDefinitionToRecord(id, request)
                .map(this::handleMongoResults)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Tag(name = "Definitions")
    @Operation(summary = "Modify an existing definition")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Definition modified"),
            @ApiResponse(responseCode = "400", description = "Request data not valid",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class))}),
            @ApiResponse(responseCode = "404", description = "Definition not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class))})
    })
    @PutMapping("/{id}/definition/{definitionId}")
    public Mono<ResponseEntity<Object>> modifyDefinition(
            @Parameter(description = "Record id", required = true)
            @PathVariable("id") String id,
            @Parameter(description = "Definition id", required = true)
            @PathVariable("definitionId") String definitionId,
            @Parameter(description = "New definition information", required = true)
            @RequestBody RequestModifyDefinition request) {
        return service.modifyDefinitionInRecord(id, definitionId, request)
                .map(this::handleMongoResults)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Tag(name = "Definitions")
    @Operation(summary = "Remove an existing definition")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Definition removed"),
            @ApiResponse(responseCode = "400", description = "Request data not valid",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class))}),
            @ApiResponse(responseCode = "404", description = "Definition not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class))})
    })
    @DeleteMapping("/{id}/definition/{definitionId}")
    public Mono<ResponseEntity<Object>> removeDefinition(
            @Parameter(description = "Record id", required = true)
            @PathVariable("id") String id,
            @Parameter(description = "Definition id", required = true)
            @PathVariable("definitionId") String definitionId) {
        return service.deleteDefinitionInRecord(id, definitionId)
                .map(this::handleMongoResults)
                .subscribeOn(Schedulers.boundedElastic());
    }

    // EXAMPLES
    @Tag(name = "Examples")
    @Operation(summary = "Create a new example to a definition")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Example created"),
            @ApiResponse(responseCode = "400", description = "Request data not valid",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class))}),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class))})
    })
    @PostMapping("/{id}/definition/{definitionId}/example")
    public Mono<ResponseEntity<Object>> createNewExample(
            @Parameter(description = "Record id", required = true)
            @PathVariable("id") String id,
            @Parameter(description = "Definition id", required = true)
            @PathVariable("definitionId") String definitionId,
            @Parameter(description = "New example information", required = true)
            @RequestBody RequestExample request) {
        return service.newExampleToDefinition(id, definitionId, request)
                .map(this::handleMongoResults)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Tag(name = "Examples")
    @Operation(summary = "Modify an existing example")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Example modified"),
            @ApiResponse(responseCode = "400", description = "Request data not valid",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class))}),
            @ApiResponse(responseCode = "404", description = "Example not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class))})
    })
    @PutMapping("/{id}/definition/{definitionId}/example/{exampleId}")
    public Mono<ResponseEntity<Object>> modifyExample(
            @Parameter(description = "Record id", required = true)
            @PathVariable("id") String id,
            @Parameter(description = "Definition id", required = true)
            @PathVariable("definitionId") String definitionId,
            @Parameter(description = "Example id", required = true)
            @PathVariable("exampleId") String exampleId,
            @Parameter(description = "New example information", required = true)
            @RequestBody RequestModifyExample request) {
        return service.modifyExampleInDefinition(id, definitionId, exampleId, request)
                .map(this::handleMongoResults)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Tag(name = "Examples")
    @Operation(summary = "Remove an existing example")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Example removed"),
            @ApiResponse(responseCode = "400", description = "Request data not valid",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class))}),
            @ApiResponse(responseCode = "404", description = "Example not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class))})
    })
    @DeleteMapping("/{id}/definition/{definitionId}/example/{exampleId}")
    public Mono<ResponseEntity<Object>> deleteExample(
            @Parameter(description = "Record id", required = true)
            @PathVariable("id") String id,
            @Parameter(description = "Definition id", required = true)
            @PathVariable("definitionId") String definitionId,
            @Parameter(description = "Example id", required = true)
            @PathVariable("exampleId") String exampleId) {
        return service.deleteExampleInDefinition(id, definitionId, exampleId)
                .map(this::handleMongoResults)
                .subscribeOn(Schedulers.boundedElastic());
    }

    private ResponseEntity<Object> handleMongoResults(BulkWriteResult bulkWriteResult) {
        return Optional.of(bulkWriteResult.getModifiedCount())
                .filter(count -> count > 0)
                .map(r -> ResponseEntity.ok().build())
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseError(HttpStatus.NOT_FOUND.value(), "Ressource not found")));
    }
}
