package com.ensa.projectspringbatch.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MedicamentReferentiel {
    @Id
    private Long id;
    private String nomMedicament;
    private double prixReference;
    private double pourcentageRemboursement;
}

