package fr.quintipio.gestionChargeMon.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Liste de méthode pour gérer facilement les chaines de caractéres
 *
 *
 */
public class StringUtils
{
	private static final int MIN = 192;
	private static final int MAX = 255; 
	private static final ArrayList<String> LIST_CORRESPONDANCE = initCorrespondance();   
	public static final Map<String, String> MAP_ACCENTS_REPORT = initMapAccentsReport(); 
	
	private static final List<String> LIST_ARTICLES = Arrays.asList("le", "au", "du", "é", "de", "la", "un", "une", "du", "les", "aux", "des");
	private static final List<String> LIST_APOSTROPHE = Arrays.asList("l'", "d'", "t'", "m'", "qu'", "c'");
	private static final List<String> LIST_PREPOSITIONS = Arrays.asList("é", "aprés", "avant", "avec", "chez", "concernant", "contre", "dans", "de", "depuis", "derriére",
					"dés", "devant", "durant", "en", "entre", "envers", "hormis", "hors", "jusque", "malgré", "moyennant", "nonobstant",
					"outre", "par", "parmi", "pendant", "pour", "prés", "sans", "sauf", "selon", "sous", "suivant", "sur", "vers", "via","dessus", "dessous", "loin");
	private static final List<String> LIST_CONJONCTIONS = Arrays.asList("et", "ou", "ni", "mais", "or", "donc", "que", "quand", "comme", "si", "lorsque", "quoique", "puisque");
	private static final List<String> LIST_PRONOMS_PERSOS = Arrays.asList( "je", "me", "tu", "il", "elle" , "nous", "vous", "ils", "elles", "leur", "on", "moi", "toi", "soi", "lui", "eux", "en", "y", "se", "te");
	private static final List<String> LIST_PRONOMS_RELATIFS = Arrays.asList( "qui", "que", "quoi", "dont", "oé", "quel", "quels", "quelles", "lequel", "auquel", "duquel", "laquelle", "lesquels", "auxquels", "desquels", "lesquelles", "auxquelles", "desquelles");

	private static final List<String> LIST_ADJECTIFS_POSSESSIFS = Arrays.asList( "mon","ton","ma","ta","sa","notre","votre","leur","mes","tes","ses","nos","vos","leurs");
	private static final List<String> LIST_PRONOMS_DEMONSTRATIFS = Arrays.asList( "ce", "ceci", "cela", "éé", "celui", "celui-ci", "celui-lé",
					"celle", "celle-ci", "celle-là", "ceux", "ceux-ci", "ceux-lé", "celles", "celles-ci", "celles-lé");
	private static final List<String> LIST_PRONOMS_POSSESIFIFS = Arrays.asList( "mien", "tien", "sien", "mienne", "tienne", "sienne", "miens", "tiens", "siens",
					"miennes", "tiennes", "siennes", "nétre", "vétre", "leur", "nétres", "vétres", "leurs");
	private static final List<String> LIST_PRONOMS_INDEFINIS = Arrays.asList( "on", "uns", "unes", "autre", "autres", "aucun", "aucune", "aucuns", "aucunes",
					"certain", "certaine", "certains", "certaines", "tel", "telle", "tels", "telles", "tout", "toute", "tous", "toutes",
					"méme", "mémes", "nul", "nulle", "nuls", "nulles", "personne", "autrui", "quiconque", "plusieurs", "rien", "quelque",
					"quelqu'un", " quelqu'une", "quelques", "chacun", "chacune");
	@SuppressWarnings("unused")
	private static final List<String> LIST_ACCENT_A = Arrays.asList( "a","é","é","é");
	@SuppressWarnings("unused")
	private static final List<String> LIST_ACCENT_C = Arrays.asList( "c" ,"é");
	@SuppressWarnings("unused")
	private static final List<String> LIST_ACCENT_E = Arrays.asList( "e","é","é","é","é");
	@SuppressWarnings("unused")
	private static final List<String> LIST_ACCENT_I = Arrays.asList( "i","é","é");
	@SuppressWarnings("unused")
	private static final List<String> LIST_ACCENT_O = Arrays.asList( "o","é","é");
	@SuppressWarnings("unused")
	private static final List<String> LIST_ACCENT_U = Arrays.asList( "u","é","é","é");
	
	
	/**
	 * Permet de convertir les , d'une chaine en . (Utile pour convertir en chiffre les Strings)
	 * @param text : la chaine é convertir
	 * @return : la chaine converti
	 */
	public static String changeVirgulePoint(String text)
	{return text.replace(',','.');}
	
	
	/**
     * Supprime les caractéres d'espacement (espace, tabulation, etc...) en
     * d?but et en fin de chaéne. Si le résultat est une chaine vide, ou si la
     * cha?ne en entrée vaut <code>null</code>, la valeur <code>null</code>
     * est renvoyée.
     * Attention, quand un integer vaut null et que l'on envoie un String.valueOf de cet<br>
     * Integer, le string n'est pas null mais contient 'null' .......!
     * 
     * @param str the str
     * 
     * @return the string
     */
	public static String trimToNull(String str) {
			if (str == null || str == "null") {
				return null;
			}
			final String newStr = str.trim();

			return (newStr.length() == 0) ? null : newStr;
		}
	
