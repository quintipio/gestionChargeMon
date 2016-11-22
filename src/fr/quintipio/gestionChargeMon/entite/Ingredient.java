package fr.quintipio.gestionChargeMon.entite;

/**
 * Entité des Ingrédients
 *
 *
 */
public class Ingredient implements Comparable<Ingredient>
{
	private int id;
	private String nom;
	private Double prix;
	private Unite unite;
	private Categorie categorie;


	public Ingredient(int id, String nom, Double prix, Unite unite,
			Categorie categorie) {
		super();
		this.id = id;
		this.nom = nom;
		this.prix = prix;
		this.unite = unite;
		this.categorie = categorie;
	}
	
	public Ingredient()
	{
		this.id = 0;
		this.nom = "";
		this.prix = 0.0;
		this.unite = new Unite();
		this.categorie = new Categorie();
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


	public Unite getUnite() {
		return unite;
	}


	public void setUnite(Unite unite) {
		this.unite = unite;
	}


	public Categorie getCategorie() {
		return categorie;
	}


	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}
	
	public String toString()
	{return nom;}


	@Override
	public int compareTo(Ingredient o) 
	{
		return o.getNom().compareTo(nom);
	}
	
	@Override
	public boolean equals(Object anObject) 
	{
		if(anObject instanceof Ingredient)
		{
			if(id == ((Ingredient)anObject).getId())
			{return true;}
			else
			{return false;}
		}
		else
		{return false;}
	}
}
