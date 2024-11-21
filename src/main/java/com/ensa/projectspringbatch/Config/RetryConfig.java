package com.ensa.projectspringbatch.Config;

import com.ensa.projectspringbatch.Exception.RecoverableException;
import com.ensa.projectspringbatch.Model.Dossier;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class RetryConfig {

    @Bean
    public Step retryStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ItemReader<Dossier> reader,
            @Qualifier("compositeItemProcessor") ItemProcessor<Dossier, Dossier> processor,
            ItemWriter<Dossier> writer
    ) {
        return new StepBuilder("retryStep", jobRepository)
                .<Dossier, Dossier>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .retry(RecoverableException.class)
                .retryLimit(3)
                .build();
    }
}