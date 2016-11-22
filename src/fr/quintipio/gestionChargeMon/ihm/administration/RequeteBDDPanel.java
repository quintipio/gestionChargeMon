package fr.quintipio.gestionChargeMon.ihm.administration;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import fr.quintipio.gestionChargeMon.contexte.ContexteUtilisateur;
import fr.quintipio.gestionChargeMon.utils.StringUtils;

import javax.swing.SwingConstants;

/**
 * Panneau permettant d'administrer la BDD en executant des requetes
 *
 *
 */
public class RequeteBDDPanel extends JPanel 
{
	private static final long serialVersionUID = -2882579599025520828L;

	private JTextPane requeteText;
	private JScrollPane scrollText,scrollLabel;
	private JLabel lblTitre,lblResult;
	private JButton btnEnvoyer;
	
	/**
	 * Constructeur du panneau
	 */
	public RequeteBDDPanel() 
	{
		//configuration du panneau
		setSize(920,595);
		setBorder(null);
		setLayout(null);
		setBackground(new Color(204,181,132));
		
		//TextArea
		requeteText = new JTextPane();
		scrollText = new JScrollPane(requeteText);
		scrollText.setBounds(10, 44, 900, 189);
		add(scrollText);
		
		//Titre
		lblTitre = new JLabel("Administration de la Base de Donn\u00E9e");
		lblTitre.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblTitre.setBounds(320, 12, 339, 21);
		add(lblTitre);
		
		//envoyer
		btnEnvoyer = new JButton("Envoyer");
		btnEnvoyer.setBounds(386, 244, 89, 23);
		btnEnvoyer.addActionListener(new EvenementButton());
		add(btnEnvoyer);
		
		//label des Résultats
		lblResult = new JLabel();
		lblResult.setVerticalAlignment(SwingConstants.TOP);
		scrollLabel = new JScrollPane(lblResult);
		scrollLabel.setBounds(10, 291, 888, 281);
		add(scrollLabel);
	}
	
	/**
	 * Classe d'action sur le bouton
	 *
	 *
	 */
	class EvenementButton implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			//sépare en liste les requetes é partir du ;
			List<String> listeRequete = StringUtils.split(requeteText.getText(),";");
			StringBuilder toAffich = new StringBuilder();
			toAffich.append("<html>");
			for (String requete : listeRequete) 
			{
				try 
				{String truc = requete.substring(0,requete.indexOf(" "));
					if(!"SELECT".equals(truc) && !"\r\nSELECT".equals(truc))
					{
						int result = ContexteUtilisateur.getBaseSQL().executeUpdateQuery(requete);
						toAffich.append(requete+" - ok - NB de ligne affecté : "+result+"<br/>");
					}
					else
					{
						String result = "";
						ResultSet r = ContexteUtilisateur.getBaseSQL().executeQuery(requete);
						while(r.next())
						{
							result+=requete+" - ok - ";
							result+=r.toString();
							result+="<br>";
						}
						toAffich.append(result);
					}
					
				}
				catch (SQLException e)
				{
					toAffich.append(requete+" - ERREUR"+e.toString()+"<br/>");
				}
			}
			toAffich.append("</html>");
			lblResult.setText(toAffich.toString());
			
		}
		
	}
}