	/**
     * Teste si une chaéne est <code>null</code>, vide ou ne contient que des
     * espaces.
     * 
     * @param str the str
     * 
     * @return true, if is empty
     */
	 public static boolean isEmpty(String str) {
			return trimToNull(str) == null;
		}
	 
	 /**
	     * Supprime les caracteres d'espacement (espace, tabulation, etc...) en
	     * debut et en fin de chaine. Si le résultat est une chaine vide, ou si la
	     * chaine en entrée vaut <code>null</code>, une chaine vide est renvoyée.
	     * 
	     * @param str the str
	     * 
	     * @return the string
	     */	
		 public static String trimToBlank(String str) {
				final String newStr = trimToNull(str);
				return (newStr != null) ? newStr : "";
			}
		 
		 /**
		     * Concaténe deux tableaux. Deux tableaux <code>null</code> sont remplacées par une tableau vide.
		     * 
		     * @param s1 the s1
		     * @param s2 the s2
		     * 
		     * @return the string[]
		     */
			public static String[] concatArrays(String[] s1, String[] s2) {
		        String[] s = new String[0];
		        if(s1 == null && s2 == null) {
		            return s;
		        }
		        final int taille1 = (s1 == null) ? 0 : s1.length;
		        final int taille2 = (s2 == null) ? 0 : s2.length;
		        final int taille = taille1 + taille2;
		        s = new String[taille];
		        if(s1 != null) {
		            for (int i = 0; i < s1.length; i++) {
		                s[i] = s1[i];
		            }
		        }
		        if(s2 != null) {
		            for (int i = 0; i < s2.length; i++) {
		                s[taille1 + i] = s2[i];
		            }
		        }
		        return s;
		    }
			
			/**
		     * Découpe une chaéne de caractéres suivant un délimiteur.
		     * 
		     * @param regexDelim the regex delim
		     * @param str the str
		     * 
		     * @return the list< string>
		     */
			public static List<String> split(String str, String regexDelim) {
		        return Arrays.asList(str.split(regexDelim));
		    }
			
			/**
		     * Retourne la concat?nation des chaines d'une liste en utilisant le
		     * délimiteur indiqué. Exemple : join(["A", "B", "CD"], "/") --> "A/B/CD"
		     * 
		     * @param liste la liste des chaines é concaténer.
		     * @param delim le délimiteur é insérer entre chaque valeur.
		     * 
		     * @return la chaine contenant les valeurs concat?n?es.
		     */
			public static String join(List<String> liste, String delim) {
		        boolean mettreSeparateur = false;
		        final StringBuilder builder = new StringBuilder();
		        if (liste != null) {
		            for (String val : liste) {
		                if (mettreSeparateur) {
		                    builder.append(delim);
		                }
		                builder.append(val);
		                mettreSeparateur = true;
		            }
		        }
		        return builder.toString();
		    }
			
			/**
		     * Retourne la valeur de la methode <code>toString()</code> d'un objet. Si
		     * l'objet vaut <code>null</code>, la valeur <code>null</code> est
		     * renvoyee.
		     * 
		     * @param obj the obj
		     * 
		     * @return the string
		     */
			public static String valueOf(Object obj) {
		        return (obj != null) ? obj.toString() : null;
		    }
			
