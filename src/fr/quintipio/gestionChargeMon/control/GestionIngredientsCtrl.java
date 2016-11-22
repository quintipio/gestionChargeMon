package fr.quintipio.gestionChargeMon.control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import fr.quintipio.gestionChargeMon.business.CategorieBusiness;
import fr.quintipio.gestionChargeMon.business.IngredientBusiness;
import fr.quintipio.gestionChargeMon.business.UniteBusiness;
import fr.quintipio.gestionChargeMon.entite.Categorie;
import fr.quintipio.gestionChargeMon.entite.Ingredient;
import fr.quintipio.gestionChargeMon.entite.Unite;
import fr.quintipio.gestionChargeMon.utils.StringUtils;

/**
 * Controleur pour la page de gestion des ingrédients
 *
 *
 */
public class GestionIngredientsCtrl 
{
	//données en mémoire (pour l'ihm)
	private Categorie selectedCategorie;
	private ArrayList<Categorie> listeCategorie;
	private Categorie[][] listeTabCategorie;
	
	private Unite selectedUnite;
	private ArrayList<Unite> listeUnite;
	private Object[][] listeTabUnite;
	
	private Ingredient selectedIngredient;
	private ArrayList<Ingredient> listeIngredient;
	private Object [][] listeTabIngredient;
	
	/**
	 * Constructeur
	 */
	public GestionIngredientsCtrl()
	{
		selectedCategorie = null;
		listeCategorie = new ArrayList<Categorie>();
		listeTabCategorie = new Categorie[1][1];
		selectedUnite = null;
		listeUnite = new ArrayList<Unite>();
		listeTabUnite = new Unite[1][1];
	}
	
	/**
	 * Permet de recharger le tableau des catégories affiché par la JTable en rafraichissant l'arraylist, la triant et reconstruisant le tableau
	 */
	private void chargerTabCategorie ()
	{
		Collections.sort(listeCategorie);
		listeTabCategorie = new Categorie[listeCategorie.size()][1];
		for (int i = 0; i < listeCategorie.size(); i++) 
			{listeTabCategorie[i][0] = listeCategorie.get(i);}
	}
	
	/**
	 * Permet de récupérer la liste des catégories en base
	 * @throws SQLException
	 */
	public void getListCategorie() throws SQLException
	{
		listeCategorie = CategorieBusiness.getAllCategorie();
		chargerTabCategorie();
	}
	
	
	/**
	 * Permet d'ajouter une catégorie en base
	 * @param nom : le nom de la nouvelle catégorie
	 * @return confirme la création ou non de la catégorie
	 * @throws SQLException
	 */
	public boolean ajoutCategorie(String nom) throws SQLException
	{
		if(!StringUtils.isEmpty(nom))
		{
			Categorie toAjout = new Categorie(0, nom.trim());
			CategorieBusiness.ajoutCategorie(toAjout);
			if(toAjout.getId() != 0)
			{
				listeCategorie.add(toAjout);
				chargerTabCategorie();
				return true;
			}
			else
			{return false;}
		}
		else
		{return true;}
		
	}
	
	/**
	 * Permet d'effacer la catégorie sélectionné
	 * @return confirme la suppression ou non de la catégorie
	 * @throws SQLException
	 */
	public boolean deleteCategorie() throws SQLException
	{
		if(selectedCategorie != null && selectedCategorie.getId() != 0)
		{
			CategorieBusiness.supprimerCategorie(selectedCategorie);
			listeCategorie.remove(selectedCategorie);
			selectedCategorie = null;
			chargerTabCategorie();
			return true;
		}
		else
		{return false;}	
	}
	
