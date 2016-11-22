package fr.quintipio.gestionChargeMon.ihm.recettes;

import java.sql.SQLException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import fr.quintipio.gestionChargeMon.contexte.ContexteUtilisateur;
import fr.quintipio.gestionChargeMon.control.AffichageRecetteControl;
import fr.quintipio.gestionChargeMon.entite.Recette;
import fr.quintipio.gestionChargeMon.exception.GestionException;
import fr.quintipio.gestionChargeMon.ihm.commun.CustomTitleBorder;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.SwingConstants;
import javax.swing.JButton;

import com.itextpdf.text.DocumentException;

/**
 * Classe de fenetre pour l'affichage des informations d'une recette
 *
 *
 */
public class AffichageRecetteFrame extends JFrame 
{

	private static final long serialVersionUID = 7519966357404966946L;

	private JPanel panelsynthese;
	private JTable tableSyntheseIngredient;
	private JScrollPane scrollPaneTableSyntheseIngredient;
	private String[] titreTableSynthese =  {"Ingr\u00E9dient", "Prix \u00E0 l'unit\u00E9", "Quantit\u00E9", "Prix Total"};
	private JLabel lblTitre, lblSyntheseCalcul;
	private JButton btnDescription, btnExport, btnFermer;
	private AffichageRecetteControl controleur;
	
	/**
	 * Constructeur de la fenetre
	 * @param frame : la fenetre parent (pour centrer l'apparition de cet objet par rapport é celle en paramétre)
	 * @param selectedRecette : la recette dont ont souhaite les infos
	 */
	public AffichageRecetteFrame(JFrame frame,Recette selectedRecette) 
	{
		//définition du controleur
		controleur = new AffichageRecetteControl(selectedRecette);
		
		
		//configuration de la fenetre
		setTitle("Calcul de "+selectedRecette.getNom());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 644, 479);
		setLocationRelativeTo(frame);
		setIconImage(Toolkit.getDefaultToolkit().getImage("ressources/logo.gif"));
		
		//Affichage de la synthese des résultats
		panelsynthese = new JPanel();
		panelsynthese.setBorder(CustomTitleBorder.getBlackBorder("Calcul de la Recette"));
		panelsynthese.setBackground(new Color(204,181,132));
		panelsynthese.setBounds(0, 0, 638, 494);
		getContentPane().add(panelsynthese);
		panelsynthese.setLayout(null);
		
		//titre
		lblTitre = new JLabel(selectedRecette.getNom());
		lblTitre.setForeground(new Color(0, 0, 0));
		lblTitre.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblTitre.setBounds(127, 11, 501, 44);
		panelsynthese.add(lblTitre);
		
		//création du tableau
		String [][] contenuTableSynthese = controleur.genereTableauSynthese(controleur.getSelectedRecette());
		tableSyntheseIngredient = new JTable(){
			private static final long serialVersionUID = -428011209206757109L;

			@Override
			public boolean isCellEditable(int row, int col){
				return false;
			}
		};
		tableSyntheseIngredient.getTableHeader().setReorderingAllowed(false);
		tableSyntheseIngredient.setModel(new DefaultTableModel(contenuTableSynthese,titreTableSynthese));
		
		scrollPaneTableSyntheseIngredient = new JScrollPane(tableSyntheseIngredient);
		scrollPaneTableSyntheseIngredient.setBounds(10, 55, 618, 179);
		panelsynthese.add(scrollPaneTableSyntheseIngredient);
		
		
		//Label des données
		String infos="";
		try
		{
			infos = controleur.genereTexteSynthese(AffichageRecetteControl.EXPORT_LABEL,controleur.getSelectedRecette());
		}
		catch(SQLException e)
		{
			ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR, "Erreur lors de la récupération des variables",e,null,this.getClass());
		}
		lblSyntheseCalcul = new JLabel(infos);
		lblSyntheseCalcul.setForeground(new Color(0, 0, 0));
		lblSyntheseCalcul.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblSyntheseCalcul.setVerticalAlignment(SwingConstants.TOP);
		lblSyntheseCalcul.setBounds(10, 245, 369, 193);
		panelsynthese.add(lblSyntheseCalcul);
		
		//création des boutons
		btnDescription = new JButton("Voir la Description");
		btnDescription.setBounds(389, 245, 239, 23);
		btnDescription.addActionListener(new EvenementButton());
		if(selectedRecette.getDescription() == null)
		{btnDescription.setEnabled(false);}
		panelsynthese.add(btnDescription);
		
		btnExport = new JButton("Exporter en PDF");
		btnExport.setBounds(389, 289, 239, 23);
		btnExport.addActionListener(new EvenementButton());
		panelsynthese.add(btnExport);
		
		btnFermer = new JButton("Fermer");
		btnFermer.setBounds(389, 415, 239, 23);
		btnFermer.addActionListener(new EvenementButton());
		panelsynthese.add(btnFermer);
		
		//affichage de la fenetre
		getContentPane().add(panelsynthese);
		setVisible(true);
	}
	
	/**
	 * Classe interne pour la gestion des événements des bouttons
	 *
	 *
	 */
	class EvenementButton implements ActionListener
	{
		JFrame frameParent;
		public EvenementButton()
		{}
		
		public EvenementButton(JFrame frameParent)
		{
			this.frameParent = frameParent;
		}
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if(e.getSource() == btnDescription)
			{
				new DescriptionRecetteFrame(frameParent, controleur.getSelectedRecette());
			}
			if(e.getSource() == btnExport)
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
				
				
				//Génere le PDF
				try 
				{
					controleur.genereUneRecettePDF(chemin,controleur.getSelectedRecette());
				} 
				catch (DocumentException e1) 
				{
					ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR, "Erreur lors de la création du PDF",e1,null,this.getClass());
				}
				catch (SQLException e1) 
				{
					ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur lors de récupération des variables en base",e1,null,this.getClass());
				} 
				catch (FileNotFoundException e1) 
				{
					ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR, "Erreur lors de la création du PDF",e1,null,this.getClass());
				}
				
			}
			
			if(e.getSource() == btnFermer)
			{
				dispose();
			}
			
		}
		
	}
}
