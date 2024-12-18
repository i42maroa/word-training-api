package com.word_training.api.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

public class MongoDbTestPopulatorConfig {

    @Value("classpath:mongodb/data/records.json")
    Resource records;


    @Bean
    public Jackson2RepositoryPopulatorFactoryBean repositoryPopulator(){
        var factory = new Jackson2RepositoryPopulatorFactoryBean();
        factory.setMapper(new ObjectMapper().registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));
        return factory;
    }
}
