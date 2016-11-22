package fr.quintipio.gestionChargeMon.contexte;

import fr.quintipio.gestionChargeMon.bdd.ComBDD;
import fr.quintipio.gestionChargeMon.exception.GestionException;
import fr.quintipio.gestionChargeMon.utils.DateUtils;

/**
 * Classe de Contexte permettant de mettre de coté des informations étant réutilisé dans toute l'application
 *
 */
public class ContexteUtilisateur 
{
	//les informations de connexion é la base
	private static final int typeBase = ComBDD.DB_H2;
	private static final String modebase = ComBDD.MODE_FILE;
	private static final String dbName = "gestionChargeMon";
	private static final String user="sa";
	private static final String passwd = "";
	
	//le lieu d'enregistrement des logs
	private static final String cheminLog = "log/";
	private static final String nomLog = DateUtils.format(DateUtils.getAujourdhui(), "dd-MM-yyyy")+".log";
	
	//les objets persos
	private static ComBDD baseSQL;
	private static GestionException erreur;
	
	
	public static int getTypebase() {
		return typeBase;
	}
	
	public static String getModebase() {
		return modebase;
	}
	
	public static String getDbname() {
		return dbName;
	}
	
	public static String getUser() {
		return user;
	}
	
	public static String getPasswd() {
		return passwd;
	}

	public static ComBDD getBaseSQL() {
		return baseSQL;
	}

	public static void setBaseSQL(ComBDD baseSQL) {
		ContexteUtilisateur.baseSQL = baseSQL;
	}

	public static GestionException getErreur() {
		return erreur;
	}

	public static void setErreur(GestionException erreur) {
		ContexteUtilisateur.erreur = erreur;
	}

	public static String getCheminlog() {
		return cheminLog;
	}

	public static String getNomlog() {
		return nomLog;
	}
	
	
}
