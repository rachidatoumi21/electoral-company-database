-- Les requetes SQL utilisees dans l'application
-- Total: 15

-- Affichage des données des candidats
SELECT * FROM Candidat
	-- Les requetes simples pour la table Candidat:
		-- INSERT INTO Candidat(Prenom, Nom, Date_naissance, Parti_politique) VALUES (?,?,?,?)
		-- UPDATE Candidat SET Prenom=?, Nom=?, Date_naissance=?, Parti_politique=? WHERE CandidatID=?
		-- DELETE FROM Candidat where CandidatID=?
		-- SELECT CandidatID, Prenom, Nom, Date_naissance,Parti_politique FROM Candidat where CandidatID=?

-- Affichage des données des villages
SELECT * FROM Village
	-- Les requetes simples pour la table Village:
		-- INSERT INTO Village(VillageID, Nom, Region)values(?,?,?)
		-- UPDATE Village SET Nom=?, Region=? where VillageID=?
		-- DELETE FROM Village where VillageID=?
		-- SELECT VillageID, Nom, Region FROM Village where VillageID=?

-- Affichage nombre de visites a un village par un candidat
SELECT vi.VillageID, v.CandidatID, c.Prenom, c.Nom, COUNT(*) AS Nombre_Visites
FROM Candidat c
INNER JOIN Visites v ON c.CandidatID = v.CandidatID
INNER JOIN Village vi ON v.VillageID = vi.VillageID
GROUP BY v.CandidatID, vi.VillageID, c.Prenom, c.Nom;

-- Affichage des actions/décisions d'un candidat
SELECT ad.Description, ad.Date
FROM ActionDecision ad
WHERE ad.CandidatID = 1	  -- remplacer 1 par "?"
ORDER BY ad.Date DESC;

-- Affichage des visites planifiées par un candidat 
SELECT v.Date_visite, vi.Nom, vi.Region
FROM Visites v
JOIN Village vi ON v.VillageID = vi.VillageID
WHERE v.CandidatID = 1;   -- remplacer 1 par "?"

-- Calculer le total des dépenses des voyages d'un candidat 
SELECT SUM(f.Montant) AS TotalDepenses
FROM Frais f
JOIN VoyageParCandidat vc ON f.VoyageID = vc.VoyageID
WHERE vc.CandidatID = 1; -- remplacer 1 par "?"

-- Estimer l'impact financier total des projets de loi d'une campagne
SELECT SUM(ImpactFinancier) AS ImpactTotal 
FROM ProjetDeLoi WHERE CampagneID = 1  -- remplacer 1 par "?"

