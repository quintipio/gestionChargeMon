package fr.quintipio.gestionChargeMon.ihm.gestion;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import fr.quintipio.gestionChargeMon.control.GestionRecetteCtrl;

/**
 * Fenetre permettant de faire modifier la description d'une recette
 *
 *
 */
public class ModifierDescriptionFrame extends JFrame 
{
	private static final long serialVersionUID = -1722296488325996850L;
	
	private JPanel contentPane;
	private JTextArea txtDescription;
	private JScrollPane scrollDescription;
	private JButton btnFermer,btnAppliquer;
	private boolean isAjout;
	private GestionRecetteCtrl controleur;
	
	/**
	 * Constructeur
	 * @param frameParent : l'objet Component demandant d'afficher cette fenetre
	 * @param controleur : le controleur de la gestion des recettes
	 */
	public ModifierDescriptionFrame(JPanel frameParent,GestionRecetteCtrl controleur,boolean isAjout) 
	{
		//configuration de la fenetre
		setTitle("Description de "+controleur.getSelectedRecette().getNom());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(frameParent);
		setIconImage(Toolkit.getDefaultToolkit().getImage("ressources/logo.gif"));
		this.isAjout = isAjout;
		this.controleur = controleur;
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(204,181,132));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		btnAppliquer = new JButton("Appliquer");
		btnAppliquer.setBounds(10, 237, 174, 23);
		btnAppliquer.addActionListener(new EvenementBouton());
		contentPane.add(btnAppliquer);
		
		btnFermer = new JButton("Fermer");
		btnFermer.setBounds(260, 237, 174, 23);
		btnFermer.addActionListener(new EvenementBouton());
		contentPane.add(btnFermer);
		
		String description = "";
		if(controleur.getSelectedRecette().getDescription() != null)
		{description = controleur.getSelectedRecette().getDescription();}
		
		txtDescription = new JTextArea(description);
		scrollDescription = new JScrollPane(txtDescription);
		scrollDescription.setBounds(10, 11, 424, 215);
		contentPane.add(scrollDescription);
		
		setVisible(true);
	}
	
	/**
	 * Classe interne pour gérer les événements des boutons
	 *
	 *
	 */
	class EvenementBouton implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if(e.getSource() == btnFermer)
			{
				dispose();
			}
			
			if(e.getSource() == btnAppliquer)
			{
				if(isAjout)
				{
					controleur.setAjoutDescription(txtDescription.getText());
				}
				else
				{
					controleur.getSelectedRecette().setDescription(txtDescription.getText());
				}
				dispose();
			}
		}
		
	}
}
