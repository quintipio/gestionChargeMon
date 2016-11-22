package fr.quintipio.gestionChargeMon.control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map.Entry;

import fr.quintipio.gestionChargeMon.business.CategorieBusiness;
import fr.quintipio.gestionChargeMon.business.IngredientBusiness;
import fr.quintipio.gestionChargeMon.business.RecetteBusiness;
import fr.quintipio.gestionChargeMon.entite.Categorie;
import fr.quintipio.gestionChargeMon.entite.Ingredient;
import fr.quintipio.gestionChargeMon.entite.Recette;
import fr.quintipio.gestionChargeMon.utils.MathUtils;

/**
 * Classe permettant de controler le panneau de gestion de Recettes
 *
 *
 */
public class GestionRecetteCtrl 
{	
	//les donénes en mémoire pour l'ihm
	private ArrayList<Recette> listeRecette;
	private Recette[][] listeTabRecette;
	private Recette selectedRecette;
	
	private ArrayList<Categorie> listeCategorie;
	private Categorie[][] listeTabCategorie;
	private Categorie selectedCategorie;
	
	private Ingredient selectedIngredientNonPresent;
	private Ingredient[][] listeTabIngredientNonPresent;
	private ArrayList<Ingredient> listeIngredientNonPresent;
	
	private Ingredient selectIngredientPresent;
	private Object[][] listeTabIngredientPresent;
	private Hashtable<Ingredient,Double> listeIngredientPresent;
	
	private String ajoutDescription;
	
	/**
	 * Constructeur vide
	 */
	public GestionRecetteCtrl()
	{
		listeRecette = new ArrayList<Recette>();
		listeTabRecette = new Recette[1][1];
		selectedRecette = null;
		
		listeCategorie = new ArrayList<Categorie>();
		listeTabCategorie = new Categorie [1][1];
		selectedCategorie = new Categorie();
		
		selectedIngredientNonPresent = null;
		listeTabIngredientNonPresent = new Ingredient[1][1];
		listeIngredientNonPresent = new ArrayList<Ingredient>();
	
		selectIngredientPresent = null;
		listeTabIngredientPresent = new Object[1][1];
		listeIngredientPresent = new Hashtable<Ingredient,Double>();
	}
	
	/**
	 * Permet de récupérer une liste de toute les Recettes
	 * @throws SQLException
	 */
	public void recupereListeRecette() throws SQLException
	{
		listeRecette = RecetteBusiness.getAllRecette();
		chargerTableauRecette();
	}
	
	/**
	 * Met é jour le tableau d'affichage des recettes
	 */
	private void chargerTableauRecette()
	{
		if(listeRecette.size() > 0)
		{
			Collections.sort(listeRecette);
			listeTabRecette = new Recette[listeRecette.size()][1];
			for (int i = 0; i < listeRecette.size(); i++) 
				{listeTabRecette[i][0] = listeRecette.get(i);}
		}
		else
		{
			listeTabRecette = new Recette[1][1];
		}
	}
	
	/**
	 * Permet de récupérer toute les catégories et prépare le tableau d'affichage des catégories
	 * @throws SQLException
	 */
	public void chargerTableauCategorie() throws SQLException
	{
		listeCategorie = CategorieBusiness.getAllCategorie();
		Collections.sort(listeCategorie);
		if(listeCategorie.size()>0)
		{
			listeTabCategorie = new Categorie[listeCategorie.size()][1];
			for (int i = 0; i < listeCategorie.size(); i++) 
			{listeTabCategorie[i][0] = listeCategorie.get(i);}
		}
		else
		{
			listeTabCategorie = new Categorie[1][1];
		}
	}
	
	/**
	 * Charge la liste des ingrédients non présent dnas la recette et prépare le tableau
	 * @throws SQLException
	 */
	public void chargerIngredientNonPresent() throws SQLException
	{
		if(selectedCategorie != null && selectedCategorie.getId()!= -1)
		{
			listeIngredientNonPresent = IngredientBusiness.getIngredientCategorie(selectedCategorie);
			chargerTableauIngredientNonPresent();
		}
	}
	
	/**
	 * Permet é partir de l'ihm de recharger le tableau des ingrédients présent en recette
	 */
	public void chargerIngredientPresent()
	{
		chargerTableauIngredientPresent();
	}

	/**
	 * Charge le tableau des ingrédients non présents en ne mettant pas ceux qui sont présent dans les Ingrédients en recette
	 */
	private void chargerTableauIngredientNonPresent()
	{
		if(listeIngredientNonPresent.size() > 0)
		{
			if(listeIngredientPresent.size() == 0)
			{
				listeTabIngredientNonPresent = new Ingredient[listeIngredientNonPresent.size()][1];
				for (int i = 0; i < listeIngredientNonPresent.size(); i++)
					{listeTabIngredientNonPresent[i][0] = listeIngredientNonPresent.get(i);}
			}
			else //si il y a deja des ingredients présent en recette, one evité d'afficher ceux déjé présent
			{

				//ont compte le nombre d'ingrédient de la catégorie sélectionné déjé présent
				int ingredientCategorieDejaPresent = 0;
				for (Entry<Ingredient,Double> entry : listeIngredientPresent.entrySet()) 
				{
					if(entry.getKey().getCategorie().getId() == selectedCategorie.getId())
					{ingredientCategorieDejaPresent++;}
				}
				
				//on créer le tableau de tout les ingrédients disponible - ceux déjé présent
				listeTabIngredientNonPresent = new Ingredient[listeIngredientNonPresent.size()-ingredientCategorieDejaPresent][1];
				int tailleTableau = 0;
				int parcoursTableau = listeIngredientNonPresent.size();
				
				//puis ont parcours chaque ingrédient dispo, et pour chacun ont vérifi si il fait parti deux déjé présent en recette. Si c'est le cas, on ne les rajoute pas
				for (int i = 0; i < parcoursTableau; i++)
				{

					boolean estPresent = false;
					for (Entry<Ingredient,Double> entry : listeIngredientPresent.entrySet()) 
					{
						if(entry.getKey().getId() == listeIngredientNonPresent.get(i).getId())
							{estPresent = true;}
					}
					if(!estPresent)
					{
						listeTabIngredientNonPresent[tailleTableau][0] = listeIngredientNonPresent.get(i);
						tailleTableau++;
					}
				}
			}
		}
		else
		{
			listeTabIngredientNonPresent = new Ingredient[1][1];
		}
	}
	
