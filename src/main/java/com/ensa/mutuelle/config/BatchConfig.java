package com.ensa.mutuelle.config;

import com.ensa.mutuelle.model.Dossier;
import com.ensa.mutuelle.processor.DossierProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DossierProcessor dossierProcessor;
    private final DataSource dataSource;

    public BatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                       DossierProcessor dossierProcessor, DataSource dataSource) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.dossierProcessor = dossierProcessor;
        this.dataSource = dataSource;
    }

    @Bean
    public JsonItemReader<Dossier> dossierJsonItemReader() {
        return new JsonItemReaderBuilder<Dossier>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(Dossier.class))
                .resource(new ClassPathResource("dossiers.json"))
                .name("dossierJsonItemReader")
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Dossier> dossierWriter() {
        return new JdbcBatchItemWriterBuilder<Dossier>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO dossier (nom_assure, numero_affiliation, total_remboursement) VALUES (:nomAssure, :numeroAffiliation, :montantTotalFrais)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Step processDossierStep() {
        return new StepBuilder("processDossierStep", jobRepository)
                .<Dossier, Dossier>chunk(10, transactionManager)
                .reader(dossierJsonItemReader())
                .processor(dossierProcessor)
                .writer(dossierWriter())
                .build();
    }

    @Bean
    public Job dossierJob() {
        return new JobBuilder("dossierJob", jobRepository)
                .start(processDossierStep())
                .build();
    }
}
