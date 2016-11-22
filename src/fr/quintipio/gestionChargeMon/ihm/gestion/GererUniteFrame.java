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

import fr.quintipio.gestionChargeMon.contexte.ContexteUtilisateur;
import fr.quintipio.gestionChargeMon.exception.GestionException;

/**
 * Fenetre permettant d'ajouter ou modifier une unité en base
 *
 *
 */
public class GererUniteFrame extends JFrame
{
	public static final int MODIFIER = 1;
	public static final int AJOUTER = 2;
	
	private static final long serialVersionUID = 1813353674800682914L;
	private JPanel contentPane;
	private JTextField textFieldNom,textFieldDiminutif;
	private JButton btnAjouter,btnAnnuler;
	private JLabel lblNom,lblDiminutif;
	private GestionIngredientPanel panelParent;
	
	int actionBouton;

	/**
	 * Constructeur pour la fenetre
	 * @param panelParent : la panneau lancant la fenetre
	 * @param action : définit si l'on doit ajouter ou modifier
	 */
	public GererUniteFrame(GestionIngredientPanel panelParent,int action) 
	{

		this.panelParent = panelParent;
		actionBouton = action;
		String texte = "", nom = "", diminutif = "";
		
		if(actionBouton != 1 && actionBouton != 2)
		{actionBouton = 2;}
		
		if(actionBouton == 1 && panelParent.getControleurIngredient().getSelectedUnite() != null)
		{
			texte = "Modifier";
			nom = panelParent.getControleurIngredient().getSelectedUnite().getNom();
			diminutif = panelParent.getControleurIngredient().getSelectedUnite().getDiminutif();
		}
		if(actionBouton == 1 && panelParent.getControleurIngredient().getSelectedUnite() == null)
		{
			ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_AVERTISSEMENT,"Aucune unité de sélectionné é modifier",null,null,this.getClass());
			actionBouton = 2;
		}
		
		if(actionBouton == 2)
		{texte = "Ajouter";}
		
		setTitle("Ajouter une unit\u00E9");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 295, 138);
		setLocationRelativeTo(panelParent);
		setIconImage(Toolkit.getDefaultToolkit().getImage("ressources/logo.gif"));
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(204,181,132));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		btnAjouter = new JButton(texte);
		btnAjouter.setBounds(30, 61, 89, 23);
		btnAjouter.addActionListener(new EvenementBouton());
		contentPane.add(btnAjouter);
		
		btnAnnuler = new JButton("Annuler");
		btnAnnuler.setBounds(156, 61, 89, 23);
		btnAnnuler.addActionListener(new EvenementBouton());
		contentPane.add(btnAnnuler);
		
		lblNom = new JLabel("Nom : ");
		lblNom.setBounds(10, 21, 46, 14);
		contentPane.add(lblNom);
		
		lblDiminutif = new JLabel("Diminutif : ");
		lblDiminutif.setBounds(164, 21, 63, 14);
		contentPane.add(lblDiminutif);
		
		textFieldNom = new JTextField(nom);
		textFieldNom.setBounds(55, 18, 86, 20);
		contentPane.add(textFieldNom);
		textFieldNom.setColumns(10);
		
		textFieldDiminutif = new JTextField(diminutif);
		textFieldDiminutif.setBounds(223, 18, 46, 20);
		contentPane.add(textFieldDiminutif);
		textFieldDiminutif.setColumns(10);
	
		setVisible(true);
	}
	
	/**
	 * Classe permettant de définir les actions sur les boutons
	 *
	 *
	 */
	class EvenementBouton implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if(e.getSource() == btnAjouter)
			{
				if(actionBouton == 2)
				{
					try
					{
						if(panelParent.getControleurIngredient().ajoutUnite(textFieldNom.getText(),textFieldDiminutif.getText()))
						{
							panelParent.getUniteTable().setModel(new DefaultTableModel(panelParent.getControleurIngredient().getListeTabUnite(), panelParent.getTitreTabUnite()));
							dispose();
						}
						else
						{ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur lors de l'ajout en base de l'unité.", null,textFieldNom.getText()+" "+textFieldDiminutif.getText(),this.getClass());}
					}
					catch(SQLException ee)
					{
						ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur lors de l'ajout en base de l'unité.", ee,textFieldNom.getText()+" "+textFieldDiminutif.getText(),this.getClass());
					}
				}
				
				if(actionBouton == 1)
				{
					try
					{
						panelParent.getControleurIngredient().getSelectedUnite().setNom(textFieldNom.getText());
						panelParent.getControleurIngredient().getSelectedUnite().setDiminutif(textFieldDiminutif.getText());
						
						if(panelParent.getControleurIngredient().modifierUnite())
						{
							panelParent.getUniteTable().setModel(new DefaultTableModel(panelParent.getControleurIngredient().getListeTabUnite(), panelParent.getTitreTabUnite()));
							if(panelParent.getControleurIngredient().getSelectedIngredient() != null)
							{
								panelParent.getControleurIngredient().recupereListeIngredient();
								panelParent.getTableIngredient().setModel(new DefaultTableModel(panelParent.getControleurIngredient().getListeTabIngredient() ,panelParent.getTitreTabIngredient()));
							}
							dispose();
						}
					}
					catch(SQLException ee)
					{
						ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur lors de la modification en base de l'unité", ee,textFieldNom.getText()+" "+textFieldDiminutif.getText(),this.getClass());
					}
				}
			}
			
			
			if(e.getSource() == btnAnnuler)
			{
				 dispose();
			}
			
		}
		
	}
}
