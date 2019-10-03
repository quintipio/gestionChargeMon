package fr.quintipio.gestionChargeMon.ihm;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import fr.quintipio.gestionChargeMon.bdd.ComBDD;
import fr.quintipio.gestionChargeMon.business.UtilitaireBDD;
import fr.quintipio.gestionChargeMon.contexte.ContexteUtilisateur;
import fr.quintipio.gestionChargeMon.exception.GestionException;
import fr.quintipio.gestionChargeMon.ihm.administration.ImporterBDDPanel;
import fr.quintipio.gestionChargeMon.ihm.administration.RequeteBDDPanel;
import fr.quintipio.gestionChargeMon.ihm.gestion.GestionIngredientPanel;
import fr.quintipio.gestionChargeMon.ihm.gestion.GestionRecettePanel;
import fr.quintipio.gestionChargeMon.ihm.gestion.GestionVariablesFrame;
import fr.quintipio.gestionChargeMon.ihm.recettes.AcceuilPanel;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * Objet de la fenetre principale
 *
 *
 */
public class MainFen extends JFrame
{
	private static final long serialVersionUID = 6091928067552777005L;
	
	//objet du menu
	private JMenuBar menuBar;
	private JMenu fichier,gestion,administration;
	private JMenuItem connexion,quitter,acceuil,gestionIngredients,gestionRecette,gestionVariables,modifierBDD,importBDD;
	
