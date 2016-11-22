package fr.quintipio.gestionChargeMon.ihm.administration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map.Entry;

import javax.swing.JPanel;
import javax.swing.JButton;

import fr.quintipio.gestionChargeMon.bdd.ComBDD;
import fr.quintipio.gestionChargeMon.business.CategorieBusiness;
import fr.quintipio.gestionChargeMon.business.IngredientBusiness;
import fr.quintipio.gestionChargeMon.business.RecetteBusiness;
import fr.quintipio.gestionChargeMon.business.UniteBusiness;
import fr.quintipio.gestionChargeMon.business.VariableBusiness;
import fr.quintipio.gestionChargeMon.contexte.ContexteUtilisateur;
import fr.quintipio.gestionChargeMon.entite.Categorie;
import fr.quintipio.gestionChargeMon.entite.Ingredient;
import fr.quintipio.gestionChargeMon.entite.Recette;
import fr.quintipio.gestionChargeMon.entite.Unite;

import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.Font;

import javax.swing.SwingConstants;

/**
 * Classe comportant le panneau pour importer la base de donéne MYSQL dans la base de donnée H2
 *
 *
 */
public class ImporterBDDPanel extends JPanel 
{

	private static final long serialVersionUID = 8818295558727756587L;
	
	private JButton btnLancerLimport, btnTestDeConnexion;
	private JLabel lblNomDeLa, lblLoginMysql, lblMotDePasse, lblTitre, lblVerifConnexion,  lblAvancementImport;
	private JTextField txtFieldDBName, txtFieldLogin, txtFieldPass;
	
	/**
	 * Constructeur
	 */
	public ImporterBDDPanel() 
	{
		//configuration du panneau
		setSize(920,595);
		setBorder(null);
		setLayout(null);
		setBackground(new Color(204,181,132));
		
		//création des labels
		lblNomDeLa = new JLabel("Nom de la base MySQL : ");
		lblNomDeLa.setBounds(10, 45, 145, 14);
		add(lblNomDeLa);
		
		lblLoginMysql = new JLabel("Login MySQL : ");
		lblLoginMysql.setBounds(10, 82, 145, 14);
		add(lblLoginMysql);
		
		lblMotDePasse = new JLabel("Mot de passe MySQL : ");
		lblMotDePasse.setBounds(10, 116, 145, 14);
		add(lblMotDePasse);
		
		lblTitre = new JLabel("Import de la base MySQL");
		lblTitre.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblTitre.setBounds(340, 21, 225, 21);
		add(lblTitre);
		
		lblVerifConnexion = new JLabel("");
		lblVerifConnexion.setVerticalAlignment(SwingConstants.TOP);
		lblVerifConnexion.setBounds(10, 190, 296, 137);
		add(lblVerifConnexion);
		
		lblAvancementImport = new JLabel("");
		lblAvancementImport.setVerticalAlignment(SwingConstants.TOP);
		lblAvancementImport.setBounds(289, 82, 460, 502);
		add(lblAvancementImport);
		
		//création des textfield
		txtFieldDBName = new JTextField("gestionChargeMon");
		txtFieldDBName.setBounds(182, 42, 86, 20);
		add(txtFieldDBName);
		txtFieldDBName.setColumns(10);
		
		txtFieldLogin = new JTextField("root");
		txtFieldLogin.setBounds(182, 79, 86, 20);
		add(txtFieldLogin);
		txtFieldLogin.setColumns(10);
		
		txtFieldPass = new JTextField("");
		txtFieldPass.setBounds(182, 113, 86, 20);
		add(txtFieldPass);
		txtFieldPass.setColumns(10);
		
		//création des boutons
		btnTestDeConnexion = new JButton("Test de connexion : ");
		btnTestDeConnexion.setBounds(10, 156, 190, 23);
		btnTestDeConnexion.addActionListener(new EvenementButton());
		add(btnTestDeConnexion);
		
		btnLancerLimport = new JButton("Lancer l'import");
		btnLancerLimport.setBounds(10, 338, 190, 23);
		btnLancerLimport.addActionListener(new EvenementButton());
		add(btnLancerLimport);
		btnLancerLimport.setEnabled(false);
		
	}
	
