package com.ensa.projectspringbatch.Config;

import com.ensa.projectspringbatch.Exception.NonRecoverableException;
import com.ensa.projectspringbatch.Exception.RecoverableException;
import com.ensa.projectspringbatch.Model.Dossier;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RetryConfig retryConfig;

    @Autowired
    private SkipConfig skipConfig;

    @Bean
    public Job mutuelleJob(Step processStep) throws Exception {
        return new JobBuilder("mutuelleJob", jobRepository())
                .start(processStep)
                .build();
    }

    @Bean
    public Step processStep(PlatformTransactionManager transactionManager,
                            ItemReader<Dossier> reader,
                            @Qualifier("compositeItemProcessor") ItemProcessor<Dossier, Dossier> processor,
                            ItemWriter<Dossier> writer) throws Exception {
        return new StepBuilder("processStep", jobRepository())
                .<Dossier, Dossier>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .retry(RecoverableException.class) // Retry on recoverable exceptions
                .retryLimit(3) // Retry limit
                .skip(NonRecoverableException.class) // Skip non-recoverable exceptions
                .skipLimit(5) // Skip limit
                .listener(skipConfig.skipListener()) // Add the skip listener here
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "jobRepository")
    public JobRepository jobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager());
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean(name = "jobLauncher")
    public JobLauncher jobLauncher() throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository());
        return jobLauncher;
    }
}
