package fr.quintipio.gestionChargeMon.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Liste de méthodes pour gérer facilement les collections
 *
 *
 */
public class CollectionUtils
{
	/**
     * Teste si une {@link Collection} est vide ou <code>null</code>.
     *
     * @param <T> type générique
     * @param col
     *            La collection é vérifier
     *
     * @return vrai si la collection est null ou vide.
     */
    public static <T> boolean isEmpty(Collection<T> col) {
        return (col == null) || col.isEmpty();
    }
    
    /**
     * Retourne le nombre d'éléments de la collection.
     * @param <T> type générique
     * @param col collection
     * @return nombre d'éléments
     */
    public static <T> int size(Collection<T> col) {
        if (col == null) {
            return 0;
        } else {
            return col.size();
        }
    }
    
    /**
     * Récupération du dernier élément d'une liste.
     * @param <T> type générique.
     * @param liste la liste.
     * @return le dernier élément.
     */
    public static <T> T getLastElement(List<T> liste) {
        if (isEmpty(liste)) {
            return null;
        } else {
            return liste.get(liste.size() - 1);
        }
    }
    
    /**
     * Récupération du premier élément d'une liste.
     * @param <T> type générique.
     * @param liste la liste.
     * @return le dernier élément.
     */
    public static <T> T getFirstElement(List<T> liste) {
        if (isEmpty(liste)) {
            return null;
        } else {
            return liste.get(0);
        }
    }
   
    
    /**
     * Vérifie si les éléments d'une collection apparaissent une et une seule fois dans la collection.
     *
     * @param <T> le type.
     * @param c l'instance de collection
     * @return un booléen, True si les éléments apparaissent une et une seule fois, False sinon
     */
    public static <T> Boolean hasNoDuplicateElement(Collection<T> c) {
        if(CollectionUtils.isEmpty(c)) {
            return Boolean.TRUE;
        }
        final Set<T> set = new HashSet<T>(c);
        for (final T t : set) {
            if(Collections.frequency(c, t) != 1) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }
    
    /**
     * Récupére les éléments d'une collection apparaissant plus d'une fois dans la collection.
     *
     * @param <T> le type.
     * @param c l'instance de collection
     * @return un Set des éléments apparaissant plus d'une fois dans la collection.
     */
    public static <T> Set<T> getDuplicateElement(Collection<T> c) {
        if(CollectionUtils.isEmpty(c)) {
            return Collections.emptySet();
        }
        final Set<T> set = new HashSet<T>();
        for (final T t : c) {
            if(Collections.frequency(c, t) != 1) {
                set.add(t);
            }
        }
        return set;
    }
    
    /**
     * Ajoute le contenu de la liste é inserer dans la liste destination.
     *
     * @param <T> le type.
     * @param listeDestination la liste destination
     * @param listeAInserer la liste a inserer
     *
     * @return the list<T>
     */
    public static <T> List<T> addAll(List<T> listeDestination, List<T> listeAInserer){
        if(listeDestination == null && listeAInserer == null) {
            return new ArrayList<T>();
        }
        if(listeDestination == null && listeAInserer != null) {
            return new ArrayList<T>(listeAInserer);
        }
        if(listeDestination != null && listeAInserer == null) {
            return new ArrayList<T>(listeDestination);
        }
        final List<T> listeRetour = new ArrayList<T>(listeDestination);
        listeRetour.addAll(listeAInserer);
        return listeRetour;
    }
}
