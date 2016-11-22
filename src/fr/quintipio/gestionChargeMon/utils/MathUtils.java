package fr.quintipio.gestionChargeMon.utils;


import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Liste de méthode pour gérer facilement les chiffres
 *
 *
 */
public class MathUtils 
{


/**
     * Retourne une valeur arrondie vers l'entier supérieur.
     * 
     * @param a the a
     * 
     * @return the int
     */
    public static int arrondirVersEntierSuperieur(double a) {
        final double arrondi = Math.floor(a);
        if (a != arrondi) {
            return (int) arrondi + 1;
        }
        return (int) arrondi;
    }

    public static Integer getTailleMax(List<Integer> tailles){
    	Collections.sort(tailles);
    	return tailles.get(tailles.size() - 1);
    }
    /**
     * Retourne une valeur arrondie vers l'entier inférieur.
     * 
     * @param a the a
     * 
     * @return the int
     */
    public static int arrondirVersEntierInferieur(double a) {
        return (int) Math.floor(a);
    }
	
	/**
     * Arrondir un nombre avec la précision indiquée.
     * 
     * @param valeur nombre é arrondir
     * @param precision nombre de chiffres aprés la virgule é conserver
     * 
     * @return le nombre arrondi
     */
    public static BigDecimal arrondir(BigDecimal valeur, int precision) {
        if (valeur == null) {
            return null;
        }
        final BigDecimal valeurDecalee = valeur.movePointRight(precision);
        final long longArrondi = Math.round(valeurDecalee.doubleValue());
        return new BigDecimal(longArrondi).movePointLeft(precision);
    }
	
	
	 /**
     * Teste si un nombre est compris entre deux autres nombres.
     * @param valeur le nombre
     * @param borneInf la borne min
     * @param borneMax la borne max
     * @return tru ou false
     */
    public static boolean isBetween(Long valeur, Long borneInf, Long borneMax) {
        if(valeur.compareTo(borneInf) >= 0 && valeur.compareTo(borneMax) <= 0) {
            return true;
        }
        return false;
    }
	
	 /**
     * Teste si un nombre est compris entre deux autres nombres.
     * @param valeur le nombre
     * @param borneInf la borne min
     * @param borneMax la borne max
     * @return tru ou false
     */
    public static boolean isBetween(Integer valeur, Integer borneInf, Integer borneMax) {
        if(valeur.compareTo(borneInf) >= 0 && valeur.compareTo(borneMax) <= 0) {
            return true;
        }
        return false;
    }
	
	
	/**
     * Retourne le nombre de chiffres aprés la virgule.
     * 
     * @param n le nombre a évaluer.
     * 
     * @return le nombre de chiffres aprés la virgule.
     */
    public static Integer precision(Double n) {
        if (n == null) {
            return null;
        }
        final String valeurChaine = n.toString();
        final List<String> lValeur = StringUtils.split(valeurChaine, "[.,]");
        if (lValeur.size() == 2) {
            final String decimal = lValeur.get(1);
            return (decimal.equals("0")) ? 0 : decimal.length();
        } else {
            return 0;
        }
    }
	
	
	/**
     * Tronque le nombre de chiffres aprés la virgule en fonction de la
     * précision voulue.
     * 
     * @param n le nombre.
     * @param precision le nombre de chiffres aprés la virgule é conserver.
     * 
     * @return le nombre tronqué.
     */
    public static Double tronquer(Double n, int precision) {
        if (n == null) {
            return null;
        }
        final String valeurChaine = n.toString();
        final List<String> lValeur = StringUtils.split(valeurChaine, "[.,]");
        if (lValeur.size() == 2) {
            final String decimal = lValeur.get(1);
            if (decimal.length() > precision) {
                return new Double(lValeur.get(0) + "." + decimal.substring(0, precision));
            } else {
                return n;
            }
        } else {
            return n;
        }
    }
}
