-- V3__create_dossier_archive_table.sql
CREATE TABLE IF NOT EXISTS dossier_archive (
       id SERIAL PRIMARY KEY,
       nom_assure VARCHAR(255) NOT NULL,
       numero_affiliation VARCHAR(255) NOT NULL,
       montant_remboursement DOUBLE PRECISION NOT NULL,
       date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
