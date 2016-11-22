package fr.quintipio.gestionChargeMon.ihm.gestion;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import fr.quintipio.gestionChargeMon.contexte.ContexteUtilisateur;
import fr.quintipio.gestionChargeMon.control.GestionIngredientsCtrl;
import fr.quintipio.gestionChargeMon.entite.Categorie;
import fr.quintipio.gestionChargeMon.entite.Ingredient;
import fr.quintipio.gestionChargeMon.entite.Unite;
import fr.quintipio.gestionChargeMon.exception.GestionException;
import fr.quintipio.gestionChargeMon.ihm.commun.CustomTitleBorder;

import java.awt.HeadlessException;
import java.sql.SQLException;

import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;

import java.awt.Color;

/**
 * Panneau de gestion des ingrédients, des unités et des catégories
 *
 *
 */
public class GestionIngredientPanel extends JPanel 
{
	private static final long serialVersionUID = -1674637869630163262L;

	private JPanel categoriePanel, unitePanel, ingredientPanel;
	private JScrollPane categorieScrollPane,uniteScrollPane,ingredientScrollPane;
	private JButton ajoutCategorie,supprimerCategorie, ajoutUnite, modifUnite, supprimerUnite, ajoutIngredientBouton, supprimerIngredientBouton, modifierIngredientBouton;
	private JTable categorieTable, uniteTable,tableIngredient;
	private String[] titreTabIngredient = {"Nom", "Unit\u00E9", "Prix(é)"}, titreTabCategorie = {"Cat\u00E9gories"}, titreTabUnite =  {"Nom", "Diminutif"};
	private GestionIngredientsCtrl controleur;
	
	/**
	 * Constructeur du panneau
	 */
	public GestionIngredientPanel() 
	{
		controleur = new GestionIngredientsCtrl();
		
		//configuration du panneau
		setSize(920,595);
		setBorder(null);
		setLayout(null);
		setBackground(new Color(204,181,132));
		
		/****PARTIE CATEGORIE****/
		categoriePanel = new JPanel();
		categoriePanel.setBorder(CustomTitleBorder.getBlackBorder("Catégories d'ingrédients"));
		categoriePanel.setBounds(5, 0, 257, 584);
		categoriePanel.setBackground(new Color(204,181,132));
		categoriePanel.setLayout(null);
		
		//récupération de la liste des ingrédients
		Categorie[][] liste = new Categorie[1][1];
		try
		{
			controleur.getListCategorie();
			liste = controleur.getListeTabCategorie();
		}
		catch(SQLException e)
		{
			ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_AVERTISSEMENT,"Impossible de récupérer la liste des catégories",e,null,this.getClass());
		}
		
		categorieTable = new JTable();
		
