package fr.quintipio.gestionChargeMon.ihm.gestion;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import fr.quintipio.gestionChargeMon.business.VariableBusiness;
import fr.quintipio.gestionChargeMon.contexte.ContexteUtilisateur;
import fr.quintipio.gestionChargeMon.exception.GestionException;
import fr.quintipio.gestionChargeMon.utils.StringUtils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Fenetre pour modifier les variables
 *
 *
 */
public class GestionVariablesFrame extends JFrame 
{
	private static final long serialVersionUID = -4578273207919238267L;
	
	//Eléments graphiques
	private JPanel contentPane;
	private JTextField textFieldTaxe,textFieldCoef, textFieldUtilisateur;
	private JLabel lblTaxe,lblCoefficientFixe,lblTitre, lblUtilisateur;
	private JButton btnModifier,btnAnnuler;
	
	/**
	 * Constructeur 
	 * @param frameParent : la fenetre lancant l'ordre d'affichage
	 */
	public GestionVariablesFrame(JFrame frameParent) 
	{
		//réglage panneau et fenetre
		setTitle("Réglage des variables de calculs");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 280, 186);
		setLocationRelativeTo(frameParent);
		setIconImage(Toolkit.getDefaultToolkit().getImage("ressources/logo.gif"));
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(204,181,132));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//les boutons
		btnModifier = new JButton("Modifier");
		btnModifier.setBounds(20, 127, 89, 23);
		btnModifier.addActionListener(new EvenementBoutton());
		contentPane.add(btnModifier);
		
		btnAnnuler = new JButton("Annuler");
		btnAnnuler.setBounds(157, 127, 89, 23);
		btnAnnuler.addActionListener(new EvenementBoutton());
		contentPane.add(btnAnnuler);
		
		//les labels
		lblTitre = new JLabel("Modification des variables de calculs");
		lblTitre.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTitre.setBounds(10, 12, 247, 14);
		contentPane.add(lblTitre);
		
		lblTaxe = new JLabel("Taxe : ");
		lblTaxe.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblTaxe.setBounds(10, 37, 48, 14);
		contentPane.add(lblTaxe);
		
		lblCoefficientFixe = new JLabel("Coefficient fixe :");
		lblCoefficientFixe.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblCoefficientFixe.setBounds(10, 68, 112, 14);
		contentPane.add(lblCoefficientFixe);
		
		lblUtilisateur = new JLabel("Utilisateur : ");
		lblUtilisateur.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblUtilisateur.setBounds(10, 96, 89, 14);
		contentPane.add(lblUtilisateur);
		
		//les champs de texte
		textFieldTaxe = new JTextField();
		textFieldTaxe.setBounds(157, 34, 86, 20);
		textFieldTaxe.setColumns(10);
		try 
		{
			textFieldTaxe.setText(VariableBusiness.getTaxe().toString());
		} 
		catch (SQLException e) 
		{
			ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur lors de la récupération de la taxe",e,null,this.getClass());
		}
		contentPane.add(textFieldTaxe);
		
		textFieldCoef = new JTextField();
		textFieldCoef.setBounds(157, 65, 86, 20);
		textFieldCoef.setColumns(10);
		try 
		{
			textFieldCoef.setText(VariableBusiness.getCoefFixe().toString());
		} 
		catch (SQLException e) 
		{
			ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur lors de la récupération du coefficent fixe",e,null,this.getClass());
		}
		contentPane.add(textFieldCoef);
		
		
		
		textFieldUtilisateur = new JTextField();
		textFieldUtilisateur.setBounds(157, 96, 86, 20);
		try 
		{
			textFieldUtilisateur.setText(VariableBusiness.getUser());
		} 
		catch (SQLException e) 
		{
			ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur lors de la récupération de l'utilisateur",e,null,this.getClass());
		}
		textFieldUtilisateur.setColumns(10);
		contentPane.add(textFieldUtilisateur);
		
		setVisible(true);
	}
	
	/**
	 * Classe pour gérer les événements des boutons
	 *
	 *
	 */
	class EvenementBoutton implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			if(arg0.getSource() == btnAnnuler)
			{
				dispose();
				
			}
			
			if(arg0.getSource() == btnModifier)
			{
				try
				{
					Double taxe,coef;
					String user;
					taxe = Double.parseDouble(StringUtils.changeVirgulePoint(textFieldTaxe.getText()));
					coef = Double.parseDouble(StringUtils.changeVirgulePoint(textFieldCoef.getText()));
					user = StringUtils.trimToBlank(textFieldUtilisateur.getText());
					if(!StringUtils.isEmpty(textFieldTaxe.getText()) && !StringUtils.isEmpty(textFieldCoef.getText()) && !StringUtils.isEmpty(textFieldUtilisateur.getText()))
					{
						try
						{
						VariableBusiness.setCoefFixe(coef);
						VariableBusiness.setTaxe(taxe);
						VariableBusiness.setuser(user);
						dispose();
						}
						catch(SQLException e)
						{
							ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Erreur lors de la modification en base des variables",e,coef.toString()+" "+taxe.toString()+" "+user,this.getClass());
						}
					}
					else
					{ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_AVERTISSEMENT,"Erreur, un des champs est vide",null,null,this.getClass());}
					
				}
				catch(NumberFormatException e)
				{
					ContexteUtilisateur.getErreur().afficherMessage(GestionException.MSG_ERREUR,"Au moins une des variables n'est pas au bon format",e,null,this.getClass());
				}
			}
			
		}
		
	}
}
