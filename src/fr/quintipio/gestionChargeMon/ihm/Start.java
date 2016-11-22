package fr.quintipio.gestionChargeMon.ihm;

import java.awt.EventQueue;

public class Start 
{

	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try 
				{
					@SuppressWarnings("unused")
					MainFen frame = new MainFen();
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

}
