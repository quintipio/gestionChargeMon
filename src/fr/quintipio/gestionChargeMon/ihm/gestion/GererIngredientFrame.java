package fr.quintipio.gestionChargeMon.ihm.gestion;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import fr.quintipio.gestionChargeMon.contexte.ContexteUtilisateur;
import fr.quintipio.gestionChargeMon.entite.Categorie;
import fr.quintipio.gestionChargeMon.entite.Unite;
import fr.quintipio.gestionChargeMon.exception.GestionException;
import fr.quintipio.gestionChargeMon.utils.StringUtils;


/**
 * Fenetre pour ajouter ou modifier un ingrédient
 *
 *
 */
public class GererIngredientFrame extends JFrame 
{
	public static final int MODIFIER = 1;
	public static final int AJOUTER = 2;
	
	private static final long serialVersionUID = 4424321662266051282L;
	private JPanel contentPane;
	private GestionIngredientPanel panelParent;
	private JButton ajouterBouton, annulerBouton;
	private JTextField textFieldNom,textFieldPrix;
	private JLabel lblNom,lblPrix, lblCatgorie, lblUnit, lblEuro;
	private JComboBox<Categorie> comboCategorie;
	private JComboBox<Unite> comboUnite;
	
	private int actionBouton;
	
	/**
	 * Constructeur permettant de créer la fenetre
	 * @param panelParent : le panneau appellant la fenetre
	 * @param action : définit si l'on doit ajouter ou modifier
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public GererIngredientFrame(GestionIngredientPanel panelParent,int action)
	{
		this.panelParent = panelParent;
		actionBouton = action;
		
		String texte = "";
		
		if(actionBouton != MODIFIER && actionBouton != AJOUTER)
		{actionBouton = AJOUTER;}
		
		if(actionBouton == MODIFIER && panelParent.getControleurIngredient().getSelectedIngredient() != null)
		{
			texte = "Modifier";
		}
		if(actionBouton == MODIFIER && panelParent.getControleurIngredient().getSelectedIngredient() == null)
		{
			ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_AVERTISSEMENT,"Aucun ingrédient de sélectionné é modifier",null,null,this.getClass());
			actionBouton = AJOUTER;
		}
		
		if(actionBouton == AJOUTER)
		{texte = "Ajouter";}
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Gérer un ingrédient");
		setBounds(100, 100, 360, 193);
		setLocationRelativeTo(panelParent);
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage("ressources/logo.gif"));
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		contentPane.setBackground(new Color(204,181,132));
		setContentPane(contentPane);
		
		
		ajouterBouton = new JButton(texte);
		ajouterBouton.setBounds(32, 119, 89, 23);
		ajouterBouton.addActionListener(new EvenementBouton());
		contentPane.add(ajouterBouton);
		
		annulerBouton = new JButton("Annuler");
		annulerBouton.setBounds(162, 119, 89, 23);
		annulerBouton.addActionListener(new EvenementBouton());
		contentPane.add(annulerBouton);
		
		
		lblNom = new JLabel("Nom : ");
		lblNom.setBounds(10, 29, 46, 14);
		contentPane.add(lblNom);
		
		textFieldNom = new JTextField();
		textFieldNom.setBounds(52, 26, 86, 20);
		contentPane.add(textFieldNom);
		textFieldNom.setColumns(10);
		
		lblPrix = new JLabel("Prix : ");
		lblPrix.setBounds(10, 69, 46, 14);
		contentPane.add(lblPrix);
		
		textFieldPrix = new JTextField();
		textFieldPrix.setBounds(52, 66, 46, 20);
		contentPane.add(textFieldPrix);
		textFieldPrix.setColumns(10);
		
		lblCatgorie = new JLabel("Cat\u00E9gorie : ");
		lblCatgorie.setBounds(148, 29, 71, 14);
		lblCatgorie.setVisible(false);
		contentPane.add(lblCatgorie);
		
		lblEuro = new JLabel("\u20AC");
		lblEuro.setBounds(109, 69, 16, 14);
		contentPane.add(lblEuro);
		
		comboCategorie = new JComboBox(panelParent.getControleurIngredient().getListeCategorie().toArray());
		comboCategorie.setBounds(218, 26, 133, 20);
		comboCategorie.setVisible(false);
		contentPane.add(comboCategorie);
		if(actionBouton == 1)
		{
			lblCatgorie.setVisible(true);
			comboCategorie.setVisible(true);
		}
		
		lblUnit = new JLabel("Unit\u00E9 : ");
		lblUnit.setBounds(148, 69, 46, 14);
		contentPane.add(lblUnit);
		
		comboUnite = new JComboBox(panelParent.getControleurIngredient().getListeUnite().toArray());
		comboUnite.setBounds(218, 66, 133, 20);
		contentPane.add(comboUnite);
		
		if(actionBouton == MODIFIER)
		{
			textFieldNom.setText(panelParent.getControleurIngredient().getSelectedIngredient().getNom());
			textFieldPrix.setText(panelParent.getControleurIngredient().getSelectedIngredient().getPrix().toString());
			comboUnite.setSelectedItem(panelParent.getControleurIngredient().getSelectedIngredient().getUnite());
			comboCategorie.setSelectedItem(panelParent.getControleurIngredient().getSelectedIngredient().getCategorie());
		}
		
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
			if(e.getSource() == ajouterBouton)
			{
				if(actionBouton ==AJOUTER)
				{
					if(!StringUtils.isEmpty(textFieldNom.getText()) && !StringUtils.isEmpty(textFieldPrix.getText()) && comboUnite.getSelectedItem() != null)
					{
						String nom = textFieldNom.getText().trim();
						try
						{
							Double prix = Double.parseDouble(StringUtils.changeVirgulePoint(textFieldPrix.getText()));
							Unite unite = (Unite) comboUnite.getSelectedItem();
							try 
							{
								if(panelParent.getControleurIngredient().ajouterIngredient(nom, prix, unite))
								{panelParent.getTableIngredient().setModel(new DefaultTableModel(panelParent.getControleurIngredient().getListeTabIngredient() ,panelParent.getTitreTabIngredient()));}
								else
								{ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_AVERTISSEMENT,"L'ingrédient n'a pu étre rajouté en base", null,nom +" "+prix.toString()+" "+unite.toString(),this.getClass());}
								
							} 
							catch (SQLException e1) 
							{
								ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur lors de l'ajout de l'ingrédient en base", e1,nom +" "+prix.toString()+" "+unite.toString(),this.getClass());
							}
						}
						catch(NumberFormatException e2)
						{
							ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_AVERTISSEMENT,"Erreur, le champs du prix n'est pas rempli correctement", e2,null,this.getClass());
						}
						dispose();
					}
					else
					{ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_AVERTISSEMENT,"Certains champs sont vides", null,null,this.getClass());}
				}
				
				if(actionBouton == MODIFIER)
				{
					if(!StringUtils.isEmpty(textFieldNom.getText()) && !StringUtils.isEmpty(textFieldPrix.getText()) && comboUnite.getSelectedItem() != null && comboCategorie.getSelectedItem() != null)
					{
						String nom = textFieldNom.getText().trim();
						try
						{
							boolean changementCategorie = false;
							Double prix = Double.parseDouble(StringUtils.changeVirgulePoint(textFieldPrix.getText()));
							Unite unite = (Unite) comboUnite.getSelectedItem();
							Categorie categorie = (Categorie) comboCategorie.getSelectedItem();
							
							if(!categorie.equals(panelParent.getControleurIngredient().getSelectedIngredient().getCategorie()))
							{changementCategorie = true;}
							
							panelParent.getControleurIngredient().getSelectedIngredient().setNom(nom);
							panelParent.getControleurIngredient().getSelectedIngredient().setPrix(prix);
							panelParent.getControleurIngredient().getSelectedIngredient().setCategorie(categorie);
							panelParent.getControleurIngredient().getSelectedIngredient().setUnite(unite);
							
							try 
							{
								if(panelParent.getControleurIngredient().modifierIngredient())
								{
									if(changementCategorie)
									{
										panelParent.getControleurIngredient().recupereListeIngredient();
									}
									
									panelParent.getTableIngredient().setModel(new DefaultTableModel(panelParent.getControleurIngredient().getListeTabIngredient() ,panelParent.getTitreTabIngredient()));
								}
								else
								{ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_AVERTISSEMENT,"L'ingrédient n'a pu étre modifié en base", null,nom +" "+prix.toString()+" "+unite.toString()+" "+categorie.toString(),this.getClass());}
								
							} 
							catch (SQLException e1) 
							{
								ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur lors de la modification de l'ingrédient en base", e1,nom +" "+prix.toString()+" "+unite.toString()+" "+categorie.toString(),this.getClass());
							}
						}
						catch(NumberFormatException e2)
						{
							ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_AVERTISSEMENT,"Erreur, le champs du prix n'est pas rempli correctement", e2,null,this.getClass());
						}
						dispose();
					}
				}
			}
			
			if(e.getSource() == annulerBouton)
			{
				dispose();
			}
			
		}
		
	}
}
