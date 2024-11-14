package com.ensa.mutuelle.writer;

import com.ensa.mutuelle.model.Dossier;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

public class DossierWriter {

    @Bean
    public JdbcBatchItemWriter<Dossier> dossierWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Dossier>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO dossier (nomAssure, numeroAffiliation, totalRemboursement) VALUES (:nomAssure, :numeroAffiliation, :totalRemboursement)")
                .dataSource(dataSource)
                .build();
    }
}