			/**
		     * Suppression de tous les espaces dans la chaine de caracteres fournie.
		     * 
		     * @param chaine the chaine
		     * 
		     * @return the string
		     */
			 public static String stripSpaces(String chaine) {
		        return (chaine != null) ? chaine.replaceAll("\\s", "") : null;
		    }
			 
			 /**
		     * Transforme une chaine pouvant contenir des accents dans une version sans accent.
		     * 
		     * @param chaine Chaine éconvertir sans accent.
		     * 
		     * @return Chaine en majuscule dont les accents ont été supprimé.
		     */
			public static String sansAccent(final String chaine) {
		        if (StringUtils.isEmpty(chaine)) {
		            return chaine;
		        }
		        return effectueSansAccent(chaine);
		    }
			
			/**
		     * Effectue le traitement de remplacement des caractéres accentués en caractéres non accentués.
		     * 
		     * @param chaine Chaine é convertir sans accent.
		     * 
		     * @return Chaine dont les accents ont été supprimé.
		     */
			public static String effectueSansAccent(final String chaine) {
		        final StringBuilder result = new StringBuilder();
		        for (int i=0; i<chaine.length(); i++) {
		            final char carVal = chaine.charAt(i);
		            ajouteSansAccent(result, carVal);
		        }
		        return result.toString();
		    }
			
			
			/**
		     * Ajoute autant de 'caractere' é la fin de la chaine 'valeur' pour obtenir la taille 'tailleTotale'.
		     * 
		     * @param valeur chaine de base
		     * @param tailleTotale taille totale de la chaine de retour
		     * @param caractere caractére é ajouter
		     * 
		     * @return la chaine avec les caractéres ajoutés.
		     */
			 public static String padRight(String valeur, int tailleTotale, char caractere) {
		        final StringBuilder retour = new StringBuilder();
		        final String valeurPasNull = StringUtils.trimToBlank(valeur);
		        if (valeurPasNull.length() <= tailleTotale) {
		            retour.append(valeurPasNull);
		        } else {
		            retour.append(valeurPasNull, 0, tailleTotale);
		        }
		        
		        for (int i=retour.length(); i<tailleTotale; i++) {
		            retour.append(caractere);
		        }
		        return retour.toString();
		    }
			
			/**
		     * Ajoute autant de 'caractere' au début de la chaine 'valeur' pour obtenir la taille 'tailleTotale'.
		     * 
		     * @param valeur chaine de base
		     * @param tailleTotale taille totale de la chaine de retour
		     * @param caractere caractére é ajouter
		     * 
		     * @return la chaine avec les caractéres ajoutés.
		     */
			public static String padLeft(String valeur, int tailleTotale, char caractere) {
		        final StringBuilder retour = new StringBuilder();
		        final String valeurPasNull = StringUtils.trimToBlank(valeur);
		        for (int i=valeurPasNull.length(); i<tailleTotale; i++) {
		            retour.append(caractere);
		        }
		        if (valeurPasNull.length() <= tailleTotale) {
		            retour.append(valeurPasNull);
		        } else {
		            retour.append(valeurPasNull, 0, tailleTotale);
		        }
		        
		        return retour.toString();
		    }
			
			
			
			/**
		     * Methode : isIn.
		     * 
		     * @param name : la chaine a trouver.
		     * @param valeurs : la chaine que l'on cherche.
		     * @param separateur : le séparateur.
		     * 
		     * @return : vrai ou faux
		     */
		    public static boolean isIn(final String name, String valeurs, String separateur){
		        if(StringUtils.trimToNull(name)!= null && StringUtils.trimToNull(valeurs)!= null){
		            final StringTokenizer st = new StringTokenizer(valeurs,separateur);
		            while (st.hasMoreTokens()) {
		                if(st.nextToken().equals(name)){
		                    return true;
		                }
		            }
		        }

		        return false;
		    }
		    
