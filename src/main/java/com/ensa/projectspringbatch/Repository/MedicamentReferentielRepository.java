package com.ensa.projectspringbatch.Repository;

import com.ensa.projectspringbatch.Model.MedicamentReferentiel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicamentReferentielRepository extends JpaRepository<MedicamentReferentiel, Long> {

    MedicamentReferentiel findByNomMedicament(String nomMedicament);
}

