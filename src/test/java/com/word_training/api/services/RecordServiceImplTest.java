package com.word_training.api.services;

import com.word_training.api.mapper.record.RecordMapper;
import com.word_training.api.moks.RecordMoks;
import com.word_training.api.repository.RecordRepository;
import com.word_training.api.service.RecordService;
import com.word_training.api.service.impl.RecordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecordServiceImplTest {


    private RecordService recordService;
    @Mock
    private RecordRepository repository;
    @Mock
    private RecordMapper recordMapper;


    @BeforeEach
    void setUp(){
        recordService = new RecordServiceImpl(recordMapper, repository);
    }

    @Test
    void getRecordId(){
        var recordResponse = RecordMoks.recordMock();
        when(recordService.getRecord(anyString())).thenReturn(Mono.just(recordResponse));

        StepVerifier.create(recordService.getRecord("1234"))
                .expectNext(recordResponse)
                .verifyComplete();
    }
}
