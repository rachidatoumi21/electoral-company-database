-- CREATE DATABASE CampagneElectorale;
use CampagneElectorale;
GO

-- DROP TABLES in the right order
DROP TABLE IF EXISTS LoiModifie;
DROP TABLE IF EXISTS VoyageParCandidat;
DROP TABLE IF EXISTS Frais;
DROP TABLE IF EXISTS Visites;
DROP TABLE IF EXISTS ProjetDeLoi;
DROP TABLE IF EXISTS ActionDecision;
DROP TABLE IF EXISTS Voyage;
DROP TABLE IF EXISTS Loi;
DROP TABLE IF EXISTS Theme;
DROP TABLE IF EXISTS Campagne;
DROP TABLE IF EXISTS Village;
DROP TABLE IF EXISTS Candidat;

-- Création de la table Candidat 

CREATE TABLE Candidat (
    CandidatID INT NOT NULL IDENTITY PRIMARY KEY,
    Prenom VARCHAR(255),
    Nom VARCHAR(255),
    Date_naissance DATE,
    Parti_Politique VARCHAR(255)
);
GO

-- Creation de la table Campagne
CREATE TABLE Campagne (
	CampagneID INT PRIMARY KEY,
	CandidatID INT,
	CONSTRAINT fk_Compagne_Candidat FOREIGN KEY(CandidatID) REFERENCES Candidat(CandidatID)
);
GO

-- Creation de la table Village
CREATE TABLE Village (
    VillageID VARCHAR(20) NOT NULL PRIMARY KEY,
    Nom VARCHAR(255),
    Region VARCHAR(255)
);
GO

-- Creation de la table Thème
CREATE TABLE Theme (
    ThemeID INT NOT NULL PRIMARY KEY,
    Nom VARCHAR(255),
    Sujet VARCHAR(255),
    VillageID VARCHAR(20),

    CONSTRAINT fk_theme_village FOREIGN KEY ( VillageID) REFERENCES Village(VillageID)
);
GO

-- Creation de la table ActionDecision
CREATE TABLE ActionDecision (
    ActionID INT NOT NULL PRIMARY KEY,
    Description TEXT,
    Date DATE,
    CandidatID INT,
    
     CONSTRAINT fk_action_candidat FOREIGN KEY (CandidatID) REFERENCES Candidat(CandidatID),
);
GO

-- Creation de la table Visites
CREATE TABLE Visites (
	CandidatID INT NOT NULL,
	VillageID VARCHAR(20) NOT NULL,
	Date_visite DATE,
	CONSTRAINT fk_Visite_Candidat FOREIGN KEY(CandidatID) REFERENCES Candidat(CandidatID),
	CONSTRAINT fk_Visite_Village FOREIGN KEY(VillageID) REFERENCES Village(VillageID)
);
GO

-- Creation de la table Loi
CREATE TABLE Loi (
	Code_loi INT PRIMARY KEY,
	Titre VARCHAR(255),
	Contenu VARCHAR(255),
	Date_adoption DATE
);
GO

-- Creation de la table ProjetDeLoi
CREATE TABLE ProjetDeLoi (
    Code_projet INT NOT NULL PRIMARY KEY,
	CampagneID INT,
	Titre VARCHAR(255),
	Contenu VARCHAR(255),
	Date_propos DATE,
	ImpactFinancier DECIMAL(10,2),

	CONSTRAINT fk_Projet_Campagne FOREIGN KEY(CampagneID) REFERENCES Campagne(CampagneID)
);
GO

-- Creation de la table LoiModifie
CREATE TABLE LoiModifie (
	Code_projet INT NOT NULL,
	Code_loi INT NOT NULL
	CONSTRAINT fk_LoiModifie_Projet FOREIGN KEY(Code_projet) REFERENCES ProjetDeLoi(Code_projet),
	CONSTRAINT fk_LoiModifie_Loi FOREIGN KEY(Code_loi) REFERENCES Loi(Code_loi)
);
GO

-- Creation de la table Voyage
CREATE TABLE Voyage (
    VoyageID INT NOT NULL PRIMARY KEY,
	Destination VARCHAR(255),
	Date_voyage DATE
    
);
GO

