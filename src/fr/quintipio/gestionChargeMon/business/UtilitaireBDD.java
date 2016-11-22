package fr.quintipio.gestionChargeMon.business;

import java.sql.SQLException;

import fr.quintipio.gestionChargeMon.bdd.ComBDD;

/**
 * Outil propre au projet pour gérer la base
 *
 *
 */
public class UtilitaireBDD 
{
	/**
	 * Méthode permettant de fabriquer ou réparer automatiquement le structure de la base ( a appeler au démarrage de la connexion)
	 * @param connexion : la connexion sur laquelle se trouve la base
	 * @throws SQLException
	 */
	public static void CreationBDD(ComBDD connexion) throws SQLException
	{
		//REQUETE DES VARIABLES
		StringBuilder requeteCreateVariable = new StringBuilder();
		requeteCreateVariable.append("CREATE TABLE IF NOT EXISTS variables(");
		requeteCreateVariable.append("nom VARCHAR(40) NOT NULL PRIMARY KEY,");
		requeteCreateVariable.append("valeur VARCHAR(50) NOT NULL)");
		
		//REQUETE DES UNITES
		StringBuilder requeteCreateUnite = new StringBuilder();
		requeteCreateUnite.append("CREATE TABLE IF NOT EXISTS unite (");
		requeteCreateUnite.append("id INT NOT NULL,");
		requeteCreateUnite.append("diminutif VARCHAR(15) NOT NULL,");
		requeteCreateUnite.append("nom VARCHAR(150) NOT NULL)");
		
		//REQUETE DES CATEGORIES
		StringBuilder requeteCreateCategorie = new StringBuilder();
		requeteCreateCategorie.append("CREATE TABLE IF NOT EXISTS ref_categorie (");
		requeteCreateCategorie.append("id INT NOT NULL PRIMARY KEY,");
		requeteCreateCategorie.append("nom VARCHAR(150) NOT NULL)");
		
		//REQUETE DES RECETTES
		StringBuilder requeteCreateRecette = new StringBuilder();
		requeteCreateRecette.append("CREATE TABLE IF NOT EXISTS recette (");
		requeteCreateRecette.append("id INT NOT NULL PRIMARY KEY,");
		requeteCreateRecette.append("nom VARCHAR(150) NOT NULL,");
		requeteCreateRecette.append("prix DOUBLE NOT NULL,");
		requeteCreateRecette.append("description TEXT)");
		
		//REQUETE DES INGREDIENTS
		StringBuilder requeteCreateIngredients = new StringBuilder();
		requeteCreateIngredients.append("CREATE TABLE IF NOT EXISTS ingredient (");
		requeteCreateIngredients.append("id INT NOT NULL PRIMARY KEY,");
		requeteCreateIngredients.append("nom VARCHAR(150) NOT NULL,");
		requeteCreateIngredients.append("prix DOUBLE NOT NULL,");
		requeteCreateIngredients.append("idUnite INT NOT NULL,");
		requeteCreateIngredients.append("idCategorie INT NOT NULL,");
		requeteCreateIngredients.append("FOREIGN KEY (idUnite) REFERENCES unite(id),");
		requeteCreateIngredients.append("FOREIGN KEY (idCategorie) REFERENCES ref_categorie(id))");
		
		//REQUETE DE REGROUPEMENT
		StringBuilder requeteCreateRefRecette = new StringBuilder();
		requeteCreateRefRecette.append("CREATE TABLE IF NOT EXISTS ref_recette (");
		requeteCreateRefRecette.append("idRecette INT NOT NULL,");
		requeteCreateRefRecette.append("idIngredient INT NOT NULL,");
		requeteCreateRefRecette.append("quantite DOUBLE NOT NULL,");
		requeteCreateRefRecette.append("CONSTRAINT pk_refRecette PRIMARY KEY(idRecette,idIngredient),");
		requeteCreateRefRecette.append("FOREIGN KEY (idRecette) REFERENCES recette(id),");
		requeteCreateRefRecette.append("FOREIGN KEY (idIngredient) REFERENCES ingredient(id))");
		
		//EXECUTION DES REQUETES
		connexion.executeUpdateQuery(requeteCreateVariable.toString());
		connexion.executeUpdateQuery(requeteCreateUnite.toString());
		connexion.executeUpdateQuery(requeteCreateCategorie.toString());
		connexion.executeUpdateQuery(requeteCreateRecette.toString());
		connexion.executeUpdateQuery(requeteCreateIngredients.toString());
		connexion.executeUpdateQuery(requeteCreateRefRecette.toString());
		
		
		//AJOUT DES DONNEES EN BASE
		if(!connexion.executeQuery("SELECT * FROM variables").next())
		{
			connexion.executeUpdateQuery("INSERT INTO variables ( nom,valeur ) VALUES ( 'coef_fixe', '3')");
			connexion.executeUpdateQuery("INSERT INTO variables ( nom,valeur ) VALUES ( 'taxe', '1.07')");
			connexion.executeUpdateQuery("INSERT INTO variables ( nom,valeur ) VALUES ( 'user', '')");
		}
		
		if(!connexion.executeQuery("SELECT * FROM ref_categorie WHERE id=0").next())
			{connexion.executeUpdateQuery("INSERT INTO ref_categorie (id,nom) VALUES (0, 'non défini')");}
		
		if(!connexion.executeQuery("SELECT * FROM unite WHERE id=0").next())
		{connexion.executeUpdateQuery("INSERT INTO unite (id,diminutif,nom) VALUES (0,'ND', 'non défini')");}
	}
}
