package fr.quintipio.gestionChargeMon.ihm.recettes;

import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import fr.quintipio.gestionChargeMon.entite.Recette;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * Fenetre permettant d'afficher la description de la recette
 *
 *
 */
public class DescriptionRecetteFrame extends JFrame 
{

	private static final long serialVersionUID = -4850884407301395791L;
	
	private JPanel contentPane;
	private JLabel lblDescription;
	private JScrollPane scrollDescription;
	private JButton btnFermer;

	/**
	 * Constructeur
	 * @param frameParent : l'objet Component demandant d'afficher cette fenetre
	 * @param selectedRecette : la recette selectionné é affiché
	 */
	public DescriptionRecetteFrame(JFrame frameParent,Recette selectedRecette) 
	{
		//configuration de la fenetre
		setTitle("Description de "+selectedRecette.getNom());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(frameParent);
		setIconImage(Toolkit.getDefaultToolkit().getImage("ressources/logo.gif"));
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(204,181,132));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		String description;
		if(selectedRecette.getDescription() == null)
			{description = "Aucune description";}
		else
		{
			description = "<html>";
			description += selectedRecette.getDescription();
			description += "</html>";
		}
		description = description.replaceAll("(\r\n|\n)","<br/>");
		lblDescription = new JLabel(description);
		lblDescription.setBackground(new Color(204,181,132));
		lblDescription.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblDescription.setVerticalAlignment(SwingConstants.TOP);
		
		scrollDescription = new JScrollPane(lblDescription);
		scrollDescription.setBackground(new Color(204,181,132));
		scrollDescription.setBounds(10, 11, 424, 213);
		contentPane.add(scrollDescription);
		
		btnFermer = new JButton("Fermer");
		btnFermer.setBounds(10, 237, 424, 23);
		btnFermer.addActionListener(new EvenementBouton());
		contentPane.add(btnFermer);
		
		setVisible(true);
	}
	
	/**
	 * Classe interne pour l'événement du bouton fermer
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
		}
		
	}

}