-- Creation de la table VoyageParCandidat
CREATE TABLE VoyageParCandidat (
	CandidatID INT NOT NULL,
	VoyageID INT NOT NULL,
	CONSTRAINT fk_VoyageParCandidat_Candidat FOREIGN KEY(CandidatID) REFERENCES Candidat(CandidatID),
	CONSTRAINT fk_VoyageParCandidat_Voyage FOREIGN KEY(VoyageID) REFERENCES Voyage(VoyageID)
);
GO

-- Creation de la table Frais
CREATE TABLE Frais (
    FraisID INT NOT NULL PRIMARY KEY,
    Type_frais VARCHAR(255),
    Montant DECIMAL(10,2),
    VoyageID INT,
    
    CONSTRAINT fk_frais_voyage FOREIGN KEY (VoyageID ) REFERENCES Voyage(VoyageID)
);

GO
-- Inserer des valeur dans les tables:

INSERT INTO Candidat (prenom, Nom,  Date_naissance, Parti_Politique)
	VALUES ('Rabah', 'Louhichi', '1971-09-16','parti libéral'),
            ('Simon', 'Deronzo' , '1982-11-25',' Bloc québecois'),
            ('Elize', 'Jozline', '1990-05-30', 'parti conservateur'),
            ('Marie', 'Clode', '1985-07-19', ' Parti vert'),
            ('Alex', 'philipe', '1979-02-11', 'parti libéral'),
            ('Noor', 'Elyakine', '1988-08-23','Parti vert'),
            ('Moncef','Toumi', '1983-01-29', ' Nouveau parti démocratique'),
            ('Sébastien', 'Major', '1973-12-05', 'parti conservateur'),
            ('Sophie', 'Nadine', '1975-04-17', ' Bloc québecois'),
            ('James', 'Mark', '1981-09-16', 'parti libéral'),
            ('Paul', 'Clark', '1977-03-22', 'Parti vert'),
            ('Susanne', 'Nadia', '1969-06-13', 'Nouveau parti démocratique');

INSERT INTO Village (VillageID, Nom, Region)
    VALUES ('BSC', 'Baie-St-Paul', 'Charlevoix'),
            ('TEQ','Terrebonne', 'Qeubec'),
            ('LAL', 'lac-Des-Écorces', 'Laurentides'),
            ('MJG','Mont-Joli', 'Gaspésie'),
            ('PEO','Pont Edward', 'Ontario'),
            ('CON', 'Casselman', 'Ontario'),
            ('SJD','Saint-Jeanne-darc', 'Quebec'),
            ('BTQ','Baie-Trinité', 'Quebec');

INSERT INTO Campagne (CampagneID, CandidatID)
VALUES 
    (1, 1),
    (2, 1),
    (3, 2),
    (4, 4),
    (5, 3),
    (6, 5),
    (7, 7);            

INSERT INTO Theme (ThemeID, Nom, Sujet, VillageID)
VALUES 
    (1, 'Économie', 'Développement durable', 'BSC'),
    (2, 'Éducation', 'Réforme scolaire', 'TEQ'),
    (3, 'Santé', 'Accès aux soins', 'LAL'),
    (4, 'Emploi', 'Création emplois', 'MJG'),
    (5, 'Environnement', 'Protection des espaces verts', 'PEO'),
    (6, 'Technologie', 'Développement numérique', 'CON'),
    (7, 'Culture', 'Subventions culturelles', 'SJD');

INSERT INTO ActionDecision (ActionID, Description, Date, CandidatID)
VALUES 
    (1, 'Débat sur énergie verte', '2024-04-01', 1),
    (2, 'Rencontre avec les industriels', '2024-04-02', 1),
    (3, 'Visite des écoles', '2024-04-03', 3),
    (4, 'Campagne de santé publique', '2024-04-04', 2),
    (5, 'Conférence sur emplois', '2024-04-05', 5),
    (6, 'Exposition technologique', '2024-04-06', 6),
    (7, 'Festival de musique', '2024-04-07', 7);


INSERT INTO Visites (CandidatID, VillageID, Date_visite)
VALUES 
    (1, 'BSC', '2024-04-08'),
    (2, 'TEQ', '2024-04-09'),
    (1, 'LAL', '2024-04-10'),
    (3, 'MJG', '2024-04-11'),
    (5, 'PEO', '2024-04-12'),
    (4, 'CON', '2024-04-13'),
    (2, 'SJD', '2024-04-14'); 

