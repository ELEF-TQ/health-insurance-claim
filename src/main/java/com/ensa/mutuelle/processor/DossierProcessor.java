package com.ensa.mutuelle.processor;

import com.ensa.mutuelle.model.Dossier;
import com.ensa.mutuelle.model.Medicament;
import com.ensa.mutuelle.model.Traitement;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DossierProcessor implements ItemProcessor<Dossier, Dossier> {

    private static final double CONSULTATION_REIMBURSEMENT_RATE = 0.8;
    private final List<Medicament> medicamentReferentiel; // List of reference medications

    public DossierProcessor(List<Medicament> medicamentReferentiel) {
        this.medicamentReferentiel = medicamentReferentiel;
    }

    @Override
    public Dossier process(Dossier dossier) throws Exception {
        // Validation
        validateDossier(dossier);

        // Calculate consultation reimbursement
        double consultationReimbursement = calculateConsultationReimbursement(dossier.getPrixConsultation());

        // Process treatments and calculate total reimbursement for treatments
        double totalTreatmentReimbursement = dossier.getTraitements().stream()
                .mapToDouble(this::calculateTreatmentReimbursement)
                .sum();

        // Calculate the total reimbursement (consultation + treatments)
        dossier.setMontantTotalFrais(consultationReimbursement + totalTreatmentReimbursement);

        return dossier;
    }

    private void validateDossier(Dossier dossier) {
        if (dossier.getNomAssure() == null || dossier.getNumeroAffiliation() == null) {
            throw new IllegalArgumentException("Nom de l'assuré ou numéro d'affiliation manquant");
        }
        if (dossier.getPrixConsultation() <= 0 || dossier.getMontantTotalFrais() <= 0) {
            throw new IllegalArgumentException("Le prix de la consultation et le montant total des frais doivent être positifs");
        }
        if (dossier.getTraitements() == null || dossier.getTraitements().isEmpty()) {
            throw new IllegalArgumentException("La liste des traitements ne doit pas être vide");
        }
    }

    private double calculateConsultationReimbursement(double consultationPrice) {
        return consultationPrice * CONSULTATION_REIMBURSEMENT_RATE;
    }

    private double calculateTreatmentReimbursement(Traitement traitement) {
        // Find the matching medication in the referential list
        Optional<Medicament> referentialMedication = medicamentReferentiel.stream()
                .filter(med -> med.getNom().equals(traitement.getNomMedicament()))
                .findFirst();

        if (referentialMedication.isPresent()) {
            Medicament medicament = referentialMedication.get();
            return traitement.getPrixMedicament() * medicament.getTauxRemboursement();
        } else {
            // No matching medication found, reimbursement set to zero for that treatment
            return 0;
        }
    }
}
