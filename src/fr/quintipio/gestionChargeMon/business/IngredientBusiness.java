package fr.quintipio.gestionChargeMon.business;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fr.quintipio.gestionChargeMon.bdd.ComBDD;
import fr.quintipio.gestionChargeMon.contexte.ContexteUtilisateur;
import fr.quintipio.gestionChargeMon.entite.Categorie;
import fr.quintipio.gestionChargeMon.entite.Ingredient;
import fr.quintipio.gestionChargeMon.entite.Unite;

/**
 * Permet de gérer les ingrédients en base
 *
 *
 */
public class IngredientBusiness 
{
	/**
	 * Permet d'ajouter un ingrédient en paramétre dans la base de donnée. Si l'ajout est effectué avec succés
	 * l'objet en paramétre prendra l'id attribué par la base sinon 0
	 * @param ingredient : l'ingrédient é ajouter avec un id é zéro
	 * @throws SQLException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public static void ajouterIngredient(Ingredient ingredient) throws SQLException
	{
		ComBDD base = ContexteUtilisateur.getBaseSQL();
		int id = base.getIdMaxTable("ingredient")+1;
		
		PreparedStatement ps = base.getConnexion().prepareStatement("INSERT INTO ingredient(id,nom,prix,idUnite,idCategorie) VALUES(?,?,?,?,?)");
		ps.setInt(1,id);
		ps.setString(2,ingredient.getNom());
		ps.setDouble(3,ingredient.getPrix());
		ps.setInt(4,ingredient.getUnite().getId());
		ps.setInt(5,ingredient.getCategorie().getId());
		
		if(base.executeUpdateQuery(ps) > 0)
		{ingredient.setId(id);}
		else
		{ingredient.setId(0);}
	}
	
	/**
	 * Efface en base un ingrédient dont l'id est précisé dans 'lobjet en paramétre
	 * @param ingredient : l'ingrédient é éffacer
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static void supprimerIngredient(Ingredient ingredient) throws SQLException
	{
		ComBDD base = ContexteUtilisateur.getBaseSQL();
		base.executeUpdateQuery("DELETE FROM ref_recette WHERE idIngredient="+ingredient.getId());
		base.executeUpdateQuery("DELETE FROM ingredient WHERE id="+ingredient.getId());
	}
	
	/**
	 * Modifie en base les informations de l'ingrédient en paramétre en l'identifiant é partir de l'id
	 * @param ingredient : l'ingrédient é modifier
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static void modifierIngredient(Ingredient ingredient)throws SQLException
	{
		ComBDD base = ContexteUtilisateur.getBaseSQL();
		PreparedStatement ps = base.getConnexion().prepareStatement("UPDATE ingredient SET nom=?, prix=?, idUnite=?, idCategorie=? WHERE id ="+ingredient.getId());
		ps.setString(1,ingredient.getNom());
		ps.setDouble(2,ingredient.getPrix());
		ps.setInt(3,ingredient.getUnite().getId());
		ps.setInt(4,ingredient.getCategorie().getId());
		base.executeUpdateQuery(ps);
	}
	
	/**
	 * Permet de récupérer un objet ingrédient é partir de l'id en paramétre
	 * @param id : id de l'ingrédient é récupére
	 * return : l'objet ingrédient obtenu
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static Ingredient getIngredient(int id) throws SQLException
	{
		ComBDD base = ContexteUtilisateur.getBaseSQL();
		StringBuilder requete = new StringBuilder();
		requete.append("SELECT ingredient.nom,ingredient.prix, ");
		requete.append("ref_categorie.id, ref_categorie.nom,");
		requete.append("unite.id, unite.diminutif,unite.nom");
		requete.append("FROM ingredient ");
		requete.append("INNER JOIN ref_categorie ON ingredient.idCategorie = ref_categorie=id ");
		requete.append("INNER JOIN unite ON ingredient.idUnite = unite.id");
		requete.append("WHERE ingredient.id = "+id);
		ResultSet res = base.executeQuery(requete.toString());
		
		if(res.next())
		{
		return new Ingredient(id,
							  res.getString(1),
							  res.getDouble(2),
							  new Unite(res.getInt(5),res.getString(6),res.getString(7)),
							  new Categorie(res.getInt(3),res.getString(4))
							  );
		}
		else
		{return null;}
	}
	
	
	/**
	 * Permet de récupérer tout les ingrédients en base
	 * @return : la liste de tout les ingrédients
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static ArrayList<Ingredient> getAllIngredient() throws SQLException
	{
		ComBDD base = ContexteUtilisateur.getBaseSQL();
		StringBuilder requete = new StringBuilder();
		requete.append("SELECT ingredient.id,ingredient.nom,ingredient.prix, ");
		requete.append("ref_categorie.id, ref_categorie.nom,");
		requete.append("unite.id, unite.diminutif,unite.nom ");
		requete.append("FROM ingredient ");
		requete.append("INNER JOIN ref_categorie ON ingredient.idCategorie = ref_categorie=id ");
		requete.append("INNER JOIN unite ON ingredient.idUnite = unite.id");
		ResultSet res = base.executeQuery(requete.toString());
		
		ArrayList<Ingredient> liste = new ArrayList<Ingredient>();
		while(res.next())
		{
		liste.add(new Ingredient(res.getInt(1), res.getString(2),res.getDouble(3),
							  new Unite(res.getInt(6),res.getString(7),res.getString(8)),
							  new Categorie(res.getInt(4),res.getString(5))
							  ));
		}
		return liste;
	}
	
	/**
	 * Permet de récupérer tout les ingrédients de la catégorie en paramétre
	 * @param categorie : la catégorie dont ont recherche les ingrédients
	 * @return : la liste des ingrédients
	 * @throws SQLException
	 */
	public static ArrayList<Ingredient> getIngredientCategorie(Categorie categorie) throws SQLException
	{
		ComBDD base = ContexteUtilisateur.getBaseSQL();
		StringBuilder requete = new StringBuilder();
		requete.append("SELECT ingredient.id,ingredient.nom,ingredient.prix, ");
		requete.append("unite.id, unite.diminutif,unite.nom ");
		requete.append("FROM ingredient ");
		requete.append("INNER JOIN unite ON ingredient.idUnite = unite.id ");
		requete.append("WHERE ingredient.idCategorie = "+categorie.getId());
		
		ResultSet res = base.executeQuery(requete.toString());
		ArrayList<Ingredient> liste = new ArrayList<Ingredient>();
		while(res.next())
		{
		liste.add(new Ingredient(res.getInt(1), res.getString(2),res.getDouble(3),
							  new Unite(res.getInt(4),res.getString(5),res.getString(6)),
							  categorie
							  ));
		}
		return liste;
	}
	
	
	
	/**
	 * UNIQUEMENT POUR MYSQL - Permet de récupérer tout les ingrédients en base
	 * @return : la liste de tout les ingrédients
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static ArrayList<Ingredient> getAllIngredientMySQL() throws SQLException
	{
		ComBDD base = ContexteUtilisateur.getBaseSQL();
		StringBuilder requete = new StringBuilder();
		requete.append("SELECT ingredient.id,ingredient.nom,ingredient.prix, ");
		requete.append("ref_categorie.id, ref_categorie.nom,");
		requete.append("ref_unite.id, ref_unite.diminutif,ref_unite.nom ");
		requete.append("FROM ingredient ");
		requete.append("INNER JOIN ref_categorie ON ingredient.id_categorie = ref_categorie.id ");
		requete.append("INNER JOIN ref_unite ON ingredient.id_unite = ref_unite.id");
		ResultSet res = base.executeQuery(requete.toString());
		
		ArrayList<Ingredient> liste = new ArrayList<Ingredient>();
		while(res.next())
		{
		liste.add(new Ingredient(res.getInt(1), res.getString(2),res.getDouble(3),
							  new Unite(res.getInt(6),res.getString(7),res.getString(8)),
							  new Categorie(res.getInt(4),res.getString(5))
							  ));
		}
		return liste;
	}
	
	/**
	 * Permet d'obtenir le nombre d'ingrédients en base
	 * @return : le nombre d'ingrédients
	 * @throws SQLException
	 */
	public static int compteIngredients() throws SQLException
	{
		ResultSet res = ContexteUtilisateur.getBaseSQL().executeQuery("SELECT COUNT(id) FROM ingredient");
		if(res.next())
		{return res.getInt(1);}
		else
		{return 0;}
	}
}
