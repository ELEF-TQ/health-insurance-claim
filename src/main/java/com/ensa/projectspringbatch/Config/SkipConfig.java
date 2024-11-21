package com.ensa.projectspringbatch.Config;

import com.ensa.projectspringbatch.Exception.NonRecoverableException;
import com.ensa.projectspringbatch.Model.Dossier;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class SkipConfig {

    @Bean
    public SkipListener<Dossier, Dossier> skipListener() {
        return new SkipListener<>() {
            @Override
            public void onSkipInRead(Throwable t) {
                System.out.println("Skipped item on read: " + t.getMessage());
            }

            @Override
            public void onSkipInProcess(Dossier item, Throwable t) {
                System.out.println("Skipped item on process: " + item + " Reason: " + t.getMessage());
            }

            @Override
            public void onSkipInWrite(Dossier item, Throwable t) {
                System.out.println("Skipped item on write: " + item + " Reason: " + t.getMessage());
            }
        };
    }

    @Bean
    public Step skipStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ItemReader<Dossier> reader,
            @Qualifier("compositeItemProcessor") ItemProcessor<Dossier, Dossier> processor,
            ItemWriter<Dossier> writer
    ) {
        return new StepBuilder("skipStep", jobRepository)
                .<Dossier, Dossier>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skip(NonRecoverableException.class)
                .skipLimit(5)
                .listener(skipListener())
                .build();
    }
}