	/**
	 * Permet de mettre dans un tableau adapté é un JTable la liste des ingrédients d'une recette
	 */
	private void chargerTableauIngredientPresent()
	{
		if(listeIngredientPresent.size() > 0)
		{
			listeTabIngredientPresent = new Object[listeIngredientPresent.size()][4];
			 int i = 0;
			for(Entry<Ingredient,Double> entry : listeIngredientPresent.entrySet()) 
			{
				listeTabIngredientPresent[i][0] = entry.getKey();
				listeTabIngredientPresent[i][1] = MathUtils.tronquer(entry.getValue(),2);
				listeTabIngredientPresent[i][2] = entry.getKey().getUnite();
				listeTabIngredientPresent[i][3] = entry.getKey().getPrix();
				i++;
			}
		}
		else
		{listeTabIngredientPresent = null;}
	}
	
	/**
	 * Ajoute la liste des ingrédients présent l'objet SelectedIngredientNonPresent
	 */
	public void ajouterIngredientRecette(Double quantite)
	{
		listeIngredientPresent.put(selectedIngredientNonPresent,quantite);
		chargerTableauIngredientPresent();
		chargerTableauIngredientNonPresent();
		selectedIngredientNonPresent = null;
		
	}
	
	/**
	 * Supprime un ingrédient de la recette
	 */
	public void supprimerIngredientRecette()
	{
		listeIngredientPresent.remove(selectIngredientPresent);
		chargerTableauIngredientPresent();
		chargerTableauIngredientNonPresent();
		selectIngredientPresent = null;
	}
	
	/**
	 * Supprime la recette dans selectedRecette
	 * @throws SQLException
	 */
	public void supprimerRecette() throws SQLException
	{
		RecetteBusiness.deleteRecette(selectedRecette);
		listeRecette.remove(selectedRecette);
		chargerTableauRecette();
		selectedRecette = null;
	}
	
	/**
	 * Permet d'ajouter une recette en base, retourne true si réussi sinon false
	 * @param recette : la recette é ajouter
	 * @return : true si réussi sinon false
	 * @throws SQLException
	 */
	public boolean ajouterRecette(Recette recette) throws SQLException
	{
		RecetteBusiness.insertRecette(recette);
		if(recette.getId() != 0)
		{return true;}
		else
		{return false;}
	}
	
	/**
	 * Permet de modifier une recette
	 * @throws SQLException
	 */
	public void modifierRecette() throws SQLException
	{
		RecetteBusiness.modifierRecette(selectedRecette);
	}

	
	/**GETTER ET SETTER **/
	public Recette[][] getListeTabRecette() {
		return listeTabRecette;
	}

	public void setListeTabRecette(Recette[][] listeTabRecette) {
		this.listeTabRecette = listeTabRecette;
	}

	public Recette getSelectedRecette() {
		return selectedRecette;
	}

	public void setSelectedRecette(Recette selectedRecette) {
		this.selectedRecette = selectedRecette;
	}

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

	public Ingredient[][] getListeTabIngredientNonPresent() {
		return listeTabIngredientNonPresent;
	}

	public void setListeTabIngredientNonPresent(
			Ingredient[][] listeTabIngredientNonPresent) {
		this.listeTabIngredientNonPresent = listeTabIngredientNonPresent;
	}

	public Ingredient getSelectedIngredientNonPresent() {
		return selectedIngredientNonPresent;
	}

	public void setSelectedIngredientNonPresent(
			Ingredient selectedIngredientNonPresent) {
		this.selectedIngredientNonPresent = selectedIngredientNonPresent;
	}

	public Ingredient getSelectIngredientPresent() {
		return selectIngredientPresent;
	}

	public void setSelectIngredientPresent(Ingredient selectIngredientPresent) {
		this.selectIngredientPresent = selectIngredientPresent;
	}

	public Object[][] getListeTabIngredientPresent() {
		return listeTabIngredientPresent;
	}

	public void setListeTabIngredientPresent(
			Object[][] listeTabIngredientPresent) {
		this.listeTabIngredientPresent = listeTabIngredientPresent;
	}

	public Hashtable<Ingredient, Double> getListeIngredientPresent() {
		return listeIngredientPresent;
	}

	public void setListeIngredientPresent(
			Hashtable<Ingredient, Double> listeIngredientPresent) {
		this.listeIngredientPresent = listeIngredientPresent;
	}

	public ArrayList<Ingredient> getListeIngredientNonPresent() {
		return listeIngredientNonPresent;
	}

	public void setListeIngredientNonPresent(
			ArrayList<Ingredient> listeIngredientNonPresent) {
		this.listeIngredientNonPresent = listeIngredientNonPresent;
	}

	public String getAjoutDescription() {
		return ajoutDescription;
	}

	public void setAjoutDescription(String ajoutDescription) {
		this.ajoutDescription = ajoutDescription;
	}
}
