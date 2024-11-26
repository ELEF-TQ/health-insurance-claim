package com.ensa.projectspringbatch.Processor;

import com.ensa.projectspringbatch.Model.Dossier;
import com.ensa.projectspringbatch.Model.MedicamentReferentiel;
import com.ensa.projectspringbatch.Model.Traitement;
import com.ensa.projectspringbatch.Repository.MedicamentReferentielRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
public class CalculProcessor implements ItemProcessor<Dossier, Dossier> {

    private final MedicamentReferentielRepository medicamentRepository;

    public CalculProcessor(MedicamentReferentielRepository medicamentRepository) {
        this.medicamentRepository = medicamentRepository;
    }

    @Override
    public Dossier process(Dossier dossier) throws Exception {

        // Step 1: Consultation
        double remboursementConsultation = dossier.getPrixConsultation() * 0.7;
        dossier.setMontantRemboursementConsultation(remboursementConsultation);

        // Step 2: Map traitement
        for (Traitement traitement : dossier.getTraitements()) {
            MedicamentReferentiel referentiel = medicamentRepository.findByNomMedicament(traitement.getNomMedicament());
            if (referentiel != null) {
                traitement.setPrixMedicament(referentiel.getPrixReference());
                traitement.setPourcentageRemboursement(referentiel.getPourcentageRemboursement());
                traitement.setExiste(true);

                double remboursementTraitement = traitement.getPrixMedicament() * (traitement.getPourcentageRemboursement() / 100);
                traitement.setMontantRemboursement(remboursementTraitement);
            } else {
                traitement.setExiste(false);
                traitement.setMontantRemboursement(0.0);
            }
        }

        // Step 3:  Total
        double totalRemboursement = dossier.getMontantRemboursementConsultation();
        for (Traitement traitement : dossier.getTraitements()) {
            totalRemboursement += traitement.getMontantRemboursement();
        }
        dossier.setMontantRemboursement(totalRemboursement);

        return dossier;
    }


    //Skip simulation
//     if (Math.random() < 0.5) {
//        throw new Exception("Simulated failure in CalculProcessor");
//    }

    //Retry simulation
//      if (dossier.getTraitements().isEmpty()) {
//        throw new IllegalStateException("No treatments found for dossier, skipping.");
//    }

}
