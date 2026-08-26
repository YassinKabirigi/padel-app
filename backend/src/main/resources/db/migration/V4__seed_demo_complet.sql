-- ============================================
-- V4__seed_demo_complet.sql
-- Fermetures, penalite active et paiement effectue
-- pour demontrer toutes les regles metier
-- ============================================

-- Fermeture globale (jour ferie, tous les sites)
INSERT INTO JOUR_FERMETURE (date_fermeture, motif, id_site)
VALUES (DATEADD(DAY, 45, GETDATE()), 'Jour ferie national', NULL);

-- Fermeture specifique a un site (maintenance a Namur)
INSERT INTO JOUR_FERMETURE (date_fermeture, motif, id_site)
VALUES (DATEADD(DAY, 20, GETDATE()), 'Maintenance des installations', 3);

-- Penalite active sur un membre (match prive non complete avant la veille)
UPDATE MEMBRE
SET date_debut_penalite = GETDATE(),
    date_fin_penalite = DATEADD(DAY, 7, GETDATE()),
    motif_penalite = 'Match prive non complete avant la veille'
WHERE matricule = 'S2202';

-- Paiement effectue sur une participation existante
INSERT INTO PAIEMENT (montant, date_paiement)
VALUES (15.00, GETDATE());

UPDATE PARTICIPATION
SET id_paiement = (SELECT TOP 1 id_paiement FROM PAIEMENT ORDER BY id_paiement DESC)
WHERE id_participation = (
    SELECT TOP 1 id_participation FROM PARTICIPATION
    WHERE matricule = 'S2201' AND id_match = 3
);