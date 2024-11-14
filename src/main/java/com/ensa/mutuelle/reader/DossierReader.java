package com.ensa.mutuelle.reader;

import com.ensa.mutuelle.model.Dossier;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.batch.item.json.GsonJsonObjectReader;

@Configuration
public class DossierReader {

    @Bean
    public JsonItemReader<Dossier> dossierJsonItemReader() {
        return new JsonItemReaderBuilder<Dossier>()
                .jsonObjectReader(new GsonJsonObjectReader<>(Dossier.class))
                .resource(new FileSystemResource("src/main/java/data/dossiers.json"))
                .name("dossierJsonItemReader")
                .build();
    }
}