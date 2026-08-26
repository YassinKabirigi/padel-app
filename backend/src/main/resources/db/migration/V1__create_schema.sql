-- ============================================
-- V1__create_schema.sql
-- Schema initial : gestion de terrains de padel
-- ============================================

CREATE TABLE SITE (
                      id_site         INT IDENTITY(1,1) PRIMARY KEY,
                      nom             NVARCHAR(100)   NOT NULL,
                      adresse         NVARCHAR(255)   NOT NULL,
                      heure_ouverture TIME            NOT NULL,
                      heure_fermeture TIME            NOT NULL
);

CREATE TABLE TERRAIN (
                         id_terrain  INT IDENTITY(1,1) PRIMARY KEY,
                         numero      NVARCHAR(20)    NOT NULL,
                         id_site     INT             NOT NULL,
                         CONSTRAINT FK_terrain_site FOREIGN KEY (id_site) REFERENCES SITE(id_site)
);

CREATE TABLE JOUR_FERMETURE (
                                id_fermeture    INT IDENTITY(1,1) PRIMARY KEY,
                                date_fermeture  DATE            NOT NULL,
                                motif           NVARCHAR(255)   NULL,
                                id_site         INT             NULL,  -- NULL = fermeture globale
                                CONSTRAINT FK_fermeture_site FOREIGN KEY (id_site) REFERENCES SITE(id_site)
);

CREATE TABLE [MATCH] (
                         id_match            INT IDENTITY(1,1) PRIMARY KEY,
    date_heure_debut    DATETIME2       NOT NULL,
    statut              NVARCHAR(20)    NOT NULL
    CONSTRAINT CK_match_statut CHECK (statut IN ('PRIVE', 'PUBLIC', 'ANNULE', 'TERMINE')),
    id_terrain          INT             NOT NULL,
    CONSTRAINT FK_match_terrain FOREIGN KEY (id_terrain) REFERENCES TERRAIN(id_terrain)
    );

CREATE TABLE MEMBRE (
                        matricule           NVARCHAR(10)    PRIMARY KEY,
                        nom                 NVARCHAR(100)   NOT NULL,
                        prenom              NVARCHAR(100)   NOT NULL,
                        email               NVARCHAR(255)   NOT NULL UNIQUE,
                        telephone           NVARCHAR(20)    NULL,
                        date_inscription    DATE            NOT NULL,
                        date_debut_penalite DATE            NULL,
                        date_fin_penalite   DATE            NULL,
                        motif_penalite      NVARCHAR(255)   NULL,
                        mot_de_passe        NVARCHAR(255)   NULL,
                        type_membre         NVARCHAR(10)    NOT NULL
        CONSTRAINT CK_membre_type CHECK (type_membre IN ('GLOBAL', 'SITE', 'LIBRE')),
                        id_site             INT             NULL,  -- utilisé uniquement si type_membre = 'SITE'
                        CONSTRAINT FK_membre_site FOREIGN KEY (id_site) REFERENCES SITE(id_site),
                        CONSTRAINT CK_membre_matricule_prefixe CHECK (
                            (type_membre = 'GLOBAL' AND matricule LIKE 'G%') OR
                            (type_membre = 'SITE'   AND matricule LIKE 'S%') OR
                            (type_membre = 'LIBRE'  AND matricule LIKE 'L%')
                            )
);

CREATE TABLE PAIEMENT (
                          id_paiement     INT IDENTITY(1,1) PRIMARY KEY,
                          montant         DECIMAL(6,2)    NOT NULL CHECK (montant > 0),
                          date_paiement   DATETIME2       NOT NULL
);

CREATE TABLE PARTICIPATION (
                               id_participation    INT IDENTITY(1,1) PRIMARY KEY,
                               date_inscription    DATETIME2       NOT NULL,
                               est_organisateur    BIT             NOT NULL DEFAULT 0,
                               matricule           NVARCHAR(10)    NOT NULL,
                               id_match            INT             NOT NULL,
                               id_paiement         INT             NULL,
                               CONSTRAINT FK_participation_membre FOREIGN KEY (matricule) REFERENCES MEMBRE(matricule),
                               CONSTRAINT FK_participation_match FOREIGN KEY (id_match) REFERENCES [MATCH](id_match),
                               CONSTRAINT FK_participation_paiement FOREIGN KEY (id_paiement) REFERENCES PAIEMENT(id_paiement),
               CONSTRAINT UQ_participation_membre_match UNIQUE (matricule, id_match)
);

CREATE TABLE ADMINISTRATEUR (
                                id_admin        INT IDENTITY(1,1) PRIMARY KEY,
                                nom             NVARCHAR(100)   NOT NULL,
                                prenom          NVARCHAR(100)   NOT NULL,
                                email           NVARCHAR(255)   NOT NULL UNIQUE,
                                mot_de_passe    NVARCHAR(255)   NULL,
                                type_admin      NVARCHAR(10)    NOT NULL
        CONSTRAINT CK_admin_type CHECK (type_admin IN ('GLOBAL', 'SITE')),
                                id_site         INT             NULL,  -- utilisé uniquement si type_admin = 'SITE'
                                CONSTRAINT FK_admin_site FOREIGN KEY (id_site) REFERENCES SITE(id_site)
);
