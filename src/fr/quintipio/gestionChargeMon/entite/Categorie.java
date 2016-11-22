package fr.quintipio.gestionChargeMon.entite;

/**
 * Entité des Catégories
 *
 *
 */
public class Categorie implements Comparable<Categorie>
{
	private int id;
	private String nom;
	
	
	
	public Categorie(int id, String nom) {
		super();
		this.id = id;
		this.nom = nom;
	}

	public Categorie (int id)
	{
		this.id = id;
		this.nom = "";
	}
	
	public Categorie()
	{
		this.id = -1;
		this.nom = "";
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
	
	public String toString()
	{return nom;}

	@Override
	public int compareTo(Categorie arg0) 
	{
		return this.nom.compareTo(arg0.getNom());
	}

	@Override
	public boolean equals(Object anObject) 
	{
		if(anObject instanceof Categorie)
		{
			if(id == ((Categorie)anObject).getId())
			{return true;}
			else
			{return false;}
		}
		else
		{return false;}
	}
	
	
}