	/**
	 * Constructeur de la fenetre
	 */
	public MainFen() 
	{
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) 
			{
				try 
				{
					if(ContexteUtilisateur.getBaseSQL() != null)
						{ContexteUtilisateur.getBaseSQL().disconnect();}
				} 
				catch (SQLException e) 
				{
					ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur lors de la déconnexion",e,null,this.getClass());
				}
			}
		});
		//Paramétrage de la fenétre
		setTitle("Application de calcul des charges");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize( 935, 650);
		setLocationRelativeTo(null);
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage("ressources/logo.gif"));
		getContentPane().setLayout(null);
		
		//Création du menu
		menuBar = new JMenuBar();
			fichier = new JMenu("Fichier");
				connexion = new JMenuItem("Tenter une connexion é la base de donnée");
				connexion.addActionListener(new EvenementJMenu(this));
				fichier.add(connexion);
				acceuil = new JMenuItem("Revenir é l'accueil");
				acceuil.addActionListener(new EvenementJMenu(this));
				fichier.add(acceuil);
				quitter = new JMenuItem("Quitter");
				quitter.addActionListener(new EvenementJMenu(this));
				fichier.add(quitter);
			menuBar.add(fichier);
			
			gestion = new JMenu("Gestion");
				gestionIngredients = new JMenuItem("Gérer les ingrédients");
				gestionIngredients.addActionListener(new EvenementJMenu(this));
				gestion.add(gestionIngredients);
				gestionRecette = new JMenuItem("Gérer les recettes");
				gestionRecette.addActionListener(new EvenementJMenu(this));
				gestion.add(gestionRecette);
				gestionVariables = new JMenuItem("Gérer les variables");
				gestionVariables.addActionListener(new EvenementJMenu(this));
				gestion.add(gestionVariables);
			menuBar.add(gestion);
			
			administration = new JMenu("Administration");
				modifierBDD = new JMenuItem("Modifier la base de donnée");
				modifierBDD.addActionListener(new EvenementJMenu(this));
				administration.add(modifierBDD);
				importBDD = new JMenuItem("Importer la base de l'ancienne version du logiciel");
				importBDD.addActionListener(new EvenementJMenu(this));
				administration.add(importBDD);
			menuBar.add(administration);
		setJMenuBar(menuBar);
		
		
		
		//affichage de la fenétre
		setVisible(true);
		
		//Création de la gestion des erreurs
		ContexteUtilisateur.setErreur(new GestionException(ContexteUtilisateur.getCheminlog(),ContexteUtilisateur.getNomlog()));
		
		//Démarrage de la base de donnée (vérification de la structure de la BDD puis sauvegarde de l'instance dans Contexte)
		ComBDD base = null;
		try 
		{
			base = ComBDD.getInstance(ContexteUtilisateur.getTypebase(),ContexteUtilisateur.getModebase(),ContexteUtilisateur.getDbname(),ContexteUtilisateur.getUser(),ContexteUtilisateur.getPasswd());
			UtilitaireBDD.CreationBDD(base);
			ContexteUtilisateur.setBaseSQL(base);
		} 
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e)
		{
			acceuil.setEnabled(false);
			gestion.setEnabled(false);
			administration.setEnabled(false);
			ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur lors de la connexion é la base de donnée", e, null, this.getClass());
		}
		
		if(ContexteUtilisateur.getBaseSQL() != null)
		{setContentPane(new AcceuilPanel(this));}
	}
	
	/**
	 * Classe interne pour gérer les événements du Menu
	 *
	 *
	 */
	class EvenementJMenu implements ActionListener
	{
		JFrame frame;
		
		/**
		 * Passe en paramétre la fenetre sur laquelle ont travail pour pouvoir la rafraichir lors d'un changement de panneau
		 * @param frame
		 */
		public EvenementJMenu(JFrame frame)
		{this.frame=frame;}
		
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			if(arg0.getSource() == connexion)
			{
				ComBDD base = null;
				try 
				{
					base = ComBDD.getInstance(ContexteUtilisateur.getTypebase(),ContexteUtilisateur.getModebase(),ContexteUtilisateur.getDbname(),ContexteUtilisateur.getUser(),ContexteUtilisateur.getPasswd());
					UtilitaireBDD.CreationBDD(base);
					ContexteUtilisateur.setBaseSQL(base);
					
					acceuil.setEnabled(true);
					gestion.setEnabled(true);
					administration.setEnabled(true);
					frame.setContentPane(new AcceuilPanel(frame));
					
				} 
				catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e)
				{
					acceuil.setEnabled(false);
					gestion.setEnabled(false);
					administration.setEnabled(false);
					ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur de la connexion é la base de donnée", e,null,this.getClass());
				}
			}
			
			if(arg0.getSource() == quitter)
			{
				if(JOptionPane.showConfirmDialog(null,"étes vous sur de vouloir quitter?","Confirmation",JOptionPane.YES_NO_OPTION) == 0)
				{
					if(ContexteUtilisateur.getBaseSQL() != null)
					{
						try 
						{
							ContexteUtilisateur.getBaseSQL().disconnect();
						} 
						catch (SQLException e)
						{
							ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur lors de la déconnexion de la base de donnée", e,null,this.getClass());
						}
					}
					
					System.exit(0);
				}
			}
			
			if(arg0.getSource() == acceuil)
			{
				getContentPane().removeAll();
				setContentPane(new AcceuilPanel(frame));
			}
			
			if(arg0.getSource() == gestionIngredients)
			{
				getContentPane().removeAll();
				setContentPane(new GestionIngredientPanel());
			}
			
			if(arg0.getSource() == gestionRecette)
			{
				getContentPane().removeAll();
				setContentPane(new GestionRecettePanel());
			}
			
			if(arg0.getSource() == gestionVariables)
			{
				new GestionVariablesFrame(frame);
			}
			
			if(arg0.getSource() == modifierBDD)
			{
				if(JOptionPane.showConfirmDialog(null,"Attention cet outil est EXCLUSIVEMENT réservé é une administration avancée de la base de donnée.\n Une mauvaise manipulation peut corrompre toute les informations enregistrés en base.\n Veillez é la sauvegardez avant(fichier : gestionChargeMon.h2.db)! Voulez vous continuer?","Confirmation",JOptionPane.YES_NO_OPTION) == 0)
				{
					getContentPane().removeAll();
					setContentPane(new RequeteBDDPanel());
				}
			}
			
			if(arg0.getSource() == importBDD)
			{
				if(JOptionPane.showConfirmDialog(null,"Attention cet outil est EFFACERA TOUT ce qui est présent en base sur le logiciel.\nVoulez vous continuer?","Confirmation",JOptionPane.YES_NO_OPTION) == 0)
				{
					getContentPane().removeAll();
					setContentPane(new ImporterBDDPanel());
				}
			}
			
			SwingUtilities.updateComponentTreeUI(frame);//permet de rafraichir la fenetre
		}
	}
}