		    /**
		     * Vérifie si la chaéne de caractéres fournie contient
		     * uniquement des caractéres alphanumériques non
		     * accentués avec acceptation ou non des espaces.
		     * 
		     * @param valeur the valeur
		     * @param accepterEspaces the accepter espaces
		     * 
		     * @return true, if checks if is alpha numeric non accentue
		     */
		    public static Boolean isAlphaNumericNonAccentue(String valeur, Boolean accepterEspaces){
		        final String val = StringUtils.trimToNull(valeur);
		        if(val==null){
		            return true;
		        } else {
		            String matcher = "abcdefghijklmnopqrstuvwxyz0123456789";
		            if(accepterEspaces){                
		                matcher += ' ';
		            }            
		            matcher = "[" + matcher + "]*";
		            return val.toLowerCase().matches(matcher);
		        }
		    }
		    
		    /**
		     * Retourne la valeur avec une majuscule au début.
		     * @param valeur valeur é formater.
		     * @return valeur formattée.
		     */
		    public static String formatagePremiereLettreMajuscule(String valeur) {
		        if (StringUtils.isEmpty(valeur)) {
		            return valeur;
		        }
		        if (valeur.length()>1) {
		            return valeur.substring(0, 1).toUpperCase() + valeur.substring(1);
		        } else { //if (valeur.length()>0) {
		            return valeur.toUpperCase();
		        }
		    }
		    
		    /**
		     * Retourne la valeur avec une majuscule au début.
		     * @param valeur valeur éformater.
		     * @return valeur formattée.
		     */
		    public static String formatageSeulementPremiereLettreMajuscule(String valeur) {
		        if (StringUtils.isEmpty(valeur)) {
		            return valeur;
		        }
		        if (valeur.length()>1) {
		            return valeur.substring(0, 1).toUpperCase() + valeur.substring(1).toLowerCase();
		        } else { //if (valeur.length()>0) {
		            return valeur.toUpperCase();
		        }
		    } 
		    
		    
		    /**
		     * Similaire au replaceSpecialChars mais uniquement pour les "_"
		     * @param param
		     * @return : la nouvelle chaine
		     */
		    public static String replaceUnderscore(String param){
		    	if (isEmpty(param)) return param;
		        String chaine = param;
		        chaine = chaine.replaceAll("_", ""); //efface les '_' underscore
		        return chaine;   
		    }
		    
		    /**
		     * 
		     * Methode : stringToMap.
		     * @param valeurs .
		     * @param separateur1 .
		     * @param separateur2 .
		     * @return :
		     */
		    public static Map<String, String> stringToMap(String valeurs, String separateur1, String separateur2) {
		        final Map<String,String> mapFormation = new HashMap<String, String>();
		        if(valeurs != null){
			        final List<String> listeFormation = StringUtils.split(valeurs,separateur1);
			        for(String formation: listeFormation){
			            final String[] tab = formation.split(separateur2);
			            if(tab.length>1){
			                mapFormation.put(tab[0],tab[1]);
			            }
			        }
		        }
		        return mapFormation;
		    }
			
		    /**
		     * Permet d'enlever les apostrophes d'une chaine
		     * @param champ : la chaine a traiter
		     * @return : la chaine traité
		     */
			public static String enleverApostrophe(String champ) {
		        StringBuilder buf = new StringBuilder();
		    	for (int i = 0 ; i < champ.length() ; i++) {
		    		final char lettre = champ.charAt(i);
		    		if (lettre == '\''){
		    			buf.append(" ");
		    		}else{
		    			buf.append(lettre);
		    		}
		    	}
		    	return buf.toString();
		    }

			/**
			 * Permet de rempalcer les \' par '
			 * @param champ : la chaine é traiter
			 * @return : la chaine traité
			 */
		    public static String remplacerApostrophe(String champ) {
		        StringBuilder buf = new StringBuilder();
		    	for (int i = 0 ; i < champ.length() ; i++) {
		    		final char lettre = champ.charAt(i);
		    		if (lettre == '\''){
		    			buf.append("' ");
		    		}else{
		    			buf.append(lettre);
		    		}
		    	}
		    	return buf.toString();
		    }
			
