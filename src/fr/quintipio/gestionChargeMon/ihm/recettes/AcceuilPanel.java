package fr.quintipio.gestionChargeMon.ihm.recettes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JLabel;

import fr.quintipio.gestionChargeMon.business.CategorieBusiness;
import fr.quintipio.gestionChargeMon.business.IngredientBusiness;
import fr.quintipio.gestionChargeMon.business.RecetteBusiness;
import fr.quintipio.gestionChargeMon.business.UniteBusiness;
import fr.quintipio.gestionChargeMon.contexte.ContexteUtilisateur;
import fr.quintipio.gestionChargeMon.control.AffichageRecetteControl;
import fr.quintipio.gestionChargeMon.entite.Recette;
import fr.quintipio.gestionChargeMon.exception.GestionException;
import fr.quintipio.gestionChargeMon.ihm.commun.CustomTitleBorder;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JButton;

import com.itextpdf.text.DocumentException;

import javax.swing.ImageIcon;

import java.awt.Color;
/**
 * Panneau étant affiché au démarrage de l'application et permettant d'avoir les infos sur la base et de consulter les recettes
 *
 *
 */
public class AcceuilPanel extends JPanel 
{

	private static final long serialVersionUID = -2040786019840827308L;

	private JPanel statistiquesPanel,consultationRecettePanel;
	private JLabel labelStats, lblBanniere, lblIcone;
	private ArrayList<Recette> listeRecette;
	private JComboBox<Recette> comboRecette;
	private JButton btnOk, btnExportToutPDF;
	private JFrame frameParent;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AcceuilPanel(JFrame parent) 
	{
		//configuration du panneau
		setSize(920,595);
		setBorder(null);
		setLayout(null);
		setBackground(new Color(204,181,132));
		frameParent = parent;
		
		lblBanniere = new JLabel("");
		lblBanniere.setIcon(new ImageIcon("ressources/banniere.jpg"));
		lblBanniere.setBounds(21, 11, 889, 143);
		add(lblBanniere);
		
		lblIcone = new JLabel("");
		lblIcone.setIcon(new ImageIcon("ressources/logo.gif"));
		lblIcone.setBounds(49, 165, 248, 309);
		add(lblIcone);
		
		//PARTIE STATISTIQUES
		statistiquesPanel = new JPanel();
		statistiquesPanel.setBackground(new Color(173,154,116));
		statistiquesPanel.setBorder(CustomTitleBorder.getBlackBorder("Quelques chiffres..."));
		statistiquesPanel.setBounds(334, 215, 511, 89);
		add(statistiquesPanel);
		statistiquesPanel.setLayout(null);
		
		labelStats = new JLabel("");
		labelStats.setVerticalAlignment(SwingConstants.TOP);
		labelStats.setBounds(10, 25, 609, 155);
		statistiquesPanel.add(labelStats);
		 int nbCategorie = 0;
		 int nbUnite = 0;
		 int nbIngredients = 0;
		 int nbRecettes = 0;
		
		 
		 try
		{
		 nbCategorie = CategorieBusiness.compteCategories();
		 nbUnite = UniteBusiness.compteUnites();
		 nbIngredients = IngredientBusiness.compteIngredients();
		 nbRecettes = RecetteBusiness.compteRecettes();
		}
		catch(SQLException e)
		{
			ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur lors de la crécupération des statistiques",e,null,this.getClass());
		}
		
		labelStats.setText("<html>Il y a actuellement "+nbIngredients+" ingrédients répartis en "+nbCategorie+" catégories<br/>"
							+ "Il y a actuellement "+nbUnite+" unités sauvegardés en base<br/>"
							+ "Et "+nbRecettes+" recettes existantes</html>");
		
		//PARTIE CONSULTATION
		if(nbRecettes > 0)
		{
			listeRecette = null;
			try
			{listeRecette = RecetteBusiness.getAllRecette();}
			catch(SQLException e)
			{
				ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Impossible de récupérer les recettes en base",e,null,this.getClass());
			}
			
			consultationRecettePanel = new JPanel();
			consultationRecettePanel.setBorder(CustomTitleBorder.getBlackBorder("Consulter les recettes"));
			consultationRecettePanel.setBackground(new Color(173,154,116));
			consultationRecettePanel.setBounds(334, 359, 360, 115);
			add(consultationRecettePanel);
			consultationRecettePanel.setLayout(null);
			
			if(listeRecette != null)
			{comboRecette = new JComboBox(listeRecette.toArray());}
			else
			{comboRecette = new JComboBox<Recette>();}
			comboRecette.setBounds(10, 35, 241, 20);
			consultationRecettePanel.add(comboRecette);
			
			btnOk = new JButton("OK");
			btnOk.setBounds(261, 33, 89, 23);
			btnOk.addActionListener(new EvenementButton());
			consultationRecettePanel.add(btnOk);
			
			btnExportToutPDF = new JButton("Tout exporter en PDF");
			btnExportToutPDF.setBounds(10, 77, 340, 23);
			btnExportToutPDF.addActionListener(new EvenementButton());
			consultationRecettePanel.add(btnExportToutPDF);
			
			
		}
	}
	
	/**
	 * classe controlant les événements d'action des boutons
	 *
	 *
	 */
	class EvenementButton implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if(e.getSource() == btnExportToutPDF)
			{
				//Récupére le chemin ou sauvegarder le fichier, ou en cas d'erreur le met dans un dossier nommé "pdf"
				String chemin;
				JFileChooser file = new JFileChooser();
				file.setDialogTitle("Sélection du dossier de sauvegarde du PDF");
				file.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = file.showOpenDialog(null);
				
				if (returnVal == JFileChooser.APPROVE_OPTION) 
				{
		            File filea = file.getSelectedFile();
		            chemin = filea.getAbsolutePath();
		        }
				else
				{
					//Créer le répertoire de stockage s'il n'existe pas
					chemin = "pdf";
					File fb = new File(chemin); 
					fb.mkdir(); 
				}
				chemin+="/";
				
				AffichageRecetteControl controleur = new AffichageRecetteControl();
				try 
				{
					controleur.genereTouteRecettePDF(chemin);
				}
				catch (SQLException e1) 
				{
					ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur lors de la récupération de la recette",e1,null,this.getClass());
				} catch (DocumentException e1) 
				{
					ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur lors de la création du PDF",e1,null,this.getClass());
				} catch (FileNotFoundException e1) 
				{
					ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur lors de la création du PDF",e1,null,this.getClass());
				}
				
			}
			
			if(e.getSource() == btnOk)
			{
				if(comboRecette.getSelectedItem() instanceof Recette && comboRecette.getSelectedItem() != null)
				{
					new AffichageRecetteFrame(frameParent,(Recette)comboRecette.getSelectedItem());
				}
			}
			
		}
		
	}
}
