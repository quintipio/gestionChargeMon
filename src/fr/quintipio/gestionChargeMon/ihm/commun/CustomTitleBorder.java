package fr.quintipio.gestionChargeMon.ihm.commun;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * Permet de générer un panneau personalisé avec une bordure et un titre
 *
 *
 */
public class CustomTitleBorder {

	private CustomTitleBorder(){}
	
	public static Border getBlackBorder(String title){
		return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.black, 1),
                title,
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51));
	}
}