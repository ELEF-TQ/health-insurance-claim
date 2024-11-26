package com.ensa.projectspringbatch.Processor;

import com.ensa.projectspringbatch.Exception.ValidationException;
import com.ensa.projectspringbatch.Model.Dossier;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)

public class ValidationProcessor implements ItemProcessor<Dossier, Dossier> {

    @Override
    public Dossier process(Dossier dossier) {
        if (dossier.getNomAssure() == null || dossier.getNomAssure().isEmpty()) {
            throw new ValidationException("Le nom de l'assuré est manquant.");
        }
        if (dossier.getNumeroAffiliation() == null || dossier.getNumeroAffiliation().isEmpty()) {
            throw new ValidationException("Le numéro d'affiliation est manquant.");
        }
        if (dossier.getPrixConsultation() <= 0) {
            throw new ValidationException("Le prix de la consultation doit être positif.");
        }
        if (dossier.getTraitements() == null || dossier.getTraitements().isEmpty()) {
            throw new ValidationException("La liste des traitements est manquante ou vide.");
        }
        return dossier;
    }
}


