CREATE TABLE medicament_referentiel (
    id SERIAL PRIMARY KEY,
    nom_medicament VARCHAR(255) NOT NULL,
    prix_reference NUMERIC(10, 2) NOT NULL,
    pourcentage_remboursement INT NOT NULL
);
