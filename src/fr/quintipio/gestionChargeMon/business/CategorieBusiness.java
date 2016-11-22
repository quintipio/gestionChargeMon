package fr.quintipio.gestionChargeMon.business;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fr.quintipio.gestionChargeMon.bdd.ComBDD;
import fr.quintipio.gestionChargeMon.contexte.ContexteUtilisateur;
import fr.quintipio.gestionChargeMon.entite.Categorie;

/**
 * Classe permettant de gérer les catégories en base
 *
 *
 */
public class CategorieBusiness
{
	/**
	 * Ajouter une catégorie en base (l'id de l'objet en paramétre recevra l'id attribué par la base si la modif est fait sinon ce sera -1
	 * @param categorie : la cétegorie é ajouter
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static void ajoutCategorie(Categorie categorie) throws SQLException
	{
		ComBDD base = ContexteUtilisateur.getBaseSQL();
		PreparedStatement ps = base.getConnexion().prepareStatement("INSERT INTO ref_categorie (id,nom) VALUES (?,?)");
		int id = base.getIdMaxTable("ref_categorie")+1;
		ps.setInt(1,id);
		ps.setString(2, categorie.getNom());
		
		if(base.executeUpdateQuery(ps) > 0)
			{categorie.setId(id);}
		else
			{categorie.setId(-1);}
	}
	
	
	/**
	 * Efface en base la catégorie en paramétre (grace é l'id)
	 * @param categorie : la catégorie é effacer
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static void supprimerCategorie(Categorie categorie) throws SQLException
	{
		ComBDD base = ContexteUtilisateur.getBaseSQL();
		base.executeUpdateQuery("UPDATE ingredient SET idCategorie=0 WHERE idCategorie="+categorie.getId());
		base.executeUpdateQuery("DELETE FROM ref_categorie WHERE id="+categorie.getId());
		
	}
	
	
	/**
	 * Modifie la categorie en paramétre en la recherchant en base grace é son id (si la modification echoue, le nom de l'objet deviendra NULL
	 * @param categorie : la catégorie é modifier
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static void modifierCategorie(Categorie categorie) throws SQLException
	{
		ComBDD base = ContexteUtilisateur.getBaseSQL();
		PreparedStatement ps = base.getConnexion().prepareStatement("UPDATE ref_categorie SET nom=? WHERE id=?");
		ps.setString(1,categorie.getNom());
		ps.setInt(2,categorie.getId());
		
		if(base.executeUpdateQuery(ps) <= 0)
			{categorie.setNom(null);}
		
	}
	
	/**
	 * Permet de récupérer une catégorie dont l'id est dans l'objet en paramétre
	 * Les données seront ajouter é l'objet en paramétre
	 * @param id : l'id de la catégorie rechercher
	 * return la catégorie trouvée
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static Categorie getCategorie(int id) throws SQLException
	{
		ComBDD base = ContexteUtilisateur.getBaseSQL();
		ResultSet res = base.executeQuery("SELECT nom FROM ref_categorie WHERE id="+id);
		if(res.next())
			{return new Categorie(id,res.getString(1));}
		else
		{return null;}
	}
	
	/**
	 * Retourne une liste de toute les catégories
	 * @return : la liste de toute les catégories
	 * @throws SQLException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public static ArrayList<Categorie> getAllCategorie() throws SQLException
	{
		ComBDD base = ContexteUtilisateur.getBaseSQL();
		ResultSet res = base.executeQuery("SELECT id,nom FROM ref_categorie");
		
		ArrayList<Categorie> liste = new ArrayList<Categorie>();
		while(res.next())
			{liste.add(new Categorie(res.getInt(1),res.getString(2)));}
		return liste;
	}
	
	/**
	 * Permet d'obtenir le nombre de caétégories en base
	 * @return : le nombre de catégories
	 * @throws SQLException
	 */
	public static int compteCategories() throws SQLException
	{
		ResultSet res = ContexteUtilisateur.getBaseSQL().executeQuery("SELECT COUNT(id) FROM ref_categorie");
		if(res.next())
		{return res.getInt(1);}
		else
		{return 0;}
	}
}
