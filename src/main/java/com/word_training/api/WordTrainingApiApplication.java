package com.word_training.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
public class WordTrainingApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WordTrainingApiApplication.class, args);
	}

}