	/**
	 * Cette méthode permet de changer le nom d'une catégorie
	 * @param nom : le nouveaunom
	 * @throws SQLException
	 */
	public void changeCategorie(String nom) throws SQLException
	{
		//ont retrouve l'élément é modifier dans l'arraylist pour récupérer l'index de position
		int indexList = 0;
		indexList = listeCategorie.indexOf(selectedCategorie);
		//ont moidfie l'élément sélectionné
		selectedCategorie.setNom(nom);
		//ont envoi la modif é la base
		CategorieBusiness.modifierCategorie(selectedCategorie);
		if(indexList != 0)
		{
			//si ont é retrouver l'éménent dans la liste ont le modifie aussi avant de rafraichir le tableau
			listeCategorie.set(indexList, selectedCategorie);
			chargerTabCategorie();
		}
		else//si on ne retrouve pas l'élément é modifier ont recharge é partir de la base
		{getListCategorie();}
	}

	/****PARTIE UNITE****/
	/**
	 * Permet de recharger le tableau des Unites affiché é partir de l'arraylist reéu de la base et de la trier
	 */
	private void chargerTabUnite()
	{
		Collections.sort(listeUnite);
		listeTabUnite = new Object[listeUnite.size()][2];
		for (int i = 0; i < listeUnite.size(); i++) 
			{
				listeTabUnite[i][0] = listeUnite.get(i).getNom();
				listeTabUnite[i][1] = listeUnite.get(i);
			}
	}
	
	/**
	 * Récupére toute les unités en base
	 * @throws SQLException
	 */
	public void getAllUnite() throws SQLException
	{
		listeUnite = UniteBusiness.getAllUnite();
		chargerTabUnite();
	}
	
