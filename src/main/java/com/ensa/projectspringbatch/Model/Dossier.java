package com.ensa.projectspringbatch.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dossier {

    private String nomAssure;
    private String numeroAffiliation;
    private String immatriculation;
    private String lienParente;
    private double montantTotalFrais;
    private double prixConsultation;
    private int nombrePiecesJointes;
    private String nomBeneficiaire;
    private LocalDate dateDepotDossier;
    private List<Traitement> traitements;

    // Attributs pour le remboursement
    private double montantRemboursementConsultation;
    private double montantRemboursement;
}
