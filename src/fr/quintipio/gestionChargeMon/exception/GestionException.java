package fr.quintipio.gestionChargeMon.exception;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import fr.quintipio.gestionChargeMon.contexte.ContexteUtilisateur;
import fr.quintipio.gestionChargeMon.utils.DateUtils;


/**
 * Classe permettant d'afficher des messages d'erreur personalisé, de loggué des messages et d'en afficher des StackTrace
 *
 *
 */
public class GestionException
{
	private String emplacement;
	
	public final static int MSG_INFORMATION = 1;
	public final static int MSG_AVERTISSEMENT = 2;
	public final static int MSG_ERREUR = 3;
	
	/**
	 * Constructeur
	 * @param chemin : indique ou doit étre enregistré le fichier de log
	 * @param fichier : indique le nom du fichier de log
	 */
	public GestionException(String chemin,String fichier)
	{
		emplacement = chemin+fichier;
	}
	/**
	 * Permet d'afficher des messages d'erreur personalisé et/ou de loggé des exceptions
	 * @param typeMSG : indique le niveau d'avertissement (mettre 0 si aucun message) sinon les donnes se trouves en static dans les attributs de cette classe MSG_???
	 * @param message : le message é afficher (null siont souaite juste faire un log
	 * @param e : l'exception déclenchant l'erreur (null si aucune)
	 * @param donnee : des donnees complémentaires é mettre en log(null si aucune)
	 * @param classe : la classe d'ou provient l'erreur (généralement un 'this.class suffit)
	 */
	public void afficherMessage(int typeMSG,String message,Exception e,String donnee,Class<?> classe)
	{
		if(typeMSG != 0)
		{
			switch(typeMSG)
			{
			case 1:
				JOptionPane.showMessageDialog(null, message,"Information", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("ressources/info.png"));
				break;
			
			case 2:
				JOptionPane.showMessageDialog(null, message,"Information", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("ressources/avertissement.png"));
				break;
			
			case 3:
				JOptionPane.showMessageDialog(null, message,"Information", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("ressources/erreur.png"));
				break;
			
			default:
				JOptionPane.showMessageDialog(null, message);
			}
		}
		
		if(donnee == null)
			{donnee = "Données non définis - message affiché : "+message;}
		ecrireLog(e,classe,donnee);
	}
	
	/**
	 * Permet de logguer une exception
	 * @param error : l'exception é logguer (null si aucune)
	 * @param classe : la classe d'ou provient l'erreur
	 * @param donnee : des données complémentaires é mettre en info dans le log (null si aucune)
	 */
	private void ecrireLog(Exception error,Class<?> classe,String donnee)
	{
		try
		{
		 
		 File fb = new File(ContexteUtilisateur.getCheminlog());
		 fb.mkdir();	
		 PrintWriter out  = new PrintWriter(new FileWriter(emplacement,true));	
		 
		 out.println("Erreur le : "+ DateUtils.formatDateHeure(DateUtils.getMaintenant())+" sur : "+donnee);
		  if(classe != null)
		 	{out.println(classe.getName());}
		 
		 if(error != null)
		 {
			 StringWriter sw = new StringWriter();
			 error.printStackTrace(new PrintWriter(sw));
			 String trace = sw.toString();
			 out.println(trace);
			 error.printStackTrace();
		 }
		 out.println("\r\n");
		 out.close();
		}
		catch(Exception e)
		{
		      e.printStackTrace();
		}
	}
	
	
	/**
	 * Permet de rajouter des informations diverses dans le log
	 * @param infos : l'info é rajouter
	 * @param classe : la classe d'ou provient l'information
	 */
	public void ajouterAuLog(String infos,Class<?> classe)
	{
		try
		{
		 
		 File fb = new File(ContexteUtilisateur.getCheminlog()); 
		 fb.mkdir();	
		 PrintWriter out  = new PrintWriter(new FileWriter(emplacement,true));	
		 
		 out.println("Le "+DateUtils.formatDateHeure(DateUtils.getMaintenant())+" :");
		  if(classe != null)
		 	{out.println(classe.getName());}
		 if(infos != null)
		 {out.println(infos);}
		
		 out.println("\r\n");
		 out.close();
		}
		catch(Exception e)
		{
		      e.printStackTrace();
		}
	}
}