	/**
	 * Permet d'ajouter une unité en base
	 * @param nom : le nom complet de l'unité
	 * @param diminutif : le dimininutif affiché é cété de l'ingrédient
	 * @throws SQLException
	 */
	public boolean ajoutUnite(String nom,String diminutif) throws SQLException
	{
		if(!StringUtils.isEmpty(nom) && !StringUtils.isEmpty(diminutif))
		{
			Unite uniteAjout = new Unite(0, diminutif, nom);
			UniteBusiness.ajoutUnite(uniteAjout);
			if(uniteAjout.getId() != 0)
			{
				listeUnite.add(uniteAjout);
				chargerTabUnite();
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Permet de supprimer l'unité sélectionné
	 * @return : true ou flase en cas d'échec
	 * @throws SQLException
	 */
	public boolean supprimerUnite() throws SQLException
	{
		if(selectedUnite != null && selectedUnite.getId() != 0)
		{
			UniteBusiness.supprimerUnite(selectedUnite);
			listeUnite.remove(selectedUnite);
			selectedUnite = null;
			chargerTabUnite();
			return true;
		}
		else
		{return false;}
	}
	
	/**
	 * Permet de modifier l'unité sélectionné
	 * @return : true ou false en cas d'échec
	 * @throws SQLException
	 */
	public boolean modifierUnite() throws SQLException
	{
		UniteBusiness.modifierUnite(selectedUnite);
		listeUnite.remove(selectedUnite);
		listeUnite.add(selectedUnite);
		chargerTabUnite();
		return true;
	}
	
	
	/***PARTIE INGREDIENT****/
	/**
	 * Permet de récupérer de la base la liste des ingrédients de la catégorie sélectionné puis de charger les tableaux
	 * @throws SQLException
	 */
	public void recupereListeIngredient() throws SQLException
	{
		listeIngredient = IngredientBusiness.getIngredientCategorie(selectedCategorie);
		chargerTabIngredient();
		
	}
	
	/**
	 * permet de recharger la liste des ingrédients et le tableau
	 */
	private void chargerTabIngredient()
	{
		Collections.sort(listeIngredient);
		if(listeIngredient.size() > 0)
		{
			listeTabIngredient = new Object[listeIngredient.size()][3];
			for (int i = 0; i < listeIngredient.size(); i++) 
				{
				listeTabIngredient[i][0] = listeIngredient.get(i);
				listeTabIngredient[i][1] = listeIngredient.get(i).getUnite();
				listeTabIngredient[i][2] = listeIngredient.get(i).getPrix();
				}
		}
		else
		{
			listeTabIngredient = new Object[1][3];
		}
	}
	
	/**
	 * Permet de supprimer un ingrédient en base
	 * @return retourne false si raté sinon true
	 * @throws SQLException
	 */
	public boolean deleteIngredient() throws SQLException
	{
		if(selectedIngredient != null)
		{
			IngredientBusiness.supprimerIngredient(selectedIngredient);
			listeIngredient.remove(selectedIngredient);
			selectedIngredient = null;
			chargerTabIngredient();
			return true;
		}
		else
		{return false;}
	}
	/**
	 * Permet d'ajouter un ingrédient en base
	 * @param nom : le nom du nouvel ingrédient
	 * @param prix : le prix du nouvel ingrédient
	 * @param unite : l'unité du nouvel ingrédient
	 * @return : true si ajouter sinon false
	 * @throws SQLException
	 */
	public boolean ajouterIngredient(String nom, Double prix, Unite unite) throws SQLException
	{
		Ingredient ingredientAjout = new Ingredient(0,nom,prix,unite,selectedCategorie);
		IngredientBusiness.ajouterIngredient(ingredientAjout);
		if(ingredientAjout.getId() != 0)
		{
			listeIngredient.add(ingredientAjout);
			chargerTabIngredient();
			return true;
		}
		else
		{return false;}	
	}
	
	
	/**
	 * Permet de modifier l'ingrédient sélectionné avec les nouvelles informations entrées
	 * @return : true si correct sinon false
	 * @throws SQLException
	 */
	public boolean modifierIngredient() throws SQLException
	{
		IngredientBusiness.modifierIngredient(selectedIngredient);
		listeIngredient.remove(selectedIngredient);
		listeIngredient.add(selectedIngredient);
		chargerTabIngredient();
		return true;
	}
	
	/**GETTER ET SETTER **/
	public Categorie getSelectedCategorie() {
		return selectedCategorie;
	}

	public void setSelectedCategorie(Categorie selectedCategorie) {
		this.selectedCategorie = selectedCategorie;
	}

	public Categorie[][] getListeTabCategorie() {
		return listeTabCategorie;
	}

	public void setListeTabCategorie(Categorie[][] listeTabCategorie) {
		this.listeTabCategorie = listeTabCategorie;
	}

	public ArrayList<Categorie> getListeCategorie() {
		return listeCategorie;
	}

	public void setListeCategorie(ArrayList<Categorie> listeCategorie) {
		this.listeCategorie = listeCategorie;
	}

	public Unite getSelectedUnite() {
		return selectedUnite;
	}

	public void setSelectedUnite(Unite selectedUnite) {
		this.selectedUnite = selectedUnite;
	}

	public ArrayList<Unite> getListeUnite() {
		return listeUnite;
	}

	public void setListeUnite(ArrayList<Unite> listeUnite) {
		this.listeUnite = listeUnite;
	}

	public Object[][] getListeTabUnite() {
		return listeTabUnite;
	}

	public void setListeTabUnite(Object[][] listeTabUnite) {
		this.listeTabUnite = listeTabUnite;
	}

	public Ingredient getSelectedIngredient() {
		return selectedIngredient;
	}

	public void setSelectedIngredient(Ingredient selectedIngredient) {
		this.selectedIngredient = selectedIngredient;
	}

	public ArrayList<Ingredient> getListeIngredient() {
		return listeIngredient;
	}

	public void setListeIngredient(ArrayList<Ingredient> listeIngredient) {
		this.listeIngredient = listeIngredient;
	}

	public Object[][] getListeTabIngredient() {
		return listeTabIngredient;
	}

	public void setListeTabIngredient(Object[][] listeTabIngredient) {
		this.listeTabIngredient = listeTabIngredient;
	}
}
