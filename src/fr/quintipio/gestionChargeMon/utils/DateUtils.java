package fr.quintipio.gestionChargeMon.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Liste de méthode pour gérer facilement les dates
 *
 *
 */
public class DateUtils 
{
	public static final String DEFAULT_DATE_PATTERN = "dd/MM/yyyy";	
	public static final String EDITION_DATE_PATTERN = "dd_MM_yy";
	public static final String TRI_DATE_PATTERN = "yyyy-MM-dd";
	
    /** Format plus complet pour les logs.*/
    public static final String ARIANE_DATE_PATTERN = "dd/MM/yyyy HH:mm:ss";
  

    /** The Constant DATE_MIN. */
    public static final Date DATE_MIN = DateUtils.creer(1752, Calendar.DECEMBER, 31); // en fait c'est la date_min - 1 jour

    /** The Constant SMALL_DATE_MIN. */
    public static final Date SMALL_DATE_MIN =
        DateUtils.creer(1899, Calendar.DECEMBER, 31); // en fait c'est la date_min - 1 jour

    /** The Constant SMALL_DATE_MAX. */
	public static final Date SMALL_DATE_MAX = DateUtils.creer(2080, Calendar.DECEMBER, 31); // en fait c'est la date_max + 1
	
	/**
	 * Converti une date String en format Date version SQL (Format(XX/XX/XXXX)
	 * @param date : la date en chaine de caractére
	 * @return : l'objet java.sql.Date
	 */
	public static long StringtoDateSQL(String date)
	{
		int emplacementA = 0, emplacementB = 0;
		int jour =0, mois = 0, annee = 0;
		
		emplacementA = date.indexOf('/');
		emplacementB = date.lastIndexOf('/');
		
		jour = Integer.parseInt(date.substring(0,emplacementA));
		mois = Integer.parseInt(date.substring(emplacementA+1,emplacementB));
		annee = Integer.parseInt(date.substring(emplacementB+1,date.length()));
		return new GregorianCalendar(annee,mois-1,jour).getTime().getTime();
	}
	
	/**
	 * converti une objet Date util en Date SQl
	 * @param d : l'objet date format java.util.Date
	 * @return : l'objet Date en format SQL
	 */
	public static java.sql.Date DateToDateSQL(java.util.Date d)
	{return new java.sql.Date(d.getTime());}
	
	/**
     * Crée une date.
     *
     * @param annee the annee
     * @param mois constante de {@link Calendar} : <code>Calendar.APRIL</code>, etc...
     * @param jour the jour
     * @param heures the heures
     * @param minutes the minutes
     * @param secondes the secondes
     *
     * @return the date
     */
    public static Date creer(int annee, int mois, int jour, int heures, int minutes,
                             int secondes) {
        return new GregorianCalendar(annee, mois, jour, heures, minutes, secondes).getTime();
    }
    