INSERT INTO Loi (Code_loi, Titre, Contenu, Date_adoption)
VALUES 
    (101, 'Loi sur énergie verte', 'Subventions pour les énergies renouvelables', '2024-04-15'),
    (102, 'Loi sur éducation', 'Nouveaux programmes scolaires', '2024-04-16'),
    (103, 'Loi sur la santé publique', 'Obligation vaccinale', '2024-04-17'),
    (104, 'Loi sur emploi', 'Incitations pour les start-ups', '2024-04-18'),
    (105, 'Loi environnementale', 'Interdiction des plastiques à usage unique', '2024-04-19'),
    (106, 'Loi sur innovation', 'Crédits impôt pour la R&D', '2024-04-20'),
    (107, 'Loi sur la culture', 'Augmentation du budget de la culture', '2024-04-21');

INSERT INTO ProjetDeLoi (Code_projet, CampagneID, Titre, Contenu, Date_propos, ImpactFinancier)
VALUES 
    (201, 1, 'Réforme fiscale', 'Réduction dimpôts pour les classes moyennes', '2024-04-22', 3000000.00),
    (202, 2, 'Loi travail', 'Loi sur les 35 heures', '2024-04-23', 2000000.00),
    (203, 3, 'Loi logement', 'Aide aux accès au logement pour les jeunes', '2024-04-24',1000000.00),       
    (204, 3, 'Loi logement', 'Aide aux accès au logement pour les jeunes', '2024-04-24', 2500000.00),
    (205, 4, 'Loi de sécurité', 'Renforcement des forces de lordre', '2024-04-25', 1500000.00),
    (206, 5, 'Loi sur le climat', 'Plan pour la neutralité carbone', '2024-04-26', 5000000.00),
    (207, 6, 'Loi sur légalité', 'Mesures pour légalité des chances', '2024-04-27', 800000.00),
    (208, 7, 'Loi sur lagriculture', 'Subventions aux agriculteurs biologiques', '2024-04-28', 1200000.00);

INSERT INTO LoiModifie (Code_projet, Code_loi)
VALUES 
    (201, 101),
    (202, 102),
    (203, 103),
    (204, 104),
    (205, 105),
    (206, 106),
    (207, 107);


INSERT INTO Voyage (VoyageID, Destination, Date_voyage)
VALUES 
    (1, 'Montréal', '2024-04-29'),
    (2, 'Québec', '2024-04-30'),
    (3, 'Ottawa', '2024-05-01'),
    (4, 'Toronto', '2024-05-02'),
    (5, 'Vancouver', '2024-05-03'),
    (6, 'Calgary', '2024-05-04'),
    (7, 'Halifax', '2024-05-05');

INSERT INTO VoyageParCandidat (CandidatID, VoyageID)
VALUES 
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 4),
    (6, 6),
    (7, 7);

INSERT INTO Frais (FraisID, Type_frais, Montant, VoyageID)
VALUES 
    (1, 'Transport', 300.00, 1),
    (2, 'Hébergement', 500.00, 1),
    (3, 'Repas', 100.00, 2),
    (4, 'Transport', 400.00, 3),
    (5, 'Hébergement', 600.00, 3),
    (6, 'Repas', 200.00, 1),
    (7, 'Transport', 700.00, 2);      

-- Affichages
--SELECT * FROM Candidat;
--SELECT * FROM Campagne;
--SELECT * FROM Village;
--SELECT * FROM Theme;
--SELECT * FROM Visites;
--SELECT * FROM Loi;
--SELECT * FROM ProjetDeLoi;
--SELECT * FROM LoiModifie ;
--SELECT * FROM ActionDecision;
--SELECT * FROM Voyage;
--SELECT * FROM VoyageParCandidat; 
--SELECT * FROM Frais;

---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
--Mises a jours des données de la BD avec procédures

-- 1) Mise a jours de la table ActionDecision :
ALTER TABLE ActionDecision ADD 
    LastUpdated DATETIME NULL; -- Ajouter le champ LastUpdated à la table ActionDecision
GO

-- Création d'une procédure stockée pour mettre à jour les actions-décisions
CREATE OR ALTER PROCEDURE UpdateAction
    @ActionID INT,
    @Description TEXT,
    @Date DATE,
    @CandidatID INT
AS
BEGIN
    UPDATE ActionDecision
    SET Description = @Description,
        Date = @Date,
        CandidatID = @CandidatID,
        LastUpdated = GETDATE() -- mise à jour automatique de la date de dernière mise à jour
    WHERE ActionID = @ActionID;
END;
GO