		    /**
		     * Permet de remplacer les caractére HTML par rien
		     * @param text : la chaine é traiter
		     * @return : le chaine traité
		     */
			public static String escapeHTMLComments(String text){
				return !StringUtils.isEmpty(text) ? text.replaceAll("(?s)<!--\\[if(.*?)\\[endif\\] *-->", "") : "";
			}
			
			public static String escapeAllNonAlphaNum(String text){
				return !StringUtils.isEmpty(text) ? text.replaceAll("[^A-Za-z0-9àâäôöïîûùüèéêëç' ]", "") : "";
			}
			
			public static String defaultIfEmpty(String text, String def) {
		        return StringUtils.isEmpty(text) ? def : text;
		    }
			
			
			/**
			 * Permet de formatter une chaine de recherche en une liste de mot clé
			 * en supprimant tous les mots de moins de 2 caracteres et les listes suivantes :
			 * LIST_ARTICLES / LIST_CONJONCTIONS / LIST_PROPOSITIONS / LIST_PRONOMS_PERSOS / LIST_PRONOMS_RELATIFS
			 * 
			 * @param chaine
			 * @return : la liste des mots clés
			 */
			public static List<String> formatChaineDeRechercheEnMotCle(String chaine){
				List<String> listMotCleFinal = new ArrayList<String>();
//				String aAccent = "[aàâä]";
//				String cAccent = "[cç]";
//				String eAccent = "[eéèêë]";
//				String iAccent = "[iîï]";
//				String oAccent = "[oôö]";
//				String uAccent = "[uùûü]";

				// on extrait tous les mots de la chaine de recherche.
				List<String> listMotCle = new ArrayList<String>();
				listMotCle.addAll(split(chaine, " "));

				// Recherche des mots qui sont inutiles pour la recherche.
				for ( String motCle : listMotCle){
			 		motCle = escapeAllNonAlphaNum(motCle); // Suppression de la ponctuation
					motCle = trimToNull(motCle);			
					if ( motCle != null ){
						motCle = motCle.toLowerCase(); // On travaille en minuscule
						// Verification si le mot commence par un article avec un '
						for ( String apostrophe : LIST_APOSTROPHE){
							if ( motCle.startsWith(apostrophe)){
								// On supprime l'article en question
								motCle = motCle.replace(apostrophe, "");
							}
						}

						if ( !LIST_ARTICLES.contains(motCle) &&
								!LIST_CONJONCTIONS.contains(motCle) &&
								!LIST_PREPOSITIONS.contains(motCle) &&
								!LIST_PRONOMS_PERSOS.contains(motCle) &&
								!LIST_PRONOMS_RELATIFS.contains(motCle) &&
								!LIST_APOSTROPHE.contains(motCle) &&
								!LIST_ADJECTIFS_POSSESSIFS.contains(motCle) && 
								!LIST_PRONOMS_DEMONSTRATIFS.contains(motCle) && 
								!LIST_PRONOMS_POSSESIFIFS.contains(motCle) &&  
								!LIST_PRONOMS_INDEFINIS.contains(motCle) ){
							
							if ( motCle.contains("'")) motCle = motCle.replace("'", "''");
							
							// Remplacement des accents pour la recherche, si l'accent saisi est mauvais.
//							char[] listeCaractere =  motCle.toCharArray();
//							for (int i = 0; i < listeCaractere.length; i++) {
//								if ( LIST_ACCENT_A.contains(listeCaractere[i]) ){
//									motCle = motCle.replace("a", aAccent);
//									break;
//								}
//								
//								if ( LIST_ACCENT_C.contains(listeCaractere[i]) ){
//									motCle = motCle.replace("c", cAccent);
//									break;
//								}
//								
//								if ( LIST_ACCENT_E.contains(listeCaractere[i]) ){
//									motCle = motCle.replace("e", eAccent);
//									break;
//								}
//								
//								if ( LIST_ACCENT_I.contains(listeCaractere[i]) ){
//									motCle = motCle.replace("i", iAccent);
//									break;
//								}
//								
//								if ( LIST_ACCENT_O.contains(listeCaractere[i]) ){
//									motCle = motCle.replace("o", oAccent);
//									break;
//								}
//								
//								if ( LIST_ACCENT_U.contains(listeCaractere[i]) ){
//									motCle = motCle.replace("u", uAccent);
//									break;
//								}
//								
//							}
							
							listMotCleFinal.add(motCle);
						}
					}
				}
				return listMotCleFinal;
			}
			/**
		     * Ajoute le caractére sans l'accent dans le builder.
		     * @param builder builder
		     * @param c caractere
		     */
		    private static void ajouteSansAccent(StringBuilder builder, char c) {
		        if ((c >= MIN) && (c <= MAX)) {
		            // ajout du caractére sans accent.
		            final String newVal = LIST_CORRESPONDANCE.get(c - MIN);
		            builder.append(newVal);
		        } else {
		            builder.append(c);
		        }
		    }


