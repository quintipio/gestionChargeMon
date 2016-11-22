package fr.quintipio.gestionChargeMon.business;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.quintipio.gestionChargeMon.bdd.ComBDD;
import fr.quintipio.gestionChargeMon.contexte.ContexteUtilisateur;

/**
 * GETTER ET SETTER DES DONNEES VARIABLES EN BASE
 *
 *
 */
public class VariableBusiness 
{
	/**
	 * Permet d'obtenir la taxe enregistré en base
	 * @return : la taxe sinon null
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static Double getTaxe() throws SQLException
	{
		ComBDD base = ContexteUtilisateur.getBaseSQL();
		ResultSet res = base.executeQuery("SELECT valeur FROM variables WHERE nom='taxe'");
		if(res.next())
		{return (Double.parseDouble(res.getString(1)));}
		else
		{return null;}
	}
	
	
	/**
	 * Permet d'obtenir le coef_fixe enregistré en base
	 * @return : le coef fixe sinon null
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static Double getCoefFixe() throws SQLException
	{
		ComBDD base = ContexteUtilisateur.getBaseSQL();
		ResultSet res = base.executeQuery("SELECT valeur FROM variables WHERE nom='coef_fixe'");
		if(res.next())
		{return (Double.parseDouble(res.getString(1)));}
		else
		{return null;}
	}
	
	/**
	 * Permet de récupérer le nom d'utilisateur du logiciel
	 * @return : le nom d'utilisateur
	 * @throws SQLException
	 */
	public static String getUser() throws SQLException
	{
		ComBDD base = ContexteUtilisateur.getBaseSQL();
		ResultSet res = base.executeQuery("SELECT valeur FROM variables WHERE nom='user'");
		if(res.next())
		{return res.getString(1);}
		else
		{return "";}
	}
	
	
	/**
	 * Permet de modifier la taxe enregistré en base
	 * @return : 1 ou succés sinon 0
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static int setTaxe(Double taxe) throws SQLException
	{
		ComBDD base = ContexteUtilisateur.getBaseSQL();
		PreparedStatement ps = base.getConnexion().prepareStatement("UPDATE variables SET valeur=? WHERE nom='taxe'");
		ps.setString(1,taxe.toString());
		return base.executeUpdateQuery(ps);
	}
	
	
	/**
	 * Permet de modifier le coef fixe enregistré en base
	 * @return : 1 ou succés sinon 0
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static int setCoefFixe(Double coefFixe) throws SQLException
	{
		ComBDD base = ContexteUtilisateur.getBaseSQL();
		PreparedStatement ps = base.getConnexion().prepareStatement("UPDATE variables SET valeur=? WHERE nom='coef_fixe'");
		ps.setString(1,coefFixe.toString());
		return base.executeUpdateQuery(ps);
	}
	
	
	/**
	 * Permet de modifier le nom d'utilisteur du logiciel
	 * @param user : le nom de l'utilisateur
	 * @return : la confirmation SQL de la modification
	 * @throws SQLException
	 */
	public static int setuser(String user) throws SQLException
	{
		ComBDD base = ContexteUtilisateur.getBaseSQL();
		PreparedStatement ps = base.getConnexion().prepareStatement("UPDATE variables SET valeur=? WHERE nom='user'");
		ps.setString(1,user);
		return base.executeUpdateQuery(ps);
	}
}