-- Appel de la procédure stockée pour mettre à jour une action
EXEC UpdateAction @ActionID = 1, 
                  @Description = 'Nouvelle description de laction', 
                  @Date = '2024-05-01', 
                  @CandidatID = 1;
GO

-- 2)  Mise a jours de la table theme :
CREATE OR ALTER PROCEDURE UpdateTheme
    @ThemeID INT,
    @Nom VARCHAR(255),
    @Sujet VARCHAR(255)
AS
BEGIN
    UPDATE Theme
    SET Nom = @Nom,
        Sujet = @Sujet
    WHERE ThemeID = @ThemeID;
END;
GO

-- Appel de la procédure stockée pour mettre à jour un theme
EXEC UpdateTheme @ThemeID = 1, 
                 @Nom = 'Économie locale', 
                 @Sujet = 'Stratégies pour le développement économique local';
GO

-- 3)  Mise a jours de la table ProjetDeLoi :   
CREATE OR  ALTER PROCEDURE UpdateProjetDeLoi
    @Code_projet INT,
    @CampagneID INT,
    @Titre VARCHAR(255),
    @Contenu VARCHAR(255),
    @Date_propos DATE,
    @ImpactFinancier DECIMAL(10,2)
AS
BEGIN
    UPDATE ProjetDeLoi
    SET CampagneID = @CampagneID,
        Titre = @Titre,
        Contenu = @Contenu,
        Date_propos = @Date_propos,
        ImpactFinancier = @ImpactFinancier
    WHERE Code_projet = @Code_projet;
END;              
GO

-- Appel de la procédure stockée pour mettre à jour
EXEC UpdateProjetDeLoi @Code_projet = 201, 
                       @CampagneID = 1,
                       @Titre = 'Réforme fiscale pour la croissance', 
                       @Contenu = 'Proposition de loi pour réduire les impôts des PME', 
                       @Date_propos = '2024-05-15', 
                       @ImpactFinancier = 2500000.00;

GO
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
--Mises a jours des données de la BD avec Triggers

-- 1) Mises a jours de la table Voyage :
ALTER TABLE Voyage ADD 
DerniereMiseAjour DATETIME NULL;
GO

CREATE TRIGGER trg_AfterUpdateVoyage
ON Voyage
AFTER UPDATE
AS
BEGIN
    IF UPDATE(Destination) OR UPDATE(Date_voyage)-- mis à jour lorsque les champs Destination ou Date_voyage sont modifiés
    BEGIN
        UPDATE Voyage
        SET DerniereMiseAjour = GETDATE()
        FROM Voyage v
        INNER JOIN Inserted i ON v.VoyageID = i.VoyageID;
    END
END;
GO

--Tester
UPDATE Voyage
SET Destination = 'Nouvelle destination',
    Date_voyage = '2024-10-12',
    DerniereMiseAjour = GETDATE()
WHERE VoyageID = 1;
GO

-- 2) Mises a jours de la table Frais :
ALTER TABLE Frais ADD
DernierMiseAjour DATETIME NULL;
GO 

CREATE TRIGGER trg_AfterUpdateFrais
ON Frais
AFTER UPDATE
AS
BEGIN
    UPDATE Frais
    SET DernierMiseAjour = GETDATE()
    FROM Frais f
    INNER JOIN Inserted i ON f.FraisID = i.FraisID;
END;
GO

--tester
UPDATE Frais
SET Montant = 450.00,
    Type_frais = 'repas'
WHERE FraisID = 2;
GO 


-- 3) mise a jour que le champ Parti_Politique de la table Candidat :
CREATE TRIGGER trg_UpdatePartiPolitiqueOnly
ON Candidat
INSTEAD OF UPDATE
AS
BEGIN
    -- Vérifiez si la mise à jour concerne d'autres champs que Parti_Politique
    IF UPDATE(Prenom) OR UPDATE(Nom) OR UPDATE(Date_naissance)
    BEGIN
        RAISERROR ('La mise à jour des champs autres que Parti_Politique est interdite.', 16, 1);
        ROLLBACK TRANSACTION;
        RETURN;
    END

    -- Mettez à jour seulement le champ Parti_Politique
    UPDATE C
    SET C.Parti_Politique = i.Parti_Politique
    FROM Candidat c
    INNER JOIN Inserted i ON C.CandidatID = i.CandidatID;
END;
GO

