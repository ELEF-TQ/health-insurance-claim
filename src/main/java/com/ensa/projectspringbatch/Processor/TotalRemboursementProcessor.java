package com.ensa.projectspringbatch.Processor;

import com.ensa.projectspringbatch.Model.Dossier;
import com.ensa.projectspringbatch.Model.Traitement;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class TotalRemboursementProcessor implements ItemProcessor<Dossier, Dossier> {

    @Override
    public Dossier process(Dossier dossier) {
        double totalRemboursement = dossier.getMontantRemboursementConsultation();

        for (Traitement traitement : dossier.getTraitements()) {
            totalRemboursement += traitement.getMontantRemboursement();
        }
        dossier.setMontantRemboursement(totalRemboursement);
        return dossier;
    }
}

