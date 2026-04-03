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

--SELECT * FROM Frais;
--SELECT * FROM Voyage;
--SELECT * FROM ActionDecision;
--SELECT * FROM Theme;  
--SELECT * FROM ProjetDeLoi;  
--SELECT * FROM Candidat;
             
 --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

-- curseur visiteCursor est utilisé pour sélectionner l'ID du village (VillageID) 
-- et la date de la visite (Date_visite) de la table Visites pour un CandidatID
--spécifique 
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