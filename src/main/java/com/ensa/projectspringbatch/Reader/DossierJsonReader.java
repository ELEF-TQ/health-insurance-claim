package com.ensa.projectspringbatch.Reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ensa.projectspringbatch.Model.Dossier;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DossierJsonReader {

    @Bean
    public JsonItemReader<Dossier> jsonItemReader() {
        ObjectMapper objectMapper = new ObjectMapper();
        // Register the JavaTimeModule to handle Java 8 date/time types
        objectMapper.registerModule(new JavaTimeModule());

        JacksonJsonObjectReader<Dossier> jsonObjectReader = new JacksonJsonObjectReader<>(Dossier.class);
        jsonObjectReader.setMapper(objectMapper);

        return new JsonItemReaderBuilder<Dossier>()
                .jsonObjectReader(jsonObjectReader)
                .resource(new ClassPathResource("dossiers.json"))
                .name("dossierJsonReader")
                .build();
    }
}


