package com.ensa.mutuelle.config;

import com.ensa.mutuelle.model.Dossier;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;

@EnableBatchProcessing
@Configuration
public class BatchJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final ItemReader<Dossier> dossierItemReader;
    private final ItemWriter<Dossier> dossierItemWriter;

    public BatchJobConfig(JobRepository jobRepository,
                          PlatformTransactionManager transactionManager,
                          ItemReader<Dossier> dossierItemReader,
                          ItemWriter<Dossier> dossierItemWriter) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.dossierItemReader = dossierItemReader;
        this.dossierItemWriter = dossierItemWriter;
    }

    @Bean
    public Job dossierProcessingJob() {
        return new JobBuilder("dossierProcessingJob", jobRepository)
                .start(processDossierStep())
                .build();
    }

    @Bean
    public Step processDossierStep() {
        return new StepBuilder("processDossierStep", jobRepository)
                .<Dossier, Dossier>chunk(100, transactionManager)
                .reader(dossierItemReader)
                .processor(compositeItemProcessor())
                .writer(dossierItemWriter)
                .build();
    }

    @Bean
    public ItemProcessor<Dossier, Dossier> compositeItemProcessor() {
        CompositeItemProcessor<Dossier, Dossier> processor = new CompositeItemProcessor<>();
        processor.setDelegates(Arrays.asList(dossierProcessor1(), dossierProcessor2()));
        return processor;
    }

    @Bean
    public ItemProcessor<Dossier, Dossier> dossierProcessor1() {
        return new DossierValidationProcessor();
    }

    @Bean
    public ItemProcessor<Dossier, Dossier> dossierProcessor2() {
        return new DossierCalculationProcessor();
    }

    @Bean
    public FlatFileItemReader<Dossier> dossierFileItemReader(@Value("${input.file}") Resource inputFile) {
        FlatFileItemReader<Dossier> reader = new FlatFileItemReader<>();
        reader.setName("DossierItemReader");
        reader.setLinesToSkip(1);
        reader.setResource(inputFile);
        reader.setLineMapper(lineMapper());
        return reader;
    }

    @Bean
    public LineMapper<Dossier> lineMapper() {
        DefaultLineMapper<Dossier> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setNames("id", "nomAssure", "numeroAffiliation", "montantTotalFrais", "prixConsultation", "dateDepotDossier");
        lineMapper.setLineTokenizer(tokenizer);

        BeanWrapperFieldSetMapper<Dossier> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Dossier.class);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }
}
