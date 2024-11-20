package com.ensa.projectspringbatch.Writer;

import com.ensa.projectspringbatch.Model.Dossier;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
public class DossierWriter {

    private final DataSource dataSource;

    @Bean
    public JdbcBatchItemWriter<Dossier> databaseItemWriter() {
        return new JdbcBatchItemWriterBuilder<Dossier>()
                .dataSource(dataSource)
                .sql("INSERT INTO dossier_archive (nom_assure, numero_affiliation, montant_remboursement) " +
                        "VALUES (:nomAssure, :numeroAffiliation, :montantRemboursement)")
                .beanMapped()
                .build();
    }
}
