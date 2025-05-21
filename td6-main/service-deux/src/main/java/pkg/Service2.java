package pkg;

import pkg.impl.BitStampImpl;
import pkg.impl.BitStampImpl.BtcExchangeRate;

/**
 * Ce service permet d'obtenir le cours du bitcoin en temps réel.
 * Il utilise un service web exposé par BitStamp.
 * @see <a href="https://www.bitstamp.net">BitStamp</a>
 * @version 1
 */
public class Service2 {

    /** Implémentation. */
    private final BitStampImpl impl = new BitStampImpl();

    /** No-arg constructor. */
    public Service2() {}

    /**
     * Recherche le taux de conversion du bitcoin dans la devise spécifiée.
     * @param d La devise demandée parmi "GBP", "USD" et "EUR".
     * @return Le taux de change pour la device demandée.
     * @throws IllegalArgumentException si la devise est incorrecte.
     */
    public R e(String d) {
        final BtcExchangeRate rate = impl.btcExchangeRate(d);
        return new R(rate.date_maj(), rate.current_rate());
    }

    /**
     * Contient le cours du bitcoin en euro à la date fournie
     * @param d La date de mise-à-jour.
     * @param r Le taux de conversion à la date.
     */
    public static record R(String d, float r) {        
        // /**
        //  * No-arg constructor.
        //  * @param d La date de mise-à-jour.
        //  * @param r Le taux de conversion à la date.
        //  */
        // public R {}
    }

    /**
     * Main.
     * @param args La ligne de commande.
     */
    public static void main(String[] args) {
        final R res = new Service2().e("EUR");
        System.out.println("Dernière maj : " + res.d);
        System.out.println("Taux : 1BTC = " + res.r + "€");
    }

}
