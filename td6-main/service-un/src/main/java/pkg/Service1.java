package pkg;

import java.util.List;
import java.util.stream.Collectors;

import pkg.impl.CoffeeListImpl;
import pkg.impl.CoffeeListImpl.CaffeinatedBeverage;

/**
 * Ce service permet de récupérer une liste
 * de boisson caféinées en utilisant une api public.
 * @see <a href="https://api.sampleapis.com">Sample APIs</a>
 * @version 1
 */
public class Service1 {

    /** Implémentation. */
    private final CoffeeListImpl impl = new CoffeeListImpl();

    /** No-arg constructor. */
    public Service1() {}

    /**
     * Recherche la liste des boisson caféinées disponibles,
     * avec pour chacune leur prix en euros.
     * @param t Le type de boisson, parmi "HOT, et "COLD".
     * @return La liste.
     * @throws IllegalArgumentException si le type est incorrect.
     */
    public List<B> l(String t) {
        final List<CaffeinatedBeverage> rate = impl.listCaffeinatedBeverage(t);
        return rate.stream().map(e -> new B(e.name(), e.description(), e.image(), e.price())).collect(Collectors.toList());
    }

    /**
     * Contient les infos sur une boisson caféinée.
     * @param n Le nom de la boisson.
     * @param d Le taux de conversion à la date.
     * @param u Le lien "https://" vers l'image.
     * @param p Le prix en euros.
     */
    public static record B(String n, String d, String u, float p) {
        
        // /**
        //  * No-arg constructor.
        //  * @param n Le nom de la boisson.
        //  * @param d Le taux de conversion à la date.
        //  * @param p Le prix en euros.
        //  */
        // public B {}
    }

    /**
     * Main.
     * @param args La ligne de commande.
     */
    public static void main(String[] args) {
        final List<B> res = new Service1().l("hot");
        System.out.println("Nombre de boissons caféinées : " + res.size());
        res.forEach(e -> System.out.println("* " + e.n + " (" + e.p + ")" ));
    }

}
