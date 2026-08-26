-- ============================================
-- V3__seed_admin_et_donnees_completes.sql
-- Sites supplementaires, administrateurs, membres varies, matchs de demonstration
-- ============================================

-- Site 2 : Waterloo
INSERT INTO SITE (nom, adresse, heure_ouverture, heure_fermeture)
VALUES ('Padel Arena Waterloo', 'Chaussee de Bruxelles 210, 1410 Waterloo', '09:00', '21:30');

INSERT INTO TERRAIN (numero, id_site)
VALUES ('Smash', 2), ('Bandeja', 2), ('Vibora', 2);

-- Site 3 : Namur
INSERT INTO SITE (nom, adresse, heure_ouverture, heure_fermeture)
VALUES ('Padel Time Namur', 'Rue de Fer 88, 5000 Namur', '08:30', '22:30');

INSERT INTO TERRAIN (numero, id_site)
VALUES ('Court Prestige', 3), ('Court Elite', 3);

-- Site 4 : Louvain-la-Neuve
INSERT INTO SITE (nom, adresse, heure_ouverture, heure_fermeture)
VALUES ('Smash Padel Louvain-la-Neuve', 'Grand-Place 4, 1348 Louvain-la-Neuve', '07:30', '23:00');

INSERT INTO TERRAIN (numero, id_site)
VALUES ('Golden', 4), ('Masters', 4), ('Champions', 4);

-- Administrateur global (identifiant de connexion : ADMIN-1)
INSERT INTO ADMINISTRATEUR (nom, prenom, email, type_admin)
VALUES ('Lambert', 'Sophie', 'sophie.lambert@padelclub.be', 'GLOBAL');

-- Administrateurs de site (identifiants de connexion : ADMIN-2, ADMIN-3)
INSERT INTO ADMINISTRATEUR (nom, prenom, email, type_admin, id_site)
VALUES
    ('Moreau', 'Nicolas', 'nicolas.moreau@padelclub.be', 'SITE', 2),
    ('Fontaine', 'Elise', 'elise.fontaine@padelclub.be', 'SITE', 4);

-- Membres varies (types Global / Site / Libre, repartis sur plusieurs sites)
INSERT INTO MEMBRE (matricule, nom, prenom, email, telephone, date_inscription, type_membre, id_site)
VALUES
    ('S2201', 'Lefevre', 'Camille', 'camille.lefevre@outlook.com', '0478 22 33 44', '2025-11-03', 'SITE', 2),
    ('S2202', 'Bernard', 'Antoine', 'antoine.bernard@hotmail.com', '0479 44 55 66', '2025-12-18', 'SITE', 2),
    ('S2401', 'Gerard', 'Chloe', 'chloe.gerard@gmail.com', '0470 55 66 77', '2026-01-22', 'SITE', 4),
    ('L3301', 'Dubois', 'Laura', 'laura.dubois@gmail.com', '0472 66 77 88', '2026-02-10', 'LIBRE', NULL),
    ('L3302', 'Simon', 'Julien', 'julien.simon@gmail.com', '0473 88 99 00', '2026-03-05', 'LIBRE', NULL),
    ('L3303', 'Renard', 'Nathan', 'nathan.renard@outlook.com', '0471 99 00 11', '2026-01-30', 'LIBRE', NULL),
    ('G1043', 'Rousseau', 'Marie', 'marie.rousseau@gmail.com', '0474 11 22 33', '2025-10-20', 'GLOBAL', NULL),
    ('G1044', 'Petit', 'Alexandre', 'alexandre.petit@yahoo.fr', '0475 33 44 55', '2026-01-08', 'GLOBAL', NULL);

-- Matchs de demonstration (repartis sur les quatre sites, statuts varies)
INSERT INTO [MATCH] (date_heure_debut, statut, id_terrain)
VALUES
    (DATEADD(DAY, 25, GETDATE()), 'PRIVE', 1),
    (DATEADD(DAY, 27, GETDATE()), 'PUBLIC', 2),
    (DATEADD(DAY, 30, GETDATE()), 'PRIVE', 5),
    (DATEADD(DAY, 32, GETDATE()), 'PUBLIC', 6),
    (DATEADD(DAY, 35, GETDATE()), 'PRIVE', 8),
    (DATEADD(DAY, 38, GETDATE()), 'PUBLIC', 10),
    (DATEADD(DAY, 40, GETDATE()), 'PRIVE', 11);

-- Participations correspondantes
INSERT INTO PARTICIPATION (date_inscription, est_organisateur, matricule, id_match)
VALUES
    (GETDATE(), 1, 'G1042', 1),
    (GETDATE(), 0, 'G1043', 1),
    (GETDATE(), 0, 'L3301', 1),
    (GETDATE(), 0, 'G1044', 1),
    (GETDATE(), 1, 'G1043', 2),
    (GETDATE(), 1, 'S2201', 3),
    (GETDATE(), 0, 'S2202', 3),
    (GETDATE(), 1, 'L3302', 4),
    (GETDATE(), 1, 'G1044', 5),
    (GETDATE(), 0, 'L3303', 6),
    (GETDATE(), 1, 'S2401', 7),
    (GETDATE(), 0, 'G1043', 7);