package fr.quintipio.gestionChargeMon.business;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map.Entry;

import fr.quintipio.gestionChargeMon.contexte.ContexteUtilisateur;
import fr.quintipio.gestionChargeMon.entite.Categorie;
import fr.quintipio.gestionChargeMon.entite.Ingredient;
import fr.quintipio.gestionChargeMon.entite.Recette;
import fr.quintipio.gestionChargeMon.entite.Unite;

/**
 * Classe de static permettant de communiquer avec la base de donnée pour gérer les recettes
 *
 *
 */
public class RecetteBusiness 
{
	/**
	 * Permet d'effacer une recette de la base
	 * @param recette : la recette é effacer
	 * @throws SQLException
	 */
	public static void deleteRecette(Recette recette) throws SQLException
	{
		ContexteUtilisateur.getBaseSQL().executeUpdateQuery("DELETE FROM ref_recette WHERE idRecette="+recette.getId());
		ContexteUtilisateur.getBaseSQL().executeUpdateQuery("DELETE FROM recette WHERE id="+recette.getId());
	}
	
	/**
	 * Permet d'ajouter une recette en base. L'id de la recette en paramétre prendre l'id attribué par la base si l'opération é réussi, sinon 0
	 * @param recette : la recette é ajouter
	 * @throws SQLException
	 */
	public static void insertRecette(Recette recette) throws SQLException
	{
		//création de la recette
		PreparedStatement ps = ContexteUtilisateur.getBaseSQL().getConnexion().prepareStatement("INSERT INTO recette(id,nom,prix,description) VALUES(?,?,?,?)");
		int id = ContexteUtilisateur.getBaseSQL().getIdMaxTable("recette")+1;
		ps.setInt(1,id);
		ps.setString(2,recette.getNom());
		ps.setDouble(3,recette.getPrix());
		ps.setString(4,recette.getDescription());
		
		if(ContexteUtilisateur.getBaseSQL().executeUpdateQuery(ps) > 0)
		{
			boolean passe = true;
			//rajout de chaque ingrédient
			for(Entry<Ingredient,Double> entry : recette.getListeIngredient().entrySet()) 
			{
			    PreparedStatement ps2 = ContexteUtilisateur.getBaseSQL().getConnexion().prepareStatement("INSERT INTO ref_recette(idRecette,idIngredient,quantite) VALUES(?,?,?)");
			    ps2.setInt(1,id);
			    ps2.setInt(2,entry.getKey().getId());
			    ps2.setDouble(3, entry.getValue());
			    
			    //verification que éa se passe bien
			    if(ContexteUtilisateur.getBaseSQL().executeUpdateQuery(ps2) == 0)
			    {passe=false;}
			}
			
			//si tout est passé, ont met é jour l'id pour valider
			if(passe)
			{recette.setId(id);}
		}
	}
	
	/**
	 * Permet de récupérer la liste de toute les recettes
	 * @return : une liste compléte des recettes
	 * @throws SQLException
	 */
	public static ArrayList<Recette> getAllRecette() throws SQLException
	{
		ArrayList<Recette> listeRecette = new ArrayList<Recette>();
		StringBuilder requete = new StringBuilder();
		
		requete.append("SELECT id,nom,prix,description ");
		requete.append("FROM recette");
		
		ResultSet resRecette = ContexteUtilisateur.getBaseSQL().executeQuery(requete.toString());
		
		while(resRecette.next())
		{
			int id = resRecette.getInt(1);
			String nom = resRecette.getString(2);
			Double prix = resRecette.getDouble(3);
			String description = resRecette.getString(4);
			
			requete = new StringBuilder();
			requete.append("SELECT ref_recette.quantite, ");
			requete.append("ingredient.id, ingredient.nom, ingredient.prix,");
			requete.append("unite.id, unite.diminutif, unite.nom, ");
			requete.append("ref_categorie.id, ref_categorie.nom ");
			requete.append("FROM ref_recette ");
			requete.append("INNER JOIN ingredient ON ref_recette.idIngredient = ingredient.id ");
			requete.append("INNER JOIN unite ON ingredient.idUnite = unite.id ");
			requete.append("INNER JOIN ref_categorie ON ingredient.idCategorie = ref_categorie.id ");
			requete.append("WHERE ref_recette.idRecette="+id);
			
			Hashtable<Ingredient,Double> tableIngredientRecette = new Hashtable<Ingredient,Double>();
			ResultSet listeIngredient =  ContexteUtilisateur.getBaseSQL().executeQuery(requete.toString());
			while(listeIngredient.next())
			{
				Double quantiteIngredient = listeIngredient.getDouble(1);
				int idIngredient = listeIngredient.getInt(2);
				String nomIngredient = listeIngredient.getString(3);
				Double prixIngredient = listeIngredient.getDouble(4);
				Unite uniteIngredient = new Unite(listeIngredient.getInt(5),listeIngredient.getString(6),listeIngredient.getString(7));
				Categorie categorieIngredient = new Categorie(listeIngredient.getInt(8),listeIngredient.getString(9));
				
				tableIngredientRecette.put(new Ingredient(idIngredient, nomIngredient, prixIngredient, uniteIngredient, categorieIngredient),quantiteIngredient);
			}
			
			listeRecette.add(new Recette(id, nom, prix, tableIngredientRecette,description));
		}
		return listeRecette;
	}
	
	/**
	 * Modification d'une recette en base
	 * @param recette : la recette é modifier : retrouvé gréce é l'id
	 * @throws SQLException
	 */
	public static void modifierRecette(Recette recette) throws SQLException
	{
		//mise é jour de la recette
		PreparedStatement ps = ContexteUtilisateur.getBaseSQL().getConnexion().prepareStatement("UPDATE recette SET nom=?, prix=?, description=? WHERE id="+recette.getId());
		ps.setString(1,recette.getNom());
		ps.setDouble(2,recette.getPrix());
		ps.setString(3, recette.getDescription());
		
		ContexteUtilisateur.getBaseSQL().executeUpdateQuery(ps);
		
		//effacement des ingrédients
		ContexteUtilisateur.getBaseSQL().executeUpdateQuery("DELETE FROM ref_recette WHERE idRecette="+recette.getId());
			
		//pour les remettres en prenant en compte d'eventuels modification
		for(Entry<Ingredient,Double> entry : recette.getListeIngredient().entrySet()) 
		{
		    PreparedStatement ps2 = ContexteUtilisateur.getBaseSQL().getConnexion().prepareStatement("INSERT INTO ref_recette(idRecette,idIngredient,Quantite) VALUES(?,?,?)");
		    ps2.setInt(1,recette.getId());
		    ps2.setInt(2,entry.getKey().getId());
		    ps2.setDouble(3, entry.getValue());
		    
		    ContexteUtilisateur.getBaseSQL().executeUpdateQuery(ps2);
		}
	}
	
	
	
	/**
	 * UNIQUEMENT POUR MySQL - Permet de récupérer la liste de toute les recettes
	 * @return : une liste compléte des recettes
	 * @throws SQLException
	 */
	public static ArrayList<Recette> getAllRecetteMySQL() throws SQLException
	{
		ArrayList<Recette> listeRecette = new ArrayList<Recette>();
		StringBuilder requete = new StringBuilder();
		
		requete.append("SELECT id,nom,prix ");
		requete.append("FROM recette");
		
		ResultSet resRecette = ContexteUtilisateur.getBaseSQL().executeQuery(requete.toString());
		
		while(resRecette.next())
		{
			int id = resRecette.getInt(1);
			String nom = resRecette.getString(2);
			Double prix = resRecette.getDouble(3);
			
			requete = new StringBuilder();
			requete.append("SELECT ref_recette.quantite, ");
			requete.append("ingredient.id, ingredient.nom, ingredient.prix,");
			requete.append("ref_unite.id, ref_unite.nom, ref_unite.diminutif, ");
			requete.append("ref_categorie.id, ref_categorie.nom ");
			requete.append("FROM ref_recette ");
			requete.append("INNER JOIN ingredient ON ref_recette.id_ingredient = ingredient.id ");
			requete.append("INNER JOIN ref_unite ON ingredient.id_unite = ref_unite.id ");
			requete.append("INNER JOIN ref_categorie ON ingredient.id_categorie = ref_categorie.id ");
			requete.append("WHERE ref_recette.id_recette="+id);
			
			Hashtable<Ingredient,Double> tableIngredientRecette = new Hashtable<Ingredient,Double>();
			ResultSet listeIngredient =  ContexteUtilisateur.getBaseSQL().executeQuery(requete.toString());
			while(listeIngredient.next())
			{
				Double quantiteIngredient = (double) listeIngredient.getFloat(1);
				int idIngredient = listeIngredient.getInt(2);
				String nomIngredient = listeIngredient.getString(3);
				Double prixIngredient = listeIngredient.getDouble(4);
				Unite uniteIngredient = new Unite(listeIngredient.getInt(5),listeIngredient.getString(7),listeIngredient.getString(6));
				Categorie categorieIngredient = new Categorie(listeIngredient.getInt(8),listeIngredient.getString(9));
				
				tableIngredientRecette.put(new Ingredient(idIngredient, nomIngredient, prixIngredient, uniteIngredient, categorieIngredient),quantiteIngredient);
			}
			
			listeRecette.add(new Recette(id, nom, prix, tableIngredientRecette));
		}
		return listeRecette;
	}
	
	/**
	 * Permet d'obtenir le nombre de recettes en base
	 * @return : le nombre de recettes
	 * @throws SQLException
	 */
	public static int compteRecettes() throws SQLException
	{
		ResultSet res = ContexteUtilisateur.getBaseSQL().executeQuery("SELECT COUNT(id) FROM recette");
		if(res.next())
		{return res.getInt(1);}
		else
		{return 0;}
	}
}
