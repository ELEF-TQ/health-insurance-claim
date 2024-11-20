package com.ensa.projectspringbatch.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BatchJobService {

    @Autowired
    private JobLauncher jobLauncher; // Injecter le JobLauncher
    @Autowired
    private Job mutuelleJob; // Injecter le job

    // Méthode pour exécuter le job
    public String runJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            // Démarrer le job avec des paramètres (si nécessaire)
            JobExecution jobExecution = jobLauncher.run(mutuelleJob, jobParameters);
            return "Le job a démarré avec succès. Statut : " + jobExecution.getStatus();
        } catch (JobExecutionException e) {
            e.printStackTrace();
            return "Erreur lors du démarrage du job : " + e.getMessage();
        }
    }
}