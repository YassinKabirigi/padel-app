-- ============================================
-- V3__seed_admin_et_donnees_completes.sql
-- Administrateur + donnees representatives supplementaires
-- ============================================

-- Administrateur global (identifiant de connexion : ADMIN-1)
INSERT INTO ADMINISTRATEUR (nom, prenom, email, type_admin)
VALUES ('Lambert', 'Sophie', 'sophie.lambert@padel.be', 'GLOBAL');

-- Membres supplementaires (un de chaque type, en plus de G0001 existant)
INSERT INTO MEMBRE (matricule, nom, prenom, email, telephone, date_inscription, type_membre, id_site)
VALUES
    ('S0001', 'Martin', 'Lucie', 'lucie.martin@test.be', '0471111111', '2026-02-01', 'SITE', 1),
    ('L0001', 'Durand', 'Marc', 'marc.durand@test.be', '0472222222', '2026-03-01', 'LIBRE', NULL),
    ('G0002', 'Petit', 'Julie', 'julie.petit@test.be', '0473333333', '2026-01-15', 'GLOBAL', NULL);

-- Quelques matches existants (pour demontrer la logique metier a l'oral)
INSERT INTO MATCH (date_heure_debut, statut, id_terrain)
VALUES
    (DATEADD(DAY, 30, GETDATE()), 'PRIVE', 1),
    (DATEADD(DAY, 35, GETDATE()), 'PUBLIC', 2);

-- Participations sur ces matches
INSERT INTO PARTICIPATION (date_inscription, est_organisateur, matricule, id_match)
VALUES
    (GETDATE(), 1, 'G0001', 1),
    (GETDATE(), 0, 'S0001', 1),
    (GETDATE(), 1, 'L0001', 2);