package com.ensa.projectspringbatch.Processor;

import com.ensa.projectspringbatch.Model.Dossier;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class CompositeProcessor {

    private final ValidationProcessor validationProcessor;
    private final CalculProcessor calculProcessor;

    @Bean
    public CompositeItemProcessor<Dossier, Dossier> compositeItemProcessor() {
        CompositeItemProcessor<Dossier, Dossier> compositeProcessor = new CompositeItemProcessor<>();
        compositeProcessor.setDelegates(Arrays.asList(validationProcessor, calculProcessor));
        return compositeProcessor;
    }
}

