package fr.quintipio.gestionChargeMon.entite;

/**
 * Entite des unités
 *
 *
 */
public class Unite implements Comparable<Unite>
{
	private int id;
	private String diminutif;
	private String nom;
	
	
	public Unite(int id, String dimunitif, String nom) {
		super();
		this.id = id;
		this.diminutif = dimunitif;
		this.nom = nom;
	}

	public Unite (int id)
	{
		this.id = id;
		this.diminutif = "";
		this.nom = "";
	}
	
	public Unite()
	{
		id = -1;
		diminutif = "";
		nom = "";
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getDiminutif() {
		return diminutif;
	}


	public void setDiminutif(String diminutif) {
		this.diminutif = diminutif;
	}


	public String getNom() {
		return nom;
	}


	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public String toString()
	{return diminutif;}

	@Override
	public int compareTo(Unite arg0) 
	{
		return nom.compareTo(arg0.getNom());
	}
	
	@Override
	public boolean equals(Object anObject) 
	{
		if(anObject instanceof Unite)
		{
			if(id == ((Unite)anObject).getId())
			{return true;}
			else
			{return false;}
		}
		else
		{return false;}
	}
}
