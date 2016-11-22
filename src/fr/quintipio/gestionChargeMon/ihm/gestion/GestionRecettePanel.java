package fr.quintipio.gestionChargeMon.ihm.gestion;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

import fr.quintipio.gestionChargeMon.contexte.ContexteUtilisateur;
import fr.quintipio.gestionChargeMon.control.GestionRecetteCtrl;
import fr.quintipio.gestionChargeMon.entite.Categorie;
import fr.quintipio.gestionChargeMon.entite.Ingredient;
import fr.quintipio.gestionChargeMon.entite.Recette;
import fr.quintipio.gestionChargeMon.exception.GestionException;
import fr.quintipio.gestionChargeMon.ihm.commun.CustomTitleBorder;
import fr.quintipio.gestionChargeMon.utils.StringUtils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.ImageIcon;

/**
 * Panneau pour gérer les recettes
 *
 *
 */
public class GestionRecettePanel extends JPanel 
{
	private static final long serialVersionUID = 949719412266110843L;
	
	private GestionRecetteCtrl controleur;
	
	private JPanel listeRecettePanel,gestionRecettePanel;
	private JScrollPane listeRecetteScrollPane, ingredientNonPresentScrollPane, ingredientPresentScrollPane, listeCategorieScrollPane;
	private JTable listeRecetteTable, ingredientNonPresentTable, ingredientPresentTable,listeCategorieTable;
	private JTextField textFieldNom, textFieldQuantite,textFieldPrix;
	private JLabel lblNom, lblPrixVendu, lblEuro, lblQuantit;
	private JButton btnAjout,btnSup, btnAppliquer, btnSupRecette, btnAjoutRecette;
	
	//pour les colonnes des tableaux
	private String[] titreRecette = new String[] {"Recettes"};
	private String[] titreCategorie = new String[] {"Categorie"};
	private String[] titreIngredientDispo = new String[] {"Ingredient Disponible"};
	private String[] titreIngredientUtilise = new String[] {"Ingr\u00E9dient", "Quantit\u00E9", "Unit\u00E9", "Prix \u00E0 l'unit\u00E9 (é)"};
	
	private boolean isAjout = false; //indique si la recette est ajouter ou modifier au bouton Appliquer
	private JButton btnModifierDescription;
	
