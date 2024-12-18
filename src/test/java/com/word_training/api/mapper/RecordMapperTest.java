package com.word_training.api.mapper;

import com.word_training.api.config.ClockTestConfiguration;
import com.word_training.api.mapper.record.RecordMapper;
import com.word_training.api.mapper.record.RecordMapperImpl;
import com.word_training.api.moks.ApiRequestMock;
import com.word_training.api.moks.RecordMoks;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;

public class RecordMapperTest {

    private Clock clock = new ClockTestConfiguration().testClock();

    private RecordMapper recordMapper;

    @BeforeEach
    void setUp() {
        recordMapper = new RecordMapperImpl(clock);
    }

    @Test
    public void generateNewRequestMap(){
        var recordRequest = ApiRequestMock.requestRecordMock();


        var recordResult = recordMapper.generateNewRecord(recordRequest);

        //assertEquals(recordResult)
        assertNotNull(recordResult);
    }
}
