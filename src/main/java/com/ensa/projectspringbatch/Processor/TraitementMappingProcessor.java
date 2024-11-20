package com.ensa.projectspringbatch.Processor;

import com.ensa.projectspringbatch.Model.Dossier;
import com.ensa.projectspringbatch.Model.MedicamentReferentiel;
import com.ensa.projectspringbatch.Model.Traitement;
import com.ensa.projectspringbatch.Repository.MedicamentReferentielRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TraitementMappingProcessor implements ItemProcessor<Dossier, Dossier> {

    private final MedicamentReferentielRepository medicamentRepository;

    @Override
    public Dossier process(Dossier dossier) {
        for (Traitement traitement : dossier.getTraitements()) {
            MedicamentReferentiel referentiel = medicamentRepository.findByNomMedicament(traitement.getNomMedicament());
            if (referentiel != null) {
                traitement.setPrixMedicament(referentiel.getPrixReference());
                traitement.setPourcentageRemboursement(referentiel.getPourcentageRemboursement());
                traitement.setExiste(true);
            } else {
                traitement.setExiste(false);
            }
        }
        return dossier;
    }
}