    /**
     * Crée une date, initialisée é minuit.
     *
     * @param annee the annee
     * @param mois the mois
     * @param jour the jour
     *
     * @return the date
     *
     * @see #creer(int, int, int, int, int, int)
     */
    public static Date creer(int annee, int mois, int jour) {
        return creer(annee, mois, jour, 0, 0, 0);
    }
    
    
    /**
     * compare deux dates.
     *
     * @param date1 une date.
     * @param date2 une date.
     *
     * @return true si les deux dates sont exactes.
     */
    public static Boolean equalsDate(final Date date1, final Date date2) {
        if ((date1 == null) && (date2 == null)) {
            return Boolean.TRUE;
        } else if ((date1 == null) || (date2 == null)) {
            return Boolean.FALSE;
        }
        final int result =
            arrondir(date1, Calendar.DATE).compareTo(arrondir(date2, Calendar.DATE));
        if (result == 0) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
    
    /**
     * renvoie l'année de la date.
     * @param : d'une date
     * @return : l'année
     */
    public static Integer getYear(Date d) {
        final Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c.get(Calendar.YEAR);
    }
    
    /**
     * Formate une date selon un motif défini.
     *
     * @param d date é formater
     * @param pattern motif de formatage
     *
     * @return une chaéne de caractéres avec la date formatée
     */
    public static String format(Date d, String pattern) {
        if (d == null) {
            return null;
        }
        final String myPattern = pattern;
        return new SimpleDateFormat(myPattern).format(d);
    }
    
    
    /**
     * Formate une date selon un motif par défaut : <code>dd/MM/yyyy</code>.
     *
     * @param d the d
     *
     * @return the string
     *
     * @see #format(Date, String)
     */
    public static String format(Date d) {
        return format(d, DEFAULT_DATE_PATTERN);
    }
    
    /**
     * Formate une date selon un motif par défaut : <code>yyyy</code>.
     *
     * @param d the d
     *
     * @return the string
     *
     * @see #format(Date, String)
     */
    public static String formatAnnee(Date d) {
        return format(d, "yyyy");
    }

    /**
     * Retourne la date au format String avec un formatage court (ex 01/01/08).
     * @param d date é formater.
     * @return date formatee
     */
    public static String formatCourt(Date d) {
        return format(d, "dd/MM/yy");
    }
    
    /**
     * Retourne la date au format String avec un formatage mini (ex 311208).
     * @param d date é formater.
     * @return date formatee
     */
    public static String formatMini(Date d) {
        return format(d, "ddMMyy");
    }
    
    /**
     * Retourne la date au format String avec un formatage court (ex 01 janvier 2008).
     * @param d date à formater.
     * @return date formatee
     */
    public static String formatText(Date d) {
        return format(d, "d MMMMM yyyy");
    }
    
    public static String formatDateHeure(Date d) {
        return format(d, "dd/MM/yy à HH:mm:ss");
    }

    public static String formatDateAnneeLongHeure(Date d) {
        return format(d, "dd/MM/yyyy à HH:mm:ss");
    }
	
	/**
     * Retourne la date du jour (sans les heures).
     *
     * @return la date du jour.
     */
    public static Date getAujourdhui() {
        return arrondir(new Date(), Calendar.DATE);
    }
    
    /**
     * Retourne la date actuelle (avec les heures).
     *
     * @return la date actuelle (avec les heures).
     */
    public static Date getMaintenant() {
        return new Date();
    }
    
    /**
     * Renvoi true si l'année passé en paramètre est bissextile.
     * @param annee l'année à tester
     * @return vrai ou faux
     */
    public static boolean isBisextile(Integer annee) {
        final Integer modulo100 = annee % 100;
        final Integer modulo4 = annee % 4;
        if(!modulo100.equals(0) && modulo4.equals(0)) {
            return true;
        }
        return false;
    }
	
	/**
     * Retourne le nombre de jours du mois.
     * @param moisDateDebut moisDateDebut le nombre 0 represente janvier.
     * @param anneeDateDebut anneeDateDebut 
     * @return nbJours du mois
     */
    public static int getNbJoursMois(int moisDateDebut, int anneeDateDebut) {
        int jours = 0;
        switch (moisDateDebut) {
            case Calendar.APRIL:
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
            case Calendar.NOVEMBER:
                jours = 30;
                break;
            case Calendar.FEBRUARY:
                jours = (anneeDateDebut%4 == 0 ? 29 : 28);
                break;
            default:
                jours = 31; 
                break;
        }
        return jours;
    }
    
    /**
     * Arrondit une date sur un type de champ de Calendar. Par exemple,
     * si l'on arrondit une date sur le champ Calendar DATE}, les composantes
     * heures, minutes, secondes et millisecondes sont initialisées é zéro. Si l'on
     * arrondit sur le champ Calendar MONTH, les composantes précédentes sont
     * réinitialisées <b>et</b> le jour est initialisé é 1. Avec le champ
     * CalendarYEAR, la date est en plus réinitialisée au 1er janvier de l'année.
     *
     * @param date the date
     * @param champ une valeur parmi : CalendarYEAR, CalendarMONTH,
     * CalendarDATE, CalendarDAY_OF_MONTH,
     * CalendarHOUR, CalendarHOUR_OF_DAY, CalendarMINUTE,
     * CalendarSECOND, CalendarMILLISECOND
     *
     * @return the date
     */
    public static Date arrondir(Date date, int champ) {

        final Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        // on peut comparer la valeur de champ aux constantes de Calendar avec
        // un switch car ces valeurs font partie de l'API publique, et sont donc
        // valables quelque soit la JVM
        switch (champ) {
            case Calendar.YEAR:
                cal.set(Calendar.MONTH, Calendar.JANUARY);
            case Calendar.MONTH:
                cal.set(Calendar.DATE, 1);
            case Calendar.DATE:
                cal.set(Calendar.HOUR_OF_DAY, 0);
            case Calendar.HOUR:
            case Calendar.HOUR_OF_DAY:
                cal.set(Calendar.MINUTE, 0);
            case Calendar.MINUTE:
                cal.set(Calendar.SECOND, 0);
            case Calendar.SECOND:
                cal.set(Calendar.MILLISECOND, 0);
                break;
            default:
                throw new RuntimeException("Valeur incorrecte pour l'argument 'champ' : " +champ);
        }

        return cal.getTime();
    }
}
