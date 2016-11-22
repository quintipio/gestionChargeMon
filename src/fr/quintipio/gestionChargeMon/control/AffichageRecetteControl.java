package fr.quintipio.gestionChargeMon.control;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map.Entry;

import com.itextpdf.text.DocumentException;

import fr.quintipio.gestionChargeMon.business.RecetteBusiness;
import fr.quintipio.gestionChargeMon.business.VariableBusiness;
import fr.quintipio.gestionChargeMon.entite.Ingredient;
import fr.quintipio.gestionChargeMon.entite.Recette;
import fr.quintipio.gestionChargeMon.utils.DateUtils;
import fr.quintipio.gestionChargeMon.utils.ExporterPDF;
import fr.quintipio.gestionChargeMon.utils.MathUtils;

/**
 * Classe de controle permettant de générer les informations de calcul des charges et de générer les pdf
 *
 *
 */
public class AffichageRecetteControl 
{
	//les paramétres de type d'export pdf disponibles
	public static final int EXPORT_LABEL = 1;
	public static final int EXPORT_PDF = 2;
	
	private static final String[] colonneSynthese = {"Ingrédient","Prix é l'unité","Quantité","Prix total"};
	
	private Recette selectedRecette; //la recette selectionné dans l'ihm
	
	/**
	 * Contructeur du controleur pour l'affichage des Recettes
	 * @param selectedRecette : la recette sur laquelle le travail du controleur sera effectué
	 */
	public AffichageRecetteControl(Recette selectedRecette)
	{
		this.selectedRecette = selectedRecette;
	}
	
	/**
	 * Constructeur vide (prévu pour l'export de toute les recettes en pdf)
	 */
	public AffichageRecetteControl()
	{
		selectedRecette = null;
	}

	/**
	 * Permet de générer un tableau indiquant pour chaque ingrédient, le prix unitaire, la quantité et le prix total pour la recette en paramétre
	 * @param recette : la recette dont ont souhaite générer le tableau de synthese
	 * @return : un tableau de String contenant toute les informations é affichés
	 */
	public String[][] genereTableauSynthese(Recette recette)
	{
		
		String[][] contenuTableSynthese;
		if(recette.getListeIngredient().size() > 0)
		{contenuTableSynthese = new String[recette.getListeIngredient().size()][4];}
		else
		{contenuTableSynthese = new String[0][4];}
		int i = 0;
		for (Entry<Ingredient, Double> entry : recette.getListeIngredient().entrySet()) 
		{
			contenuTableSynthese[i][0] = entry.getKey().getNom();
			contenuTableSynthese[i][1] = MathUtils.tronquer(entry.getKey().getPrix(),2).toString()+" é";
			contenuTableSynthese[i][2] = MathUtils.tronquer(entry.getValue(),2).toString();
			Double result = MathUtils.tronquer(entry.getKey().getPrix()*entry.getValue(),2);
			contenuTableSynthese[i][3] = result.toString()+" é";
			i++;
		}
		return contenuTableSynthese;
	}
	
