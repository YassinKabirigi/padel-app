-- ============================================
-- V2__seed_data.sql
-- Jeu de donnees de test
-- ============================================

INSERT INTO SITE (nom, adresse, heure_ouverture, heure_fermeture)
VALUES ('Site Central', 'Rue du Padel 1, 1000 Bruxelles', '08:00', '22:00');

INSERT INTO TERRAIN (numero, id_site)
VALUES ('Terrain 1', 1), ('Terrain 2', 1);

INSERT INTO MEMBRE (matricule, nom, prenom, email, telephone, date_inscription, type_membre)
VALUES ('G0001', 'Dupont', 'Jean', 'jean.dupont@test.be', '0470000000', '2026-01-01', 'GLOBAL');