		//événement pour enregistré la catégorie de sélectionné
		categorieTable.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				if(categorieTable.getValueAt(categorieTable.getSelectedRow(),0) != null)
				{
					controleur.setSelectedCategorie((Categorie) categorieTable.getValueAt(categorieTable.getSelectedRow(),0));
					
					try
					{
						controleur.recupereListeIngredient();
						tableIngredient.setModel(new DefaultTableModel(controleur.getListeTabIngredient() ,titreTabIngredient));
						ingredientPanel.setVisible(true);
						controleur.setSelectedIngredient(null);
					} 
					catch (SQLException e) 
					{
						ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur lors de la récupération des ingrédients de cette catégorie "+categorieTable.getValueAt(categorieTable.getSelectedRow(),0),e,categorieTable.getValueAt(categorieTable.getSelectedRow(),0).toString(),this.getClass());
					}
				}
				else
				{controleur.setSelectedCategorie(null);}
			}
		});
		categorieTable.setModel(new DefaultTableModel(liste,titreTabCategorie));
		categorieTable.getModel().addTableModelListener(new MyTableModelListener(categorieTable));
		categorieTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		categorieTable.getTableHeader().setReorderingAllowed(false);
		
		categorieScrollPane = new JScrollPane(categorieTable);
		categorieScrollPane.setBounds(10, 15, 196, 562);
		categoriePanel.add(categorieScrollPane);
		
		ajoutCategorie = new JButton("");
		ajoutCategorie.setIcon(new ImageIcon("ressources/icone-plus.png"));
		ajoutCategorie.setBounds(210, 51, 40, 40);
		ajoutCategorie.addActionListener(new ActionBouton());
		categoriePanel.add(ajoutCategorie);
		
		supprimerCategorie = new JButton("");
		supprimerCategorie.setIcon(new ImageIcon("ressources/icone-moins.png"));
		supprimerCategorie.setBounds(210, 112, 40, 40);
		supprimerCategorie.addActionListener(new ActionBouton());
		categoriePanel.add(supprimerCategorie);
		
		categoriePanel.setVisible(true);
		add(categoriePanel);
		
		
		/****PARTIE UNITE****/
		
		unitePanel = new JPanel();
		unitePanel.setBorder(CustomTitleBorder.getBlackBorder("Unité des ingrédients"));
		unitePanel.setBounds(594, 11, 316, 145);
		unitePanel.setBackground(new Color(204,181,132));
		unitePanel.setLayout(null);
		add(unitePanel);
		
		uniteTable = new JTable(){
			private static final long serialVersionUID = -428011209206757109L;

			@Override
			public boolean isCellEditable(int row, int col){
				return false;
			}
		};
		uniteTable.getTableHeader().setReorderingAllowed(false);
		try 
		{
			controleur.getAllUnite();
		} 
		catch (SQLException e) 
		{
			ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur lors de la récupération des Unités en base",e,null,this.getClass());
		}
		uniteTable.setModel(new DefaultTableModel(controleur.getListeTabUnite(),titreTabUnite));
		uniteTable.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				if(uniteTable.getValueAt(uniteTable.getSelectedRow(),1) != null)
				{
					controleur.setSelectedUnite((Unite) uniteTable.getValueAt(uniteTable.getSelectedRow(),1));
				}
				else
				{controleur.setSelectedUnite(null);}
			}
		});
		uniteScrollPane = new JScrollPane(uniteTable);
		uniteScrollPane.setBounds(10, 24, 244, 113);
		unitePanel.add(uniteScrollPane);
		
		ajoutUnite = new JButton("");
		ajoutUnite.setIcon(new ImageIcon("ressources/icone-plus.png"));
		ajoutUnite.setBounds(264, 11, 40, 40);
		ajoutUnite.addActionListener(new ActionBouton(this));
		unitePanel.add(ajoutUnite);
		
		modifUnite = new JButton("");
		modifUnite.setIcon(new ImageIcon("ressources/edit.jpg"));
		modifUnite.setBounds(264, 55, 40, 40);
		modifUnite.addActionListener(new ActionBouton(this));
		unitePanel.add(modifUnite);
		
		supprimerUnite = new JButton("");
		supprimerUnite.setIcon(new ImageIcon("ressources/icone-moins.png"));
		supprimerUnite.setBounds(264, 97, 40, 40);
		supprimerUnite.addActionListener(new ActionBouton());
		unitePanel.add(supprimerUnite);
		
		
		
		/****PARTIE INGREDIENT ****/
		ingredientPanel = new JPanel();
		ingredientPanel.setBorder(CustomTitleBorder.getBlackBorder("Ingrédients de la catégorie"));
		ingredientPanel.setBounds(272, 159, 638, 425);
		ingredientPanel.setBackground(new Color(204,181,132));
		add(ingredientPanel);
		ingredientPanel.setLayout(null);
		
		ajoutIngredientBouton = new JButton("");
		ajoutIngredientBouton.setIcon(new ImageIcon("ressources/icone-plus.png"));
		ajoutIngredientBouton.setBackground(Color.WHITE);
		ajoutIngredientBouton.setBounds(10, 22, 40, 40);
		ajoutIngredientBouton.addActionListener(new ActionBouton(this));
		ingredientPanel.add(ajoutIngredientBouton);
		
		supprimerIngredientBouton = new JButton("");
		supprimerIngredientBouton.setIcon(new ImageIcon("ressources/icone-moins.png"));
		supprimerIngredientBouton.setBounds(168, 22, 40, 40);
		supprimerIngredientBouton.addActionListener(new ActionBouton());
		ingredientPanel.add(supprimerIngredientBouton);
		
		modifierIngredientBouton = new JButton("");
		modifierIngredientBouton.setForeground(Color.WHITE);
		modifierIngredientBouton.setIcon(new ImageIcon("ressources/edit.jpg"));
		modifierIngredientBouton.setBounds(88, 22, 40, 40);
		modifierIngredientBouton.addActionListener(new ActionBouton(this));
		ingredientPanel.add(modifierIngredientBouton);
		tableIngredient = new JTable(){
			private static final long serialVersionUID = -428011209206757109L;

			@Override
			public boolean isCellEditable(int row, int col){
				return false;
			}
		};
		tableIngredient.getTableHeader().setReorderingAllowed(false);
		tableIngredient.setModel(new DefaultTableModel(new Object[][] {},titreTabIngredient));
		tableIngredient.setBounds(10, 39, 618, 368);
		
		tableIngredient.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				if(tableIngredient.getValueAt(tableIngredient.getSelectedRow(),1) != null)
				{
					controleur.setSelectedIngredient((Ingredient) tableIngredient.getValueAt(tableIngredient.getSelectedRow(),0));
				}
				else
				{controleur.setSelectedUnite(null);}
			}
		});
		
		ingredientScrollPane = new JScrollPane(tableIngredient);
		ingredientScrollPane.setBounds(10, 68, 618, 346);
		ingredientPanel.setVisible(false);
		ingredientPanel.add(ingredientScrollPane);
	}
	
	/**
	 * Classe interne pour gérer les événements de modification des données de table (pour les catégories)
	 *
	 *
	 */
	class MyTableModelListener implements TableModelListener 
	{
		private JTable tableMem;
		
		public MyTableModelListener(JTable table)
		{this.tableMem = table;}
		
		//lorsuqu'une modification de la table est détecté, mise é jour dans la base
		@Override
		public void tableChanged(TableModelEvent arg0) 
		{
			try 
			{
				controleur.changeCategorie(tableMem.getValueAt(tableMem.getSelectedRow(),0).toString());
				categorieTable.setModel(new DefaultTableModel(controleur.getListeTabCategorie(),titreTabCategorie));
				categorieTable.getModel().addTableModelListener(new MyTableModelListener(categorieTable));
				controleur.recupereListeIngredient();
				tableIngredient.setModel(new DefaultTableModel(controleur.getListeTabIngredient() ,titreTabIngredient));
			} 
			catch (SQLException e) 
			{
				ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur lors de la modification de la catégorie en base",e,null,this.getClass());
			}
		}
		
	}
	
	/**
	 * Classe interne pour gérer les actions des boutons
	 *
	 *
	 */
	class ActionBouton implements ActionListener
	{
		private GestionIngredientPanel savePanelIngredient;
		
		/**
		 * Contructeur permettant aprés de retourner l'objet panel aux fenetres d'otions des unités et des ingrédients pour entre autre rafraichir le tableau
		 * @param savePanelIngredient : le panneau parent
		 */
		public ActionBouton(GestionIngredientPanel savePanelIngredient)
		{this.savePanelIngredient = savePanelIngredient;}
		
		/**
		 * Constructeur vide
		 */
		public ActionBouton()
		{}
		
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			//pour les catégories
			if(arg0.getSource() == ajoutCategorie)
			{
			 try 
			 {
				if(!controleur.ajoutCategorie(JOptionPane.showInputDialog("Entrez le nom de la nouvelle catégorie : ")))
				{ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Impossible de créer la catégorie",null,null,this.getClass());}
				else//rafraichisement du tableau
				{
					categorieTable.setModel(new DefaultTableModel(controleur.getListeTabCategorie(),titreTabCategorie));
					categorieTable.getModel().addTableModelListener(new MyTableModelListener(categorieTable));
				} 
			 } 
			 catch (SQLException | HeadlessException e)
			 {
				ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur lors de l'ajout de la catégorie en base",e,null,this.getClass());
			 }	
			}
			
			if(arg0.getSource() == supprimerCategorie)
			{
				if(JOptionPane.showConfirmDialog(null,"Supprimer cette catégorie est définitif et placera tout les ingrédients en 'Non défini'. Etes vous sér?","Confirmation",JOptionPane.YES_NO_OPTION) == 0)
				{
					try 
					{
						if(controleur.deleteCategorie())
						{
							categorieTable.setModel(new DefaultTableModel(controleur.getListeTabCategorie(),titreTabCategorie));
							categorieTable.getModel().addTableModelListener(new MyTableModelListener(categorieTable));
							ingredientPanel.setVisible(false);
						}
						else
						{ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_AVERTISSEMENT,"Impossible de supprimer cette catégorie",null,null,this.getClass());}
					} 
					catch (SQLException e) 
					{
						ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur sur la requete lors de la suppression de la Catégorie",e,null,this.getClass());
					}
				}
			}
			
			//pour les unités
			if(arg0.getSource() == ajoutUnite)
			{
				new GererUniteFrame(savePanelIngredient,GererUniteFrame.AJOUTER);
			}
			
			if(arg0.getSource() == modifUnite)
			{
				if(controleur.getSelectedUnite() != null)
					{new GererUniteFrame(savePanelIngredient, GererUniteFrame.MODIFIER);}
				else
					{ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_AVERTISSEMENT,"Aucune unité de sélectionné é modifier",null,null,this.getClass());}
			}
			
			if(arg0.getSource() == supprimerUnite)
			{
				
				try 
				{
					if(JOptionPane.showConfirmDialog(null,"Supprimer cette unité est définitif et placera tout les ingrédients en 'Non défini'. Etes vous sér?","Confirmation",JOptionPane.YES_NO_OPTION) == 0)
					{
						if(controleur.supprimerUnite())
						{
							uniteTable.setModel(new DefaultTableModel(controleur.getListeTabUnite(), titreTabUnite));
							if(controleur.getSelectedCategorie() != null)
							{
								controleur.recupereListeIngredient();
								tableIngredient.setModel(new DefaultTableModel(controleur.getListeTabIngredient() ,titreTabIngredient));
							}
						}
						else
						{
							ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_AVERTISSEMENT,"Impossible de supprimer cette unité",null,null,this.getClass());
						}
					}
				} 
				catch (SQLException e) 
				{
					ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur lors de la suppression de l'unité",e,null,this.getClass());
				}
			}
			
			
			//pour les ingrédients
			if(arg0.getSource() == ajoutIngredientBouton)
			{
				new GererIngredientFrame(savePanelIngredient,GererIngredientFrame.AJOUTER);
			}
			
			if(arg0.getSource() == modifierIngredientBouton)
			{
				if(controleur.getSelectedIngredient() != null)
				{new GererIngredientFrame(savePanelIngredient,GererIngredientFrame.MODIFIER);}
				else
					{ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_AVERTISSEMENT,"Aucun ingrédient de sélectionné é modifier",null,null,this.getClass());}
			}
			if(arg0.getSource() == supprimerIngredientBouton)
			{
				try 
				{
					if(JOptionPane.showConfirmDialog(null,"Supprimer cet ingrédient l'effacera de toute les recettes présente. Etes vous sér?","Confirmation",JOptionPane.YES_NO_OPTION) == 0)
					{
						if(!controleur.deleteIngredient())
						{ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Aucun ingrédient de sélectionné é supprimer",null,controleur.getSelectedIngredient().toString(),this.getClass());}
						else
						{
							tableIngredient.setModel(new DefaultTableModel(controleur.getListeTabIngredient() ,titreTabIngredient));
						}
					}
				} 
				catch (SQLException e) 
				{
					ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur lors de la suppression de l'ingrédient en base",e,null,this.getClass());
				}
			}
			
		}
		
	}

	
	
	/**GETTER ET SETTER**/
	public GestionIngredientsCtrl getControleurIngredient() {
		return controleur;
	}

	public void setControleurIngredient(GestionIngredientsCtrl controleurIngredient) {
		this.controleur = controleurIngredient;
	}

	public JTable getUniteTable() {
		return uniteTable;
	}

	public void setUniteTable(JTable uniteTable) {
		this.uniteTable = uniteTable;
	}

	public JTable getTableIngredient() {
		return tableIngredient;
	}

	public void setTableIngredient(JTable tableIngredient) {
		this.tableIngredient = tableIngredient;
	}

	public String[] getTitreTabIngredient() {
		return titreTabIngredient;
	}

	public String[] getTitreTabCategorie() {
		return titreTabCategorie;
	}

	public String[] getTitreTabUnite() {
		return titreTabUnite;
	}
}