	/**
	 * Permet de générer le texte synthétisant toute les informations du calcul des charges
	 * @param typeExport : indiqué en variables static public EXPORT_??? du controleur pour différencier les espaces si ont exporte en pdf ou pour un label
	 * @param recette : la recette dont ont souhaite obtenir le texte de calcul des charges
	 * @return : la string contenant les phrases du calcul des charges
	 * @throws SQLException
	 */
	public String genereTexteSynthese(int typeExport,Recette recette) throws SQLException
	{
		String retourLigne;
		switch(typeExport)
		{
		case EXPORT_LABEL:
			retourLigne="<br/>";
			break;
		
		case EXPORT_PDF:
			retourLigne="\r\n";
			break;
		default :
			retourLigne="<br/>";
			break;
		}
		
		Double resultTotal = 0.0;
		for (Entry<Ingredient, Double> entry : recette.getListeIngredient().entrySet()) 
		{resultTotal += MathUtils.tronquer(entry.getKey().getPrix()*entry.getValue(),2);}
		
		Double coefMini = VariableBusiness.getCoefFixe();
		Double taxe = VariableBusiness.getTaxe();
		Double prixVendu = recette.getPrix();
		Double coutMateriel = MathUtils.tronquer(resultTotal, 2);
		Double difference = +MathUtils.tronquer(recette.getPrix() - resultTotal, 2);
		Double coefReel = MathUtils.tronquer(prixVendu/coutMateriel,2);
		Double prixVenteTheoHT = MathUtils.tronquer(coutMateriel*coefMini,2);
		Double prixVenteReelHT = MathUtils.tronquer(coutMateriel*coefReel,2);
		Double prixVenteAppliqueTTC = MathUtils.tronquer(prixVendu*taxe, 2);
		Double prixVenteTheoriqueTTC = MathUtils.tronquer(prixVenteTheoHT*taxe,2);
		Double prixVenteReelTTC = MathUtils.tronquer(prixVenteReelHT*taxe,2);
		
		StringBuilder synthese = new StringBuilder();
		if(typeExport==EXPORT_LABEL){synthese.append("<html>");}
		synthese.append("Prix vendu de la recette : "+prixVendu.toString()+" é    ");
		synthese.append("Coét Matériel : "+coutMateriel.toString()+" é ");
		synthese.append(retourLigne);
		synthese.append("Différence : "+difference+" é ");
		synthese.append(retourLigne);
		synthese.append(retourLigne);
		synthese.append("Coef Minimum : "+coefMini.toString());
		synthese.append(retourLigne);
		synthese.append("Coefficient reel : "+prixVendu.toString()+"/"+coutMateriel.toString()+" = "+coefReel.toString());
		synthese.append(retourLigne);
		synthese.append("Taxe : "+taxe.toString());
		synthese.append(retourLigne);
		synthese.append(retourLigne);
		synthese.append("Prix de vente théorique HT : "+coutMateriel.toString()+" x "+coefMini.toString()+" = "+prixVenteTheoHT.toString()+" é ");
		synthese.append(retourLigne);
		synthese.append("Prix de vente réel HT : "+coutMateriel.toString()+" x "+coefReel.toString()+" = "+prixVenteReelHT.toString()+" é ");
		synthese.append(retourLigne);
		synthese.append(retourLigne);
		synthese.append("Prix de vente appliqué TTC : "+prixVendu.toString()+" x "+taxe.toString()+" = "+prixVenteAppliqueTTC.toString()+" é ");
		synthese.append(retourLigne);
		synthese.append("Prix de vente théorique TTC : "+prixVenteTheoHT.toString()+" x "+taxe.toString()+" = "+prixVenteTheoriqueTTC.toString()+" é ");
		synthese.append(retourLigne);
		synthese.append("Prix de vente réel TTC : "+prixVenteReelHT.toString()+" x "+taxe.toString()+" = "+prixVenteReelTTC.toString()+" é ");
		if(typeExport==EXPORT_LABEL){synthese.append("</html>");}
		
		return synthese.toString();
	}
	
	
	/**
	 * Genere un fichier PDF de la recette en cours de consultation dans le chemin spécifié
	 * @param chemin : le chemin ou enregistré le fichier
	 * @param recette : la recette dont ont souhaite obtenir un PDF
	 * @throws DocumentException
	 * @throws SQLException
	 * @throws FileNotFoundException 
	 */
	public void genereUneRecettePDF (String chemin,Recette recette) throws DocumentException, SQLException, FileNotFoundException
	{
		ExporterPDF pdf = new ExporterPDF(chemin,selectedRecette.getNom()+"-"+ DateUtils.format(DateUtils.getAujourdhui(), "dd-MM-yyyy")+".pdf");
		pdf.addMetaData(selectedRecette.getNom(), "Calcul des charges", "recettes,charges",VariableBusiness.getUser() , VariableBusiness.getUser());
		pdf.addTitlePage(selectedRecette.getNom());
		pdf.createTable(colonneSynthese, genereTableauSynthese(recette), "Synthése des ingrédients", 1);
		pdf.ajouterParagrapghe(genereTexteSynthese(EXPORT_PDF,recette));
		pdf.ajouterParagrapghe(recette.getDescription());
		pdf.closeDocument();
	}
	
	/**
	 * Permet de générer un fichier PDF contenant toute les recettes en base
	 * @param chemin : le chemin complet ou  se trouve le fichier pdf
	 * @throws SQLException
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 */
	public void genereTouteRecettePDF(String chemin) throws SQLException, DocumentException, FileNotFoundException
	{
		ArrayList<Recette> listeRecette = RecetteBusiness.getAllRecette();
		
		ExporterPDF pdf = new ExporterPDF(chemin,"Toute les recettes -"+DateUtils.format(DateUtils.getAujourdhui(), "dd-MM-yyyy")+".pdf");
		pdf.addMetaData("Toute les recettes", "Calcul des charges", "recettes,charges",VariableBusiness.getUser() ,VariableBusiness.getUser());
		for (Recette recette : listeRecette) 
		{
			pdf.addTitlePage(recette.getNom());
			pdf.createTable(colonneSynthese, genereTableauSynthese(recette), "Synthése des ingrédients", 1);//bug é prévoir car la méthode travail sur selectedRecette, faire un paramétre?
			pdf.ajouterParagrapghe(genereTexteSynthese(EXPORT_PDF,recette));
			pdf.ajouterParagrapghe(recette.getDescription());
			pdf.newPage();
		}
		pdf.closeDocument();
	}
	
	
	/**GETTER ET SETTER**/
	public Recette getSelectedRecette() {
		return selectedRecette;
	}

	public void setSelectedRecette(Recette selectedRecette) {
		this.selectedRecette = selectedRecette;
	}
	
	
}
