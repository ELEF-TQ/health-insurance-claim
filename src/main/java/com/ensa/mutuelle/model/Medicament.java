package com.ensa.mutuelle.model;


import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;

@Entity
@Getter
@Setter
public class Medicament {
    private String nom;
    private double prixReference;
    private double tauxRemboursement;
}
