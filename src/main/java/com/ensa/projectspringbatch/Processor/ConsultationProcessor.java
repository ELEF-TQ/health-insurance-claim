package com.ensa.projectspringbatch.Processor;

import com.ensa.projectspringbatch.Model.Dossier;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ConsultationProcessor implements ItemProcessor<Dossier, Dossier> {
    private static final double POURCENTAGE_REMBOURSEMENT_CONSULTATION = 0.7;
    @Override
    public Dossier process(Dossier dossier) {
        double remboursementConsultation = dossier.getPrixConsultation() * POURCENTAGE_REMBOURSEMENT_CONSULTATION;
        dossier.setMontantRemboursementConsultation(remboursementConsultation);
        return dossier;
    }
}