	/**
	 * Classe interne pour gérer les événements des boutons
	 *
	 *
	 */
	class EvenementButton implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			//pour tester la connexion é la base MySQL
			if(e.getSource() == btnTestDeConnexion)
			{
				boolean test = true;
				StringBuilder toAffich = new StringBuilder();
				toAffich.append("<html>");
				
				//deconnexion de H2
				try
				{
					ContexteUtilisateur.getBaseSQL().disconnect();
					toAffich.append("Deconnexion é H2 : OK");
				}
				catch(SQLException e1)
				{
					toAffich.append("Deconnexion é H2 : ERREUR");
					test = false;
					ContexteUtilisateur.getErreur().afficherMessage(0,null,e1,null,this.getClass());
				}
				
				toAffich.append("<br/><br/>");
				
				//connexion é MySQL
				try 
				{
					ComBDD base = ComBDD.getInstance(ComBDD.DB_MySQL,ContexteUtilisateur.getModebase(),txtFieldDBName.getText(),txtFieldLogin.getText(),txtFieldPass.getText());
					ContexteUtilisateur.setBaseSQL(base);
					toAffich.append("Connexion é MySQL : OK");
				}
				catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e1) 
				{
					toAffich.append("Connexion é MySQL : ERREUR");
					test = false;
					ContexteUtilisateur.getErreur().afficherMessage(0,null,e1,null,this.getClass());
				}
				
				toAffich.append("<br/><br/>");
				
				//Deconnexion é Mysql
				try
				{
					ContexteUtilisateur.getBaseSQL().disconnect();
					toAffich.append("Deconnexion é MySQL : OK");
				}
				catch(SQLException e1)
				{
					toAffich.append("Deconnexion é MySQL : ERREUR");
					test = false;
					ContexteUtilisateur.getErreur().afficherMessage(0,null,e1,null,this.getClass());
				}
				
				toAffich.append("<br/><br/>");
				
