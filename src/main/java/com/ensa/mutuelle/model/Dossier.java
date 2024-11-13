package com.ensa.mutuelle.model;

import java.util.List;

public class Dossier {
    private String nomAssure;
    private String numeroAffiliation;
    private String immatriculation;
    private String lienParente;
    private double montantTotalFrais;
    private double prixConsultation;
    private int nombrePiecesJointes;
    private String nomBeneficiaire;
    private String dateDepotDossier;

   private List<Traitement> traitements;
}
