-- ============================================
-- init.sql
-- Initialisation de la base padel et création
-- de l'utilisateur applicatif dédié (padel_app)
-- ============================================

-- Création de la base si elle n'existe pas encore
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'padel')
    CREATE DATABASE padel;
GO

USE padel;
GO

-- Création du login SQL Server (niveau serveur)
IF NOT EXISTS (SELECT name FROM sys.server_principals WHERE name = 'padel_app')
    CREATE LOGIN padel_app WITH PASSWORD = 'PadelApp2026!';
GO

-- Création du user dans la base padel (niveau base)
IF NOT EXISTS (SELECT name FROM sys.database_principals WHERE name = 'padel_app')
BEGIN
    CREATE USER padel_app FOR LOGIN padel_app;

    -- db_datareader  : SELECT sur toutes les tables de la base
    ALTER ROLE db_datareader  ADD MEMBER padel_app;

    -- db_datawriter  : INSERT / UPDATE / DELETE sur toutes les tables
    ALTER ROLE db_datawriter  ADD MEMBER padel_app;

    -- db_ddladmin    : CREATE TABLE / ALTER (nécessaire pour Flyway)
    ALTER ROLE db_ddladmin    ADD MEMBER padel_app;

    -- DROITS NON ACCORDÉS (principe du moindre privilège) :
    --   sysadmin, serveradmin, securityadmin, dbcreator,
    --   processadmin, setupadmin, bulkadmin, diskadmin
    -- padel_app est limité à la base 'padel' uniquement.
END
GO
