package com.ensa.mutuelle.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Traitement {
    private String codeBarre;
    private boolean existe;
    private String nomMedicament;
    private String typeMedicament;
    private double prixMedicament;
    @Id
    private Long id;


}