		    /**
		     * Initialisation de la liste de correspondance entre les caractéres accentués
		     * et leur homologues non accentués.
		     * 
		     * @return liste de correspondances
		     */
		    private static ArrayList<String> initCorrespondance() {
		        final ArrayList<String> result = new ArrayList<String>();
		        String car = null;

		        car = "A";
		        result.add(car);
		        result.add(car); 
		        result.add(car); 
		        result.add(car); 
		        result.add(car);
		        result.add(car);
		        car = "AE";
		        result.add(car); 
		        car = "C";
		        result.add(car); 
		        car = "E";
		        result.add(car);
		        result.add(car);
		        result.add(car); 
		        result.add(car); 
		        car = "I";
		        result.add(car); 
		        result.add(car); 
		        result.add(car); 
		        result.add(car); 
		        car = "D";
		        result.add(car); 
		        car = "N";
		        result.add(car); 
		        car = "O";
		        result.add(car); 
		        result.add(car); 
		        result.add(car); 
		        result.add(car);
		        result.add(car); 
		        car = "*";
		        result.add(car); 
		        car = "0";
		        result.add(car); 
		        car = "U";
		        result.add(car); 
		        result.add(car); 
		        result.add(car);
		        result.add(car); 
		        car = "Y";
		        result.add(car);
		        car = "é";
		        result.add(car); 
		        car = "B";
		        result.add(car); 
		        car = "a";
		        result.add(car); 
		        result.add(car); 
		        result.add(car);
		        result.add(car); 
		        result.add(car); 
		        result.add(car); 
		        car = "ae";
		        result.add(car);
		        car = "c";
		        result.add(car); 
		        car = "e";
		        result.add(car); 
		        result.add(car); 
		        result.add(car); 
		        result.add(car); 
		        car = "i";
		        result.add(car); 
		        result.add(car); 
		        result.add(car);
		        result.add(car);
		        car = "d";
		        result.add(car); 
		        car = "n";
		        result.add(car); 
		        car = "o";
		        result.add(car); 
		        result.add(car); 
		        result.add(car); 
		        result.add(car); 
		        result.add(car); 
		        car = "/";
		        car = "0";
		        result.add(car); 
		        car = "u";
		        result.add(car); 
		        result.add(car); 
		        result.add(car); 
		        result.add(car);
		        car = "y";
		        result.add(car); 
		        car = "é";
		        result.add(car); 
		        car = "y";
		        result.add(car); 
		        result.add(car); 

		        return result;
		    }


		/**
		     * Creation de Map des correspondances d'accents pour les éditions PDF.
		     * @return Map des correspondances d'accents pour les éditions PDF.
		     */
		    private static Map<String, String> initMapAccentsReport() {
		        final Map<String, String> mapRetour = new HashMap<String, String>();
		        mapRetour.put("a_grave", "é");
		        mapRetour.put("a_circonflexe", "é");
		        mapRetour.put("ae", "é");
		        mapRetour.put("c_cedille", "é");
		        mapRetour.put("e_grave", "é");
		        mapRetour.put("e_aigu", "é");
		        mapRetour.put("e_circonflexe", "é");
		        mapRetour.put("e_trema", "é");
		        mapRetour.put("i_circonflexe", "é");
		        mapRetour.put("i_trema", "é");
		        mapRetour.put("o_circonflexe", "é");
		        mapRetour.put("u_grave", "é");
		        mapRetour.put("u_circonflexe", "é");
		        mapRetour.put("degre", "é");
		        
		        return mapRetour;
		    }



}
