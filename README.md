
# Gestion d'une campagne électorale
## Aperçu

Ce projet présente une application de gestion d’une campagne électorale appuyée par une base de données relationnelle et une interface Java. L’objectif est de centraliser les informations sur les candidats, les villages, les visites, les actions et décisions, les voyages, les frais, ainsi que les projets de loi afin de faciliter le suivi opérationnel et l’analyse de campagne. Le rapport du projet explique que la solution vise à organiser, capter et analyser les données essentielles au succès d’une campagne électorale.

L’application permet notamment de gérer les candidats et les villages, d’afficher les actions et décisions d’un candidat, de consulter les visites planifiées, de calculer les dépenses de voyage d’un candidat et d’estimer l’impact financier total des projets de loi d’une campagne. Ces fonctionnalités sont visibles dans les captures de l’application et correspondent aux requêtes SQL décrites dans le rapport et dans le script de base de données.

## Contexte académique

Projet réalisé dans le cadre du cours IFT-2935 – Bases de données à l’Université de Montréal.

## Auteurs

- Rachida Toumi
- Vi Phung


---

## Technologies utilisées

- Java
- SQL Server / T-SQL
- JDBC
- Interface graphique Java

---

### Objectifs du projet

Le système a été conçu pour répondre à plusieurs besoins d’une campagne électorale :

- gérer les informations détaillées des candidats ;
- suivre les campagnes électorales ;
- planifier les visites de villages et villes ;
- suivre les actions et décisions des candidats ;
- gérer les projets de loi et leurs impacts financiers ;
- enregistrer les voyages et les frais associés.

L’objectif final est de fournir une vue d’ensemble structurée de la campagne pour soutenir la prise de décision et la stratégie politique.

Le modèle relationnel présenté dans le rapport comprend les entités principales suivantes : Campagne, Candidat, Village, Action, Theme, Visite, ProjetLoi, Loi, LoiModifie, Voyage, VoyageParCandidat et Frais.

Le script SQL implémente concrètement ces tables avec des clés primaires et des clés étrangères. Il crée par exemple les tables Candidat, Campagne, Village, Theme, ActionDecision, Visites, Loi, ProjetDeLoi, LoiModifie, Voyage, VoyageParCandidat et Frais.

---

## Fonctionnalités principales
Gestion des candidats

L’application permet d’afficher et de gérer les données des candidats, notamment l’identifiant, le prénom, le nom, la date de naissance et le parti politique. La capture dédiée montre également les actions CRUD principales : insertion, modification, suppression et recherche par identifiant. Ces attributs correspondent à la table Candidat du script SQL.

![Gestion des candidats](screenshots/candidats.png)
Gestion des villages

L’application permet aussi de consulter et gérer les villages avec leur identifiant, leur nom et leur région, ce qui reflète directement la structure de la table Village. La capture montre les opérations d’insertion, de modification, de suppression et de recherche.

![Gestion des villages](screenshots/villages.png)
Affichage des actions et décisions d’un candidat

Une fonctionnalité permet d’afficher les actions et décisions récentes d’un candidat à partir de son identifiant. Le rapport décrit une requête qui retourne la description et la date des actions, triées par date décroissante, et le script SQL contient cette requête.

![Actions et décisions](screenshots/actions.png)
Affichage des visites planifiées

L’application affiche les visites planifiées par un candidat en montrant la date de visite, le nom du village et la région. Cette fonctionnalité correspond à la requête SELECT v.Date_visite, vi.Nom, vi.Region ... WHERE v.CandidatID = 1.

![Visites planifiées](screenshots/visites.png)
Calcul du total des dépenses de voyage

L’application permet de calculer la somme des frais associés aux voyages d’un candidat. Le rapport et le script montrent une requête utilisant SUM(f.Montant) avec une jointure entre Frais et VoyageParCandidat.

![Total des dépenses](screenshots/depenses.png)
Estimation de l’impact financier des projets de loi

L’application peut aussi estimer l’impact financier total des projets de loi d’une campagne donnée. Cette fonctionnalité repose sur une somme de ImpactFinancier dans la table ProjetDeLoi pour un CampagneID donné. fileciteturn0file0L112-L113

![Impact financier](screenshots/impact-financier.png)
Rapport sur le nombre de visites par village et par candidat

