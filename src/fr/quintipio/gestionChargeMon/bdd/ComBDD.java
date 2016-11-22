package fr.quintipio.gestionChargeMon.bdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe Singleton permettant de se connecter é une base de donnée HSQLDB,  ,SQLite (bases embarqués) ou MySQL
 *
 */
public class ComBDD 
{
	//type de BDD é laquelle ont souhaite se connecter
	public static final int DB_HSQLDB = 1;
	public static final int DB_H2 = 2;
	public static final int DB_SQLite = 3;
	public static final int DB_MySQL = 4;
	
	//mode d'utilisation souhaité de la BDD
	public static final String MODE_FILE = "file";
	public static final String MODE_MEM = "mem";
	public static final String MODE_NONE = "";
	
	private int typeBaseConnect;	//indique quel type de BDD est en cours d'utilisation
	private Connection connexion;	//la connexion é la base
	private static ComBDD bdd;		//le singleton
	
	
	/****CONNEXION BDD****/
	
	/**
	 * Ce Getter est prévu pour préparer les objets preparedStatement
	 * @return : l'objet connecté é la base
	 */
	public Connection getConnexion() {
		return connexion;
	}
	
	/**
	 * Constructeur de la classe pour ouvrir la connexion é la base
	 * @param DatabaseName : nom de la base de donnée
	 * @param login : login de l'utilisateur
	 * @param password : mot de passe de l'utilisateur
	 * @throws SQLException
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private ComBDD(String DatabaseName,String login,String password,int typeBase,String DatabaseMode) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		if(typeBase == DB_H2)
		{
			Class.forName("org.h2.Driver");
			connexion = DriverManager.getConnection("jdbc:h2:"+DatabaseMode+":"+DatabaseName, login, password);
			typeBaseConnect = DB_H2;
		}
		
		if(typeBase == DB_HSQLDB)
		{
			Class.forName("org.hsqldb.jdbcDriver");
			connexion = DriverManager.getConnection("jdbc:hsqldb:"+DatabaseMode+":"+DatabaseName+";shutdown=true", login, password);
			typeBaseConnect = DB_HSQLDB;
		}
		
		if(typeBase == DB_SQLite)
		{
			 Class.forName("org.sqlite.JDBC");
		    connexion = DriverManager.getConnection("jdbc:sqlite:"+DatabaseName+".db3");
		    typeBase = DB_SQLite;
		}
		
		if(typeBase == DB_MySQL)
		{
			 Class.forName ("com.mysql.jdbc.Driver");
			 connexion = DriverManager.getConnection ("jdbc:mysql://localhost/"+DatabaseName+"?user="+login+"&password="+password);
			 typeBase = DB_MySQL;
		}
	}
	
	/**
	 * Retourne l'instance du Singleton
	 * @param typeBase : type de la base utilisé (les paramétres sont en public et commencent par DB.
	 * @param databaseMode : mode d'utilisation de la base de donnée. Actif uniquement pour H2 et HSQLDB. Ils sont accessible en public et commencent par MODE
	 * @param databaseName : nom de la base de donnée
	 * @param login : login de l'utilisateur
	 * @param password : mot de passe de l'utilisateur
	 * @return : retourne l'instance permettant de faire des requétes é la base
	 * @throws SQLException
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static ComBDD getInstance(int typeBase,String databaseMode,String databaseName, String login, String password) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		if(bdd==null)
		{bdd =  new ComBDD(databaseName, login, password,typeBase,databaseMode);}
		return bdd;
	}
	
	
	/**
	 * Permet de se déconnecter de la base de donnée en sauvegardant les données
	 * @throws SQLException
	 */
	public void disconnect() throws SQLException
	{
		if(bdd != null)
		{
			if(typeBaseConnect == DB_HSQLDB)
			{
				Statement statement = connexion.createStatement();
				statement.executeQuery("SHUTDOWN");
				statement.close();
			}
			connexion.close();
			bdd = null;
			typeBaseConnect = 0;
		}
	}
	
	
	/****REQUETE A LA BDD****/
	
	
	/**
	 * Permet de directement exécuter une requete de mise é jour dans la base de donnée
	 * (notamment pour la creation/modification/suppression de tables ou les insertions/modifications/suppression de données)
	 * @param requete : la requete de création
	 * @return : retourne le nombre de ligne affecté par l'opération
	 * @throws SQLException
	 */
	public int executeUpdateQuery(String requete) throws SQLException
	{
		if(bdd != null)
		{
			Statement statement = connexion.createStatement() ;
			return statement.executeUpdate(requete);
		}
		else
		{
			return -1;
		}
		
	}
	
	/**
	 * Permet d'exécuter une requete de modification préparé au préalable avec les outils de java.sql
	 * L'objet preparedStatement se créer directement é partir de l'objet de connexion é la BDD (obtenu par son getter)
	 * Ont en profite pour y inscrire la requete.
	 * Puis ont ajoute les paramétres de la requete avec ps.setString/SetDate/setInteger....
	 * @param ps : l'objet de requete préparer
	 * @return : retourne le nombre de ligne affecté par l'opération
	 * @throws SQLException 
	 */
	public int executeUpdateQuery(PreparedStatement ps) throws SQLException
	{
		if(bdd != null)
		{
			return ps.executeUpdate();
		}
		else
		{
			return -1;
		}
	}
	
	/**
	 * Permet de directement éxécuter uen requete avec une demande de résultat é la base de donnée
	 * (notamment les SELECT)
	 * @param requete : la requete é lancer
	 * @return : l'objet des résultats ou si aucun, null
	 * @throws SQLException
	 */
	public ResultSet executeQuery(String requete) throws SQLException
	{
		if(bdd != null)
		{
			Statement  statement = connexion.createStatement();
			return statement.executeQuery(requete);
		}
		else
		{
			return null;
		}
	}
	
	
	/**
	 * Permet d'exécuter une requete de selection préparé au préalable avec les outils de java.sql
	 * L'objet preparedStatement se créer directement é partir de l'objet de connexion é la BDD (obtenu par son getter)
	 * Ont en profite pour y inscrire la requete.
	 * Puis ont ajoute les paramétres de la requete avec ps.setString/SetDate/setInteger....
	 * @param ps : l'objet de requete préparé
	 * @return : retourne le résulutat de la requete sinon null
	 * @throws SQLException 
	 */
	public ResultSet executeQuery(PreparedStatement ps) throws SQLException
	{
		if(bdd != null)
		{
			return ps.executeQuery();
		}
		else
		{
			return null;
		}
	}
	
	
	/****OUTILS BDD****/
	
	
	/**
	 * retourne l'id max de la table en paramétre (la colonne doit obligatoirement s'appeler 'id'
	 * @param table : la table dont l'id max est recherché
	 * @return : l'id max de la table recherché
	 */
	public int getIdMaxTable(String table) throws SQLException
	{
		int ret = 0;
		if(bdd != null)
		{
			ResultSet r;
			Statement  statement = connexion.createStatement();
			r = statement.executeQuery("SELECT MAX(id) FROM "+table);
			if(r.next())
			{ret = r.getInt(1);}
			return ret;
		}
		else
		{return ret;}
	}	
}