				//Reconnexion é H2
				try 
				{
					ComBDD base = ComBDD.getInstance(ContexteUtilisateur.getTypebase(),ContexteUtilisateur.getModebase(),ContexteUtilisateur.getDbname(),ContexteUtilisateur.getUser(),ContexteUtilisateur.getPasswd());
					ContexteUtilisateur.setBaseSQL(base);
					toAffich.append("Reconnexion é H2 : OK");
				}
				catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e1) 
				{
					toAffich.append("Reconnexion é H2 : ERREUR");
					test = false;
					ContexteUtilisateur.getErreur().afficherMessage(0,null,e1,null,this.getClass());
				}
				
				//si le test est correct, ont peut lancer l'import
				if(test)
				{btnLancerLimport.setEnabled(true);}
				else
				{btnLancerLimport.setEnabled(false);}
				toAffich.append("<br/><br/>");
				toAffich.append("</html>");
				lblVerifConnexion.setText(toAffich.toString());
				
			}
			
			
			
			if(e.getSource() == btnLancerLimport)
			{
				StringBuilder toAffich = new StringBuilder();
				boolean passe = true;
				toAffich.append("<html>Démarrage du transfert....<br/>");
				lblAvancementImport.setText(toAffich.toString());
				
				//deconnexion de H2
				try
				{
					ContexteUtilisateur.getBaseSQL().disconnect();
					toAffich.append("Deconnexion é H2 : OK");
				}
				catch(SQLException e1)
				{
					toAffich.append("Deconnexion é H2 : ERREUR");
					passe = false;
					ContexteUtilisateur.getErreur().afficherMessage(0,null,e1,null,this.getClass());
				}
				
				if(!passe)
				{
					toAffich.append("<br/><br/>");
					toAffich.append("Erreur lors de la procédure - IMPOSSIBLE DE CONTINUER");
					lblAvancementImport.setText(toAffich.toString());
					return ;
				}
				
				toAffich.append("<br/>");
				lblAvancementImport.setText(toAffich.toString());
				
				
				//connexion é MySQL
				try 
				{
					ComBDD base = ComBDD.getInstance(ComBDD.DB_MySQL,ContexteUtilisateur.getModebase(),txtFieldDBName.getText(),txtFieldLogin.getText(),txtFieldPass.getText());
					ContexteUtilisateur.setBaseSQL(base);
					toAffich.append("Connexion é MySQL : OK");
				}
				catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e1) 
				{
					toAffich.append("Connexion é MySQL : ERREUR");
					passe = false;
					ContexteUtilisateur.getErreur().afficherMessage(0,null,e1,null,this.getClass());
				}
				
				
				if(!passe)
				{
					toAffich.append("<br/><br/>");
					toAffich.append("Erreur lors de la procédure - IMPOSSIBLE DE CONTINUER");
					lblAvancementImport.setText(toAffich.toString());
					return ;
				}
				
				toAffich.append("<br/><br/>");
				lblAvancementImport.setText(toAffich.toString());
				
				
				//récupération des catégories
				ArrayList<Categorie> listeCategorie = null;
				try 
				{
					listeCategorie = CategorieBusiness.getAllCategorie();
					
					if(listeCategorie == null)
					{toAffich.append("Récupération des catégories : ERREUR");passe = false;}
					else if(listeCategorie.size() == 0)
					{toAffich.append("Récupération des catégories : CHAMPS VIDES");}
					else
					{toAffich.append("Récupération des catégories : OK");}
				} 
				catch (SQLException e2) 
				{
					toAffich.append("Récupération des catégories : ERREUR");
					passe = false;
					ContexteUtilisateur.getErreur().afficherMessage(0,null,e2,null,this.getClass());
				}
				
				
				toAffich.append("<br/>");
				lblAvancementImport.setText(toAffich.toString());
				
				
				//récupération des unités
				ArrayList<Unite> listeUnite = null;
				try 
				{
					listeUnite = UniteBusiness.getAllUniteMySQL();
					
					if(listeUnite == null)
					{toAffich.append("Récupération des unités : ERREUR");passe = false;}
					else if(listeUnite.size() == 0)
					{toAffich.append("Récupération des unités : CHAMPS VIDES");}
					else
					{toAffich.append("Récupération des unités : OK");}
				} 
				catch (SQLException e2) 
				{
					toAffich.append("Récupération des unités : ERREUR");
					passe = false;
					ContexteUtilisateur.getErreur().afficherMessage(0,null,e2,null,this.getClass());
				}
				
				
				toAffich.append("<br/>");
				lblAvancementImport.setText(toAffich.toString());
				
				//récupération des variables
				Hashtable<String,Double> listeVariables = new Hashtable<String,Double>();
				try
				{
					listeVariables.put("coef_fixe", VariableBusiness.getCoefFixe());
					listeVariables.put("taxe",VariableBusiness.getTaxe());
					
					if(listeVariables.size() == 0)
					{toAffich.append("Récupération des variables : CHAMPS VIDES");}
					else
					{toAffich.append("Récupération des variables : OK");}
				}
				catch(SQLException e2)
				{
					toAffich.append("Récupération des variables : ERREUR");
					passe = false;
					ContexteUtilisateur.getErreur().afficherMessage(0,null,e2,null,this.getClass());
				}
				
				
				toAffich.append("<br/>");
				lblAvancementImport.setText(toAffich.toString());
				
				
				//récupération des ingrédients
				ArrayList<Ingredient> listeIngredients = null;
				try
				{
					listeIngredients = IngredientBusiness.getAllIngredientMySQL();
					
					if(listeIngredients == null)
					{toAffich.append("Récupération des ingrédients : ERREUR");passe = false;}
					else if(listeIngredients.size() == 0)
					{toAffich.append("Récupération des ingrédients : CHAMPS VIDES");}
					else
					{toAffich.append("Récupération des ingrédients : OK");}
				}
				catch (SQLException e2) 
				{
					toAffich.append("Récupération des ingrédients : ERREUR");
					passe = false;
					ContexteUtilisateur.getErreur().afficherMessage(0,null,e2,null,this.getClass());
				}
				
				
				toAffich.append("<br/>");
				lblAvancementImport.setText(toAffich.toString());
				
				
				//Récupération des recettes
				ArrayList<Recette> listeRecette = null;
				try
				{
					listeRecette = RecetteBusiness.getAllRecetteMySQL();
					
					if(listeRecette == null)
					{toAffich.append("Récupération des recettes : ERREUR");passe = false;}
					else if(listeRecette.size() == 0)
					{toAffich.append("Récupération des recettes : CHAMPS VIDES");}
					else
					{toAffich.append("Récupération des recettes : OK");}
				}
				catch (SQLException e2) 
				{
					toAffich.append("Récupération des recettes : ERREUR");
					passe = false;
					ContexteUtilisateur.getErreur().afficherMessage(0,null,e2,null,this.getClass());
				}
				
				
				toAffich.append("<br/><br/>");
				lblAvancementImport.setText(toAffich.toString());
				
				if(!passe)
				{
					toAffich.append("Erreur lors de la procédure - CERTAINES DONNEES MANQUANTES - ARRET");
					lblAvancementImport.setText(toAffich.toString());
					return ;
				}
				
				
				//Deconnexion é Mysql
				try
				{
					ContexteUtilisateur.getBaseSQL().disconnect();
					toAffich.append("Deconnexion é MySQL : OK");
				}
				catch(SQLException e1)
				{
					toAffich.append("Deconnexion é MySQL : ERREUR");
					passe = false;
					ContexteUtilisateur.getErreur().afficherMessage(0,null,e1,null,this.getClass());
				}
				
				
				if(!passe)
				{
					toAffich.append("<br/><br/>");
					toAffich.append("Erreur lors de la procédure - IMPOSSIBLE DE CONTINUER");
					lblAvancementImport.setText(toAffich.toString());
					return ;
				}
				
				toAffich.append("<br/>");
				lblAvancementImport.setText(toAffich.toString());
				
				
				//Reconnexion é H2
				try 
				{
					ComBDD base = ComBDD.getInstance(ContexteUtilisateur.getTypebase(),ContexteUtilisateur.getModebase(),ContexteUtilisateur.getDbname(),ContexteUtilisateur.getUser(),ContexteUtilisateur.getPasswd());
					ContexteUtilisateur.setBaseSQL(base);
					toAffich.append("Reconnexion é H2 : OK");
				}
				catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e1) 
				{
					toAffich.append("Reconnexion é H2 : ERREUR");
					ContexteUtilisateur.getErreur().afficherMessage(0,null,e1,null,this.getClass());
				}
				
				
				if(!passe)
				{
					toAffich.append("<br/><br/>");
					toAffich.append("Erreur lors de la procédure - IMPOSSIBLE DE CONTINUER");
					lblAvancementImport.setText(toAffich.toString());
					return ;
				}
				
				toAffich.append("<br/><br/>");
				lblAvancementImport.setText(toAffich.toString());
				
				//EFFACEMENT DE H2
				try
				{
					ContexteUtilisateur.getBaseSQL().executeUpdateQuery("DELETE FROM variables WHERE  nom='coef_fixe' OR nom='taxe'");
					ContexteUtilisateur.getBaseSQL().executeUpdateQuery("DELETE FROM ref_recette");
					ContexteUtilisateur.getBaseSQL().executeUpdateQuery("DELETE FROM recette");
					ContexteUtilisateur.getBaseSQL().executeUpdateQuery("DELETE FROM ingredient");
					ContexteUtilisateur.getBaseSQL().executeUpdateQuery("DELETE FROM unite");
					ContexteUtilisateur.getBaseSQL().executeUpdateQuery("DELETE FROM ref_categorie");
					ContexteUtilisateur.getBaseSQL().executeUpdateQuery("INSERT INTO ref_categorie (id,nom) VALUES (0, 'non défini')");
					ContexteUtilisateur.getBaseSQL().executeUpdateQuery("INSERT INTO unite (id,diminutif,nom) VALUES (0,'ND', 'non défini')");
					
					toAffich.append("Effacement de la base donnée actuelle : OK");
				}
				catch(SQLException e1)
				{
					toAffich.append("Effacement de la base donnée actuelle : ERREUR");
					passe = false;
					ContexteUtilisateur.getErreur().afficherMessage(0,null,e1,null,this.getClass());
				}
				
				
				toAffich.append("<br/><br/>");
				lblAvancementImport.setText(toAffich.toString());
				
				
				
				if(passe)
				{	
					//import des variables
					passe = true;
					for (Entry<String, Double> entry : listeVariables.entrySet()) 
					{
						try 
						{
							PreparedStatement ps = ContexteUtilisateur.getBaseSQL().getConnexion().prepareStatement("INSERT INTO variables ( nom,valeur ) VALUES (?,?)");
							ps.setString(1,entry.getKey());
							ps.setDouble(2,entry.getValue());
							ContexteUtilisateur.getBaseSQL().executeUpdateQuery(ps);
							
						} 
						catch (SQLException e1) 
						{
							passe=false;
							ContexteUtilisateur.getErreur().afficherMessage(0,null,e1,null,this.getClass());
						}
					}
					
					if(passe)
					{toAffich.append("Import des variables : OK");}
					else
					{toAffich.append("Import des variables : ERREUR");}
					
					toAffich.append("<br/>");
					lblAvancementImport.setText(toAffich.toString());
					
					
					//import des catégories
					passe = true;
					for (Categorie categorie : listeCategorie) 
					{
						try 
						{
							CategorieBusiness.ajoutCategorie(categorie);
						} 
						catch (SQLException e1) 
						{
							passe = false;
							ContexteUtilisateur.getErreur().afficherMessage(0,null,e1,null,this.getClass());
						}
					}
					
					if(passe)
					{toAffich.append("Import des catégories : OK");}
					else
					{toAffich.append("Import des catégories : ERREUR");}
					
					toAffich.append("<br/>");
					lblAvancementImport.setText(toAffich.toString());
					
					
					//import des unités
					passe = true;
					for (Unite unite : listeUnite) 
					{
						try 
						{
							UniteBusiness.ajoutUnite(unite);
						} 
						catch (SQLException e1) 
						{
							passe = false;
							ContexteUtilisateur.getErreur().afficherMessage(0,null,e1,null,this.getClass());
						}
					}
					
					if(passe)
					{toAffich.append("Import des unités : OK");}
					else
					{toAffich.append("Import des unités : ERREUR");}
					
					toAffich.append("<br/>");
					lblAvancementImport.setText(toAffich.toString());
					
					
					//import des ingrédients
					passe = true;
					for (Ingredient ingredient : listeIngredients) 
					{
						try 
						{
							IngredientBusiness.ajouterIngredient(ingredient);
						} 
						catch (SQLException e1) 
						{
							passe = false;
							ContexteUtilisateur.getErreur().afficherMessage(0,null,e1,null,this.getClass());
						}
					}
					
					if(passe)
					{toAffich.append("Import des ingredients : OK");}
					else
					{toAffich.append("Import des ingrédients : ERREUR");}
					
					toAffich.append("<br/>");
					lblAvancementImport.setText(toAffich.toString());
					
					//import des recettes
					passe = true;
					for (Recette recette : listeRecette) 
					{
						try 
						{
							RecetteBusiness.insertRecette(recette);
						} 
						catch (SQLException e1) 
						{
							passe = false;
							ContexteUtilisateur.getErreur().afficherMessage(0,null,e1,null,this.getClass());
						}
					}
					
					if(passe)
					{toAffich.append("Import des recettes : OK");}
					else
					{toAffich.append("Import des recettes : ERREUR");}
					
					toAffich.append("<br/><br/>");
					lblAvancementImport.setText(toAffich.toString());
					
					
					//FIN
					toAffich.append("Import terminé");
					lblAvancementImport.setText(toAffich.toString());
				}
				
			}
			
			
			
			
		}
		
	}
}