	/**
	 * Constructeur
	 */
	public GestionRecettePanel() 
	{
		//configuration du panneau
		setSize(920,595);
		setBorder(null);
		setLayout(null);
		setBackground(new Color(204,181,132));
		controleur = new GestionRecetteCtrl();
		
		
		//PARTIE RECETTE
		listeRecettePanel = new JPanel();
		listeRecettePanel.setBorder(CustomTitleBorder.getBlackBorder("Liste des Recettes"));
		listeRecettePanel.setBounds(10, 11, 250, 573);
		listeRecettePanel.setBackground(new Color(204,181,132));
		add(listeRecettePanel);
		listeRecettePanel.setLayout(null);
		
		try
		{controleur.recupereListeRecette();}
		catch(SQLException e)
		{
			ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Probléme lors de la récupération de la liste de Recettes",e,null,this.getClass());
		}
		
		listeRecetteTable = new JTable(){
			private static final long serialVersionUID = -428011209206757109L;

			@Override
			public boolean isCellEditable(int row, int col){
				return false;
			}
		};
		listeRecetteTable.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				if(listeRecetteTable.getValueAt(listeRecetteTable.getSelectedRow(),0) != null)
					{
						controleur.setSelectedRecette((Recette)listeRecetteTable.getValueAt(listeRecetteTable.getSelectedRow(),0));
						isAjout = false;
						try 
						{
							controleur.chargerTableauCategorie();
							textFieldNom.setText(controleur.getSelectedRecette().getNom());
							textFieldPrix.setText(controleur.getSelectedRecette().getPrix().toString());
							controleur.setListeIngredientPresent(controleur.getSelectedRecette().getListeIngredient());
							controleur.chargerIngredientPresent();
							listeCategorieTable.setModel(new DefaultTableModel(controleur.getListeTabCategorie() ,titreCategorie));
							ingredientPresentTable.setModel(new DefaultTableModel(controleur.getListeTabIngredientPresent(),titreIngredientUtilise));
							gestionRecettePanel.setVisible(true);
							
						} 
						catch (SQLException e1) 
						{
							ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Impossible de récupérer la liste des catégories en base",e1,null,this.getClass());
						}
					}
			}
		});
		listeRecetteTable.getTableHeader().setReorderingAllowed(false);
		listeRecetteTable.setModel(new DefaultTableModel(controleur.getListeTabRecette(),titreRecette));
		listeRecetteScrollPane = new JScrollPane(listeRecetteTable);
		listeRecetteScrollPane.setBounds(10, 24, 177, 538);
		listeRecettePanel.add(listeRecetteScrollPane);
		
		btnAjoutRecette = new JButton("");
		btnAjoutRecette.setIcon(new ImageIcon("ressources/icone-plus.png"));
		btnAjoutRecette.setBounds(197, 83, 40, 40);
		btnAjoutRecette.addActionListener(new EvenementBouton());
		listeRecettePanel.add(btnAjoutRecette);
		
		btnSupRecette = new JButton("");
		btnSupRecette.setIcon(new ImageIcon("ressources/icone-moins.png"));
		btnSupRecette.setBounds(197, 153, 40, 40);
		btnSupRecette.addActionListener(new EvenementBouton());
		listeRecettePanel.add(btnSupRecette);
		
		
		//PARTIE GESTION DE LA RECETTE
		gestionRecettePanel = new JPanel();
		gestionRecettePanel.setBorder(CustomTitleBorder.getBlackBorder("Gestion de la recette"));
		gestionRecettePanel.setBounds(273, 11, 637, 573);
		gestionRecettePanel.setBackground(new Color(204,181,132));
		add(gestionRecettePanel);
		gestionRecettePanel.setLayout(null);
		gestionRecettePanel.setVisible(false);
		
		listeCategorieTable = new JTable(){
			private static final long serialVersionUID = -428011209206757109L;

			@Override
			public boolean isCellEditable(int row, int col){
				return false;
			}
		};
		listeCategorieTable.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				if(listeCategorieTable.getValueAt(listeCategorieTable.getSelectedRow(),0) != null)
				{
					controleur.setSelectedCategorie((Categorie)listeCategorieTable.getValueAt(listeCategorieTable.getSelectedRow(),0));
					try 
					{
						controleur.chargerIngredientNonPresent();
						ingredientNonPresentTable.setModel(new DefaultTableModel(controleur.getListeTabIngredientNonPresent() ,titreIngredientDispo));
					}
					catch (SQLException e1) 
					{
						ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Impossible de récupérer la liste des ingrédients de la base",e1,null,this.getClass());
					}
				}
			}
		});
		listeCategorieTable.getTableHeader().setReorderingAllowed(false);
		listeCategorieTable.setModel(new DefaultTableModel(new Object[][] {},titreCategorie));
		listeCategorieScrollPane = new JScrollPane(listeCategorieTable);
		listeCategorieScrollPane.setBounds(10, 98, 196, 220);
		gestionRecettePanel.add(listeCategorieScrollPane);
		
		ingredientNonPresentTable = new JTable(){
			private static final long serialVersionUID = -428011209206757109L;

			@Override
			public boolean isCellEditable(int row, int col){
				return false;
			}
		};
		ingredientNonPresentTable.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				if(ingredientNonPresentTable.getValueAt(ingredientNonPresentTable.getSelectedRow(),0) != null)
					{controleur.setSelectedIngredientNonPresent((Ingredient)ingredientNonPresentTable.getValueAt(ingredientNonPresentTable.getSelectedRow(),0));}
			}
		});
		ingredientNonPresentTable.getTableHeader().setReorderingAllowed(false);
		ingredientNonPresentTable.setModel(new DefaultTableModel(new Object[][] {},titreIngredientDispo));
		ingredientNonPresentScrollPane = new JScrollPane(ingredientNonPresentTable);
		ingredientNonPresentScrollPane.setBounds(10, 330, 196, 232);
		gestionRecettePanel.add(ingredientNonPresentScrollPane);
		
		ingredientPresentTable = new JTable(){
			private static final long serialVersionUID = -428011209206757109L;

			@Override
			public boolean isCellEditable(int row, int col){
				return false;
			}
		};
		ingredientPresentTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				if(ingredientPresentTable.getValueAt(ingredientPresentTable.getSelectedRow(),0) != null)
				{controleur.setSelectIngredientPresent((Ingredient)ingredientPresentTable.getValueAt(ingredientPresentTable.getSelectedRow(),0));}
			}
		});
		ingredientPresentTable.getTableHeader().setReorderingAllowed(false);
		ingredientPresentTable.setModel(new DefaultTableModel(new Object[][] {},titreIngredientUtilise));
		ingredientPresentScrollPane = new JScrollPane(ingredientPresentTable);
		ingredientPresentScrollPane.setBounds(279, 98, 348, 464);
		gestionRecettePanel.add(ingredientPresentScrollPane);
		
		lblNom = new JLabel("Nom :");
		lblNom.setBounds(38, 32, 58, 14);
		gestionRecettePanel.add(lblNom);
		
		textFieldNom = new JTextField();
		textFieldNom.setBounds(108, 29, 138, 20);
		textFieldNom.setColumns(10);
		gestionRecettePanel.add(textFieldNom);
		
		lblPrixVendu = new JLabel("Prix vendu : ");
		lblPrixVendu.setBounds(38, 73, 88, 14);
		gestionRecettePanel.add(lblPrixVendu);
		
		lblEuro = new JLabel("\u20AC");
		lblEuro.setBounds(163, 73, 46, 14);
		gestionRecettePanel.add(lblEuro);
		
		textFieldPrix = new JTextField();
		textFieldPrix.setBounds(108, 67, 45, 20);
		textFieldPrix.setColumns(10);
		gestionRecettePanel.add(textFieldPrix);
		
		lblQuantit = new JLabel("Quantit\u00E9 : ");
		lblQuantit.setBounds(216, 415, 62, 14);
		gestionRecettePanel.add(lblQuantit);
		
		textFieldQuantite = new JTextField("");
		textFieldQuantite.setBounds(216, 440, 46, 20);
		textFieldQuantite.setColumns(10);
		gestionRecettePanel.add(textFieldQuantite);
		
		btnAjout = new JButton("");
		btnAjout.setIcon(new ImageIcon("ressources/FD.jpg"));
		btnAjout.setBounds(216, 368, 38, 34);
		btnAjout.addActionListener(new EvenementBouton());
		gestionRecettePanel.add(btnAjout);
		
		btnSup = new JButton("");
		btnSup.setIcon(new ImageIcon("ressources/FG.jpg"));
		btnSup.setBounds(220, 483, 38, 34);
		btnSup.addActionListener(new EvenementBouton());
		gestionRecettePanel.add(btnSup);
		
		btnAppliquer = new JButton("");
		btnAppliquer.setIcon(new ImageIcon("ressources/appliquer.jpg"));
		btnAppliquer.setBounds(539, 32, 50, 50);
		btnAppliquer.addActionListener(new EvenementBouton());
		gestionRecettePanel.add(btnAppliquer);
		
		btnModifierDescription = new JButton("Modifier la description");
		btnModifierDescription.setBounds(279, 47, 176, 23);
		btnModifierDescription.addActionListener(new EvenementBouton());
		gestionRecettePanel.add(btnModifierDescription);

	}
	
	/**
	 * Classe d'événement des boutons
	 *
	 *
	 */
	class EvenementBouton implements ActionListener
	{
		JPanel panelParent;
		
		public EvenementBouton()
		{}
		
		public EvenementBouton(JPanel panelParent)
		{
			this.panelParent = panelParent;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if(e.getSource() == btnModifierDescription)
			{
				new ModifierDescriptionFrame(panelParent, controleur,isAjout);
			}
			
			
			if(e.getSource() == btnAjoutRecette)
			{
				textFieldNom.setText("");
				textFieldPrix.setText("");
				textFieldQuantite.setText("");
				controleur.setListeIngredientPresent(new Hashtable<Ingredient,Double>());
				controleur.setListeIngredientNonPresent(new ArrayList<Ingredient>());
				try 
				{
					controleur.chargerTableauCategorie();
					controleur.chargerIngredientPresent();
					controleur.chargerIngredientNonPresent();
					isAjout = true;
				}
				catch (SQLException e1) 
				{
					ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur lors de la recharge des tableaux",e1,null,this.getClass());
				}
				listeCategorieTable.setModel(new DefaultTableModel(controleur.getListeTabCategorie() ,titreCategorie));
				ingredientPresentTable.setModel(new DefaultTableModel(controleur.getListeTabIngredientPresent() ,titreIngredientUtilise));
				ingredientNonPresentTable.setModel(new DefaultTableModel(controleur.getListeTabIngredientNonPresent(),titreIngredientDispo));
				gestionRecettePanel.setVisible(true);
			}
			
			if(e.getSource() ==btnSupRecette)
			{
				try 
				{
					controleur.supprimerRecette();
					gestionRecettePanel.setVisible(false);
					listeRecetteTable.setModel(new DefaultTableModel(controleur.getListeTabRecette(),titreRecette));
					
				} 
				catch (SQLException e1) 
				{
					ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Impossible de supprimer la recette en base",e1,controleur.getSelectedRecette().toString(),this.getClass());
				}
			}
			
			//ajouter un ingrédient é la recette
			if(e.getSource() == btnAjout)
			{
				if(!StringUtils.isEmpty(textFieldQuantite.getText()))
				{
					if(controleur.getSelectedIngredientNonPresent() != null)
					{
						try
						{
							Double quantite = Double.parseDouble(textFieldQuantite.getText());
							controleur.ajouterIngredientRecette(quantite);
							ingredientPresentTable.setModel(new DefaultTableModel(controleur.getListeTabIngredientPresent(),titreIngredientUtilise));
							ingredientNonPresentTable.setModel(new DefaultTableModel(controleur.getListeTabIngredientNonPresent(),titreIngredientDispo));
						}
						catch(NumberFormatException e1)
						{
							
							ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_AVERTISSEMENT,"Erreur de format de la quantité", e1,null,this.getClass());
						}
					}
					else
					{
						ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_AVERTISSEMENT,"Aucun ingrédient de sélectionné ! ",null,null,this.getClass());
					}
				}
				else
				{
					ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_AVERTISSEMENT,"Impossible d'importer l'ingrédient sans quantité ! ",null,null,this.getClass());
				}
			}
			
			//supprimer un ingrédient é la recette
			if(e.getSource() == btnSup)
			{
				if(controleur.getSelectIngredientPresent() != null)
				{
					controleur.supprimerIngredientRecette();
					ingredientPresentTable.setModel(new DefaultTableModel(controleur.getListeTabIngredientPresent(),titreIngredientUtilise));
					ingredientNonPresentTable.setModel(new DefaultTableModel(controleur.getListeTabIngredientNonPresent(),titreIngredientDispo));
				}
				else
				{
					ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_AVERTISSEMENT,"Aucun ingrédient de sélectionné é supprimer",null,null,this.getClass());
				}
			}
			
			
			//enregistrer la recette
			if(e.getSource() == btnAppliquer)
			{
				if(!StringUtils.isEmpty(textFieldNom.getText()) && !StringUtils.isEmpty(textFieldPrix.getText()) && controleur.getListeIngredientPresent().size() > 0)
				{
					if(isAjout)
					{
						try
						{
							String nom = StringUtils.trimToBlank(textFieldNom.getText());
							Double prix = Double.parseDouble(StringUtils.trimToBlank(textFieldPrix.getText()));
							Hashtable<Ingredient,Double> listeIngredient = new Hashtable<Ingredient,Double>();
							String description = controleur.getAjoutDescription();
							for (int i = 0; i < controleur.getListeTabIngredientPresent().length; i++) 
							{
								listeIngredient.put((Ingredient)controleur.getListeTabIngredientPresent()[i][0],(Double)controleur.getListeTabIngredientPresent()[i][1]);
							}
							
							controleur.ajouterRecette(new Recette(0, nom, prix, listeIngredient,description));
							controleur.recupereListeRecette();
							listeRecetteTable.setModel(new DefaultTableModel(controleur.getListeTabRecette(),titreRecette));
							
						}
						catch(NumberFormatException e1)
						{
							ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_AVERTISSEMENT,"Probléme de format",e1,null,this.getClass());
						} catch (SQLException e1) 
						{
							ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur lors de l'ajout de la recette en base",e1,null,this.getClass());
						}
					}
					else
					{
						try
						{
							String nom = StringUtils.trimToBlank(textFieldNom.getText());
							Double prix = Double.parseDouble(StringUtils.trimToBlank(textFieldPrix.getText()));
							Hashtable<Ingredient,Double> listeIngredient = new Hashtable<Ingredient,Double>();
							for (int i = 0; i < controleur.getListeTabIngredientPresent().length; i++) 
							{
								listeIngredient.put((Ingredient)controleur.getListeTabIngredientPresent()[i][0],(Double)controleur.getListeTabIngredientPresent()[i][1]);
							}
							controleur.getSelectedRecette().setNom(nom);
							controleur.getSelectedRecette().setPrix(prix);
							controleur.getSelectedRecette().setListeIngredient(listeIngredient);
							
							controleur.modifierRecette();
							controleur.recupereListeRecette();
							listeRecetteTable.setModel(new DefaultTableModel(controleur.getListeTabRecette(),titreRecette));
							
						}
						catch(NumberFormatException e1)
						{
							ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_AVERTISSEMENT,"Probléme de format",e1,null,this.getClass());
						} catch (SQLException e1) 
						{
							ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur lors de l'ajout de la recette en base",e1,null,this.getClass());
						}
					}
				}
				else
				{
					ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_INFORMATION,"Toutes les informations nécéssaires ne sont pas présentent",null,null,this.getClass());
				}
			}
		}
		
	}
}
