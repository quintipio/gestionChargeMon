package fr.quintipio.gestionChargeMon.entite;

import java.util.Hashtable;

/**
 * Entité des recettes
 *
 *
 */
public class Recette implements Comparable<Recette>
{
	private int id;
	private String nom;
	private Double prix;
	private Hashtable<Ingredient,Double> listeIngredientQuantite;
	private String description;
	
	
	public Recette(int id, String nom, Double prix,
			Hashtable<Ingredient,Double> listeIngredient) {
		super();
		this.id = id;
		this.nom = nom;
		this.prix = prix;
		this.listeIngredientQuantite = listeIngredient;
		this.description = "";
	}
	
	public Recette(int id, String nom, Double prix,
			Hashtable<Ingredient,Double> listeIngredient,String description) {
		super();
		this.id = id;
		this.nom = nom;
		this.prix = prix;
		this.listeIngredientQuantite = listeIngredient;
		this.description = description;
	}

	public Recette()
	{
		this.id = 0;
		this.nom = "";
		this.prix = 0.0;
		this.listeIngredientQuantite = new Hashtable<Ingredient,Double>();
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getNom() {
		return nom;
	}


	public void setNom(String nom) {
		this.nom = nom;
	}


	public Double getPrix() {
		return prix;
	}


	public void setPrix(Double prix) {
		this.prix = prix;
	}


	public Hashtable<Ingredient,Double> getListeIngredient() {
		return listeIngredientQuantite;
	}


	public void setListeIngredient(Hashtable<Ingredient,Double> listeIngredient) {
		this.listeIngredientQuantite = listeIngredient;
	}
	
	@Override
	public String toString()
	{return nom;}


	@Override
	public int compareTo(Recette o) 
	{
		return this.nom.compareTo(o.getNom());
	}
	
	@Override
	public boolean equals(Object anObject) 
	{
		if(anObject instanceof Recette)
		{
			if(id == ((Recette)anObject).getId())
			{return true;}
			else
			{return false;}
		}
		else
		{return false;}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
