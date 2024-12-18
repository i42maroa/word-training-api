package com.word_training.api.mapper;

import com.word_training.api.mapper.definition.DefinitionMapper;
import com.word_training.api.mapper.definition.DefinitionMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@ExtendWith(MockitoExtension.class)
public class DefinitionMapperTests {

    private final DefinitionMapper definitionMapper;

    DefinitionMapperTests(){
        var clock = Clock.fixed(Instant.parse("2023-05-03T10:00:00.000Z"), ZoneId.of("UTC"));
        this.definitionMapper = new DefinitionMapperImpl(clock);
    }

}