L’interface contient aussi un bloc d’affichage du nombre de visites à un village par un candidat. Cette idée s’inscrit dans les requêtes du rapport qui calculent le nombre de visites par village et, dans les requêtes complexes, le nombre de visites par candidat et par région.

![Nombre de visites](screenshots/nombre-visites.png)
Procédures stockées, triggers et curseur

Le projet ne se limite pas aux tables et requêtes de base. Le script T-SQL inclut aussi des mécanismes avancés :

des procédures stockées pour mettre à jour ActionDecision, Theme et ProjetDeLoi ;
des triggers pour enregistrer automatiquement la dernière mise à jour de Voyage et Frais, ainsi qu’un trigger qui interdit la mise à jour de certains champs du candidat en dehors de Parti_Politique ;
un curseur qui parcourt les visites d’un candidat donné afin d’afficher, ligne par ligne, les villages visités et leurs dates.

Ces éléments montrent un projet de base de données plus complet qu’un simple schéma CRUD.

Jeu de données de démonstration

Le script SQL insère des données d’exemple pour les candidats, villages, campagnes, thèmes, actions et décisions, visites, lois, projets de loi, voyages et frais. Cela permet de tester directement l’application et les requêtes sans devoir remplir la base manuellement.

Structure suggérée du repository
.
├── electoral_db.sql
├── T-SQL.sql
├── Java-applicationSQL/
│   ├── DbConnection.java
│   ├── JavaCrud.java
│   └── JavaCrud.form
├── screenshots/
│   ├── application-complete.png
│   ├── candidats.png
│   ├── villages.png
│   ├── actions.png
│   ├── visites.png
│   ├── depenses.png
│   └── impact-financier.png
└── README.md
Installation et exécution
1. Créer la base de données

Exécuter le script principal SQL dans SQL Server pour créer la base, les tables, les relations et les données d’exemple.

-- CREATE DATABASE CampagneElectorale;
use CampagneElectorale;

Le script principal contient ensuite les instructions de création des tables, les insertions de données et les requêtes utilisées par l’application.

2. Ajouter les procédures et triggers

Exécuter ensuite le fichier T-SQL.sql si vous souhaitez tester les procédures stockées, les triggers et le curseur indépendamment.

3. Configurer la connexion Java

Ouvrir le projet Java et adapter les paramètres de connexion à la base de données dans la classe de connexion selon votre environnement local.

4. Lancer l’application

Compiler puis exécuter l’interface Java pour accéder aux écrans de gestion et aux requêtes affichées dans les captures.

Captures d’écran
Vue d’ensemble de l’application
![Vue d’ensemble](screenshots/application-complete.png)
Gestion des candidats
![Gestion des candidats](screenshots/candidats.png)
Gestion des villages
![Gestion des villages](screenshots/villages.png)
Actions et décisions d’un candidat
![Actions et décisions](screenshots/actions.png)
Visites planifiées d’un candidat
![Visites planifiées](screenshots/visites.png)
Total des dépenses de voyage
![Total des dépenses](screenshots/depenses.png)
Impact financier d’une campagne
![Impact financier](screenshots/impact-financier.png)
Points forts du projet
modélisation d’une base de données relationnelle complète pour un cas réaliste ;
implémentation de tables liées par des clés étrangères ;
présence de requêtes simples et complexes pour l’analyse ;
intégration d’une interface Java avec fonctionnalités CRUD ;
utilisation de procédures stockées, triggers et curseur pour enrichir la logique de gestion. fileciteturn0file0L29-L57
Limites et améliorations possibles

Les captures montrent une application fonctionnelle et claire, mais plusieurs améliorations peuvent être envisagées :

validation plus robuste des entrées utilisateur ;
meilleure gestion des erreurs de connexion SQL ;
amélioration visuelle de l’interface ;
ajout d’authentification et de rôles ;
création d’un tableau de bord analytique plus avancé.
Conclusion

Ce projet met en place une solution cohérente pour la gestion d’une campagne électorale, depuis le stockage structuré des données jusqu’à leur exploitation dans une application Java. Le rapport conclut que la base a été conçue pour répondre aux besoins dynamiques d’une campagne politique, en combinant planification des visites, gestion financière et analyse des projets de loi.

