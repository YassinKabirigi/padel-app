-- ============================================
-- V2__seed_data.sql
-- Site principal et premier membre
-- ============================================

INSERT INTO SITE (nom, adresse, heure_ouverture, heure_fermeture)
VALUES ('Padel Club Woluwe', 'Avenue de Roodebeek 45, 1200 Bruxelles', '08:00', '22:00');

INSERT INTO TERRAIN (numero, id_site)
VALUES ('Smash', 1), ('Bandeja', 1), ('Vibora', 1), ('Chiquita', 1);

INSERT INTO MEMBRE (matricule, nom, prenom, email, telephone, date_inscription, type_membre)
VALUES ('G1042', 'Vandenberghe', 'Thomas', 'thomas.vandenberghe@gmail.com', '0475 12 34 56', '2025-09-12', 'GLOBAL');