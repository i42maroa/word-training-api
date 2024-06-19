package com.word_training.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Word Training API",
                version = "1.0",
                description = "This API exposes endpoints to manage word, phrasal verbs or expressions.",
                contact = @Contact(
                        name = "Antonio Marín",
                        email = "amarinprofesional@gmail.com",
                        url = "https://antonio-web.es"
                )
        ),
        tags = {
                @Tag(name = "Records", description = "Main registry"),
                @Tag(name = "Definitions", description = "Definition of a record"),
                @Tag(name = "Examples", description = "Example of a definition")
        })
@Configuration
public class OpenAPIConfiguration {
}