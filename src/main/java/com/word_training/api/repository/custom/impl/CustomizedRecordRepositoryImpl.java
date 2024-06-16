package com.word_training.api.repository.custom.impl;

import com.word_training.api.repository.custom.CustomizedRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

@RequiredArgsConstructor
public class CustomizedRecordRepositoryImpl implements CustomizedRecordRepository {

    private final ReactiveMongoTemplate reactiveMongoTemplate;
}