-- tester
UPDATE Candidat
SET Nom = 'Dupont', Parti_Politique = 'Nouveau Parti'
WHERE CandidatID = 1;
*/
--SELECT * FROM Frais;
--SELECT * FROM Voyage;
--SELECT * FROM ActionDecision;
--SELECT * FROM Theme;  
--SELECT * FROM ProjetDeLoi;  
--SELECT * FROM Candidat; 

---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- curseur visiteCursor est utilisé pour sélectionner l'ID du village (VillageID) 
-- et la date de la visite (Date_visite) de la table Visites pour un CandidatID
--spécifique 
/*
Le rôle de ce curseur est donc de permettre un traitement ligne par ligne des visites effectuées par un candidat spécifique, 
souvent utilisé pour générer des rapports, des logs, ou pour effectuer des opérations supplémentaires dans une application ou un script plus large.
 Cela pourrait être utilisé, par exemple, pour afficher l'itinéraire d'un candidat, pour vérifier que les visites ont été effectuées conformément au plan,
  ou pour toute autre analyse nécessitant une inspection détaillée des visites individuelles.

  Le curseur visiteCursor dans ce script SQL sert à parcourir, de manière séquentielle, 
  un ensemble de données extraites de la table Visites pour un CandidatID donné.
  
   */

DECLARE @CandidatID INT = 1;
DECLARE @VillageID VARCHAR(20);
DECLARE @DateVisite DATE;

-- Création du curseur
DECLARE visiteCursor CURSOR FOR
SELECT VillageID, Date_visite
FROM Visites
WHERE CandidatID = @CandidatID;

-- Ouverture du curseur
OPEN visiteCursor;

-- Récupération de la première ligne
FETCH NEXT FROM visiteCursor INTO @VillageID, @DateVisite;

-- Boucle pour parcourir les lignes
WHILE @@FETCH_STATUS = 0
BEGIN
    
    -- Ici, afficher l'ID du village et la date de la visite :
    PRINT 'Visite au village ' + @VillageID + ' le ' + CONVERT(VARCHAR, @DateVisite, 101);

    -- Passez à la ligne suivante
    FETCH NEXT FROM visiteCursor INTO @VillageID, @DateVisite;
END;

-- Fermeture du curseur
CLOSE visiteCursor;
DEALLOCATE visiteCursor;

--------------------------------des requettes pour l'application--------------------------------------------------
-- 1) Sélectionner les visites planifiées pour un candidat donné-------------------------
SELECT v.Date_visite, vi.Nom, vi.Region
FROM Visites v
JOIN Village vi ON v.VillageID = vi.VillageID
WHERE v.CandidatID = 1; -- Remplacez ? par l'ID du candidat

-- Insérer une nouvelle visite
INSERT INTO Visites (CandidatID, VillageID, Date_visite) VALUES (1, 'PEO', '2024-04-19');

-- 2) Calculer le total des dépenses pour un candidat donné-------------------------------
SELECT SUM(f.Montant) AS TotalDepenses
FROM Frais f
JOIN VoyageParCandidat vc ON f.VoyageID = vc.VoyageID
WHERE vc.CandidatID = 1;

-- Estimer l'impact financier total des projets de loi d'une campagne donnée
SELECT SUM(pl.ImpactFinancier) AS ImpactTotal
FROM ProjetDeLoi pl
WHERE pl.CampagneID = 3;

--3) Sélectionner les actions et décisions récentes d'un candidat-----------------------------
SELECT ad.Description, ad.Date
FROM ActionDecision ad
WHERE ad.CandidatID = 1
ORDER BY ad.Date DESC;

-- 4) Trouver les lois existantes modifiées par les projets de loi d'une campagne-------------
SELECT l.Titre, l.Contenu
FROM Loi l
JOIN LoiModifie lm ON l.Code_loi = lm.Code_loi
JOIN ProjetDeLoi pl ON lm.Code_projet = pl.Code_projet
WHERE pl.CampagneID = 3;    

-- 5) Rapport des visites par village avec comptage des visites-------------------------------
SELECT vi.Nom, COUNT(*) AS NombreDeVisites
FROM Visites v
JOIN Village vi ON v.VillageID = vi.VillageID
GROUP BY vi.Nom;

-- Statistiques sur les propositions de loi par impact financier
SELECT pl.Titre, AVG(pl.ImpactFinancier) AS ImpactMoyen
FROM ProjetDeLoi pl
GROUP BY pl.Titre;