package fr.quintipio.gestionChargeMon.business;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fr.quintipio.gestionChargeMon.bdd.ComBDD;
import fr.quintipio.gestionChargeMon.contexte.ContexteUtilisateur;
import fr.quintipio.gestionChargeMon.entite.Unite;

/**
 * Classe permettant de gérer les unités de mesure en base
 *
 *
 */
public class UniteBusiness
{
	/**
	 * Ajouter une unité en base (l'id de l'objet en paramétre recevra l'id attribué par la base si la modif est fait sinon ce sera -1
	 * @param unite : l'unité é ajouter
	 * @throws SQLException
	 */
	public static void ajoutUnite(Unite unite) throws SQLException
	{
		ComBDD base = ContexteUtilisateur.getBaseSQL();
		PreparedStatement ps = base.getConnexion().prepareStatement("INSERT INTO unite (id,diminutif,nom) VALUES (?,?,?)");
		int id = base.getIdMaxTable("unite")+1;
		ps.setInt(1,id);
		ps.setString(2,unite.getDiminutif());
		ps.setString(3,unite.getNom());
		
		if(base.executeUpdateQuery(ps) > 0)
			{unite.setId(id);}
		else
			{unite.setId(-1);}
	}
	
	
	/**
	 * Efface en base l'unité en paramétre (grace é l'id)
	 * @param unite : l'unité é effacer
	 * @throws SQLException
	 */
	public static void supprimerUnite(Unite unite) throws SQLException
	{
		ComBDD base = ContexteUtilisateur.getBaseSQL();
		base.executeUpdateQuery("UPDATE ingredient SET idUnite=0 WHERE idUnite="+unite.getId());
		base.executeUpdateQuery("DELETE FROM unite WHERE id="+unite.getId());
	}
	
	
	/**
	 * Modifie l'unité en paramétre en la recherchant en base grace é son id (si la modification echoue, le nom et le diminutif de l'objet deviendront NULL
	 * @param unite : la unité é modifier
	 * @throws SQLException
	 */
	public static void modifierUnite(Unite unite) throws SQLException
	{
		ComBDD base = ContexteUtilisateur.getBaseSQL();
		PreparedStatement ps = base.getConnexion().prepareStatement("UPDATE unite SET nom=?, diminutif=? WHERE id=?");
		ps.setString(1,unite.getNom());
		ps.setString(2,unite.getDiminutif());
		ps.setInt(3,unite.getId());
		
		if(base.executeUpdateQuery(ps) <= 0)
			{
			unite.setNom(null);
			unite.setDiminutif(null);
			}
		
	}
	
	/**
	 * Permet de récupérer une unité dont l'id est dans l'objet en paramétre
	 * Les données seront ajouter é l'objet en paramétre
	 * @param : l'id de l'unité rechercher
	 * @return : l'unite trouvée sinon null
	 * @throws SQLException
	 */
	public static Unite getUnite(int id) throws SQLException
	{
		ComBDD base = ContexteUtilisateur.getBaseSQL();
		ResultSet res = base.executeQuery("SELECT diminutif,nom FROM unite WHERE id="+id);
		if(res.next())
		{
			return new Unite(id,res.getString(1),res.getString(2));
		}
		else
		{return null;}
	}
	
	/**
	 * Retourne une liste de toute les unités
	 * @return : la liste de toute les unités
	 * @throws SQLException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public static ArrayList<Unite> getAllUnite() throws SQLException
	{
		ComBDD base = ContexteUtilisateur.getBaseSQL();
		ResultSet res = base.executeQuery("SELECT id,diminutif,nom FROM unite");
		
		ArrayList<Unite> liste = new ArrayList<Unite>();
		while(res.next())
			{liste.add(new Unite(res.getInt(1),res.getString(2),res.getString(3)));}
		return liste;
	}
	
	/**
	 * UNIQUEMENT POUR L'ANCIENNE BASE MYSQL - Retourne une liste de toute les unités
	 * @return : la liste de toute les unités
	 * @throws SQLException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public static ArrayList<Unite> getAllUniteMySQL() throws SQLException
	{
		ComBDD base = ContexteUtilisateur.getBaseSQL();
		ResultSet res = base.executeQuery("SELECT id,diminutif,nom FROM ref_unite");
		
		ArrayList<Unite> liste = new ArrayList<Unite>();
		while(res.next())
			{liste.add(new Unite(res.getInt(1),res.getString(2),res.getString(3)));}
		return liste;
	}
	
	/**
	 * Permet d'obtenir le nombre d'unités en base
	 * @return : le nombre de unités
	 * @throws SQLException
	 */
	public static int compteUnites() throws SQLException
	{
		ResultSet res = ContexteUtilisateur.getBaseSQL().executeQuery("SELECT COUNT(id) FROM unite");
		if(res.next())
		{return res.getInt(1);}
		else
		{return 0;}
	}
}
