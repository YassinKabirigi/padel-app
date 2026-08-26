-- ============================================
-- V5__add_mot_de_passe.sql
-- Ajout du champ mot_de_passe (BCrypt) pour Membre et Administrateur
-- ============================================

ALTER TABLE MEMBRE ADD mot_de_passe NVARCHAR(255) NULL;
ALTER TABLE ADMINISTRATEUR ADD mot_de_passe NVARCHAR(255) NULL;

-- Hash BCrypt de 'padel2026'
DECLARE @hashMembre NVARCHAR(255) = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy';
-- Hash BCrypt de 'admin2026'
DECLARE @hashAdmin  NVARCHAR(255) = '$2a$10$TKh8H1.PfbZwFos3yEGM7OBUexqKTbXmH7xyXB9FlNdQhJKWKM2YK';

UPDATE MEMBRE      SET mot_de_passe = @hashMembre WHERE matricule IN ('G1042','G1043','G1044','S2201','S2202','S2401','L3301','L3302','L3303');
UPDATE ADMINISTRATEUR SET mot_de_passe = @hashAdmin  WHERE id_admin IN (1,2,3);
