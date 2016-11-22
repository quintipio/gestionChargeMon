package fr.quintipio.gestionChargeMon.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Map.Entry;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Permet de sortir un fichier PDF é partir des données fournis
 *
 *
 */
public class ExporterPDF
{
	
	private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	@SuppressWarnings("unused")
	private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL, BaseColor.RED);
	private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,Font.BOLD);
	@SuppressWarnings("unused")
	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
	private static Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
	
	private String FILE;
	private Document document;
	
	/**
	 * Constructeur
	 * @param folder : enplacement du fichier é créer (avec / é la fin)
	 * @param file : nom du fichier avec l'extension
	 * @throws FileNotFoundException
	 * @throws DocumentException
	 */
	public ExporterPDF(String folder,String file) throws FileNotFoundException, DocumentException
	{
		FILE = folder+file;
		document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(FILE));
		document.open();
		
	}
	
	/**
	 * Permet d'accéder au document sur lequel ont travail
	 * @return : le document en cours d'utilisation
	 */
	public Document getDocument()
	{
		return document;
	}
	
	/**
	 * Permet de fermer le document de travail
	 */
	public void closeDocument()
	{
		document.close();
	}
	
	/**
	 * Ajoute les métadonnées du fichier
	 * @param title : le titre du fichier
	 * @param subject : son sujet
	 * @param keywords : les motcs clés
	 * @param author : l'auteur
	 * @param creator : le créateur
	 */
	public void addMetaData(String title, String subject, String keywords,String author,String creator)
	{
	    document.addTitle(title);
	    document.addSubject(subject);
	    document.addKeywords(keywords);
	    document.addAuthor(author);
	    document.addCreator(creator);
	}
	
	/**
	 * Ajoute un titre de page
	 * @param title : le titre
	 * @throws DocumentException
	 */
	public void addTitlePage(String title) throws DocumentException
	{
	    Paragraph preface = new Paragraph();
	    preface.setAlignment(Element.ALIGN_MIDDLE);
	    addEmptyLine(preface, 1);
	    preface.add(new Paragraph(title, catFont));
	    addEmptyLine(preface, 2);
	    document.add(preface);
	 }
	
	/**
	 * Permet d'ajouter un nombre de saut de ligne  é un pragraphe
	 * @param paragraph : le pragraphe
	 * @param number : le nombre de retour é la ligne
	 */
	private void addEmptyLine(Paragraph paragraph, int number) 
    {
      for (int i = 0; i < number; i++) 
      {paragraph.add(new Paragraph(" "));}
    }
	
	/**
	 * Permet de créer un pragraphe é partir d'un texte
	 * @param texte : le texte é mettre dans le parapgraphe
	 * @throws DocumentException
	 */
	public void ajouterParagrapghe(String texte) throws DocumentException
	{
		Paragraph paragraphe = new Paragraph();
		paragraphe.add(new Paragraph(texte,normal));
		addEmptyLine(paragraphe,1);
		document.add(paragraphe);
	}
	
	/**
	 * Permet d'ajouter un paragraphe en fournissant des informations détaillés
	 * @param texte : le texte é ajouter
	 * @param font : la police
	 * @param alignment : l'alignement par rapport é la page
	 * @throws DocumentException
	 */
	public void ajouterParagrapghe(String texte,Font font,int alignment) throws DocumentException
	{
		Paragraph paragraphe = new Paragraph();
		paragraphe.add(new Paragraph(texte,font));
		paragraphe.setAlignment(alignment);
		addEmptyLine(paragraphe,1);
		document.add(paragraphe);
	}
	
	/**
	 * Permet d'ajouter un objet Paragraphe
	 * @param paragraphe : le paragraphe é ajouter
	 * @throws DocumentException
	 */
	public void ajouterParagraphe(Paragraph paragraphe) throws DocumentException
	{
		document.add(paragraphe);
	}
	
	/**
	 * Permet de démarrer une nouvelle page
	 */
	public void newPage()
	{
		 document.newPage();
	}
	
	/**
	 * Permet d'ajouter du contenu par chapitre et sections
	 * @param chapitre : le titre du chapitre
	 * @param numeroChapitre : le numéro de chapitre
	 * @param sousCategorie : la liste de section, avec le titreen key et le contenu en value
	 * @throws DocumentException
	 */
	public void addContent(String chapitre,int numeroChapitre,ArrayList<Entry<String,String>> sousCategorie) throws DocumentException 
	{
	    Anchor anchor = new Anchor(chapitre, catFont);
	    anchor.setName(chapitre);
	    Chapter catPart = new Chapter(new Paragraph(anchor),numeroChapitre);
	    
	    Section subCatPart = null;
	    Paragraph sousParagraphe = new Paragraph();
	    for (Entry<String,String> entry : sousCategorie) 
	    {
	    	 sousParagraphe = new Paragraph(entry.getKey(), subFont);
	    	 subCatPart = catPart.addSection(sousParagraphe);
	    	 subCatPart.add(new Paragraph(entry.getValue()));
	    }

	    document.add(catPart);
	}
	
	/**
	 * Permet d'ajouter un tableau au document
	 * @param colonne : la liste des titres de colonnes
	 * @param tableau : le tableau é ajouter
	 * @param titreTableau : le titre du Tableau
	 * @param numeroChapitre : le numéro de chapitre dnas lequel est le tableau
	 * @throws DocumentException
	 */
	public void createTable(String[] colonne,String[][] tableau,String titreTableau,int numeroChapitre) throws DocumentException 
	{
	    PdfPTable table = new PdfPTable(colonne.length);

	    // t.setBorderColor(BaseColor.GRAY);
	    // t.setPadding(4);
	    // t.setSpacing(4);
	    // t.setBorderWidth(1);
	    
	    for (int i = 0; i < colonne.length; i++) 
	    {
	    	PdfPCell c1 = new PdfPCell(new Phrase(colonne[i]));
	    	c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	    	table.addCell(c1);
		}
	   table.setHeaderRows(1);
	   
	   for (int i = 0; i < tableau.length; i++) 
	   {
		   for (int j = 0; j < colonne.length; j++) 
		   	{table.addCell(tableau[i][j]); }
	   }
	   
	   Chapter chapter = new Chapter(titreTableau,numeroChapitre);
	   Paragraph para = new Paragraph(titreTableau);
	   addEmptyLine(para,1);
	   Section subCatPart = chapter.addSection(para);
	   subCatPart.add(table);
	   document.add(subCatPart);
	}  
}
