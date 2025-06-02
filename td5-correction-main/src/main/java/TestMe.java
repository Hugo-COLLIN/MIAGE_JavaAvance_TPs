
import java.util.List;

import pokemon.domain.Pokemon;
import pokemon.services.PokemonFetcher;
import pokemon.services.impl.PokemonRestFetcher;

/**
 * Classe de test pour l'implémentation qui appelle
 * le webservice exposé sur les Internets.
 */
public class TestMe {
    public static void main(String[] args) throws Exception {

        // cette implémentation appelle le webservice 👇
        final PokemonFetcher pf = new PokemonRestFetcher();
        final List<Pokemon> liste = pf.fetch();

        // affichage des 10 premiers
        liste.stream().limit(10).forEach(p -> System.out.println(p));
        System.out.println("+ " + (liste.size() - 10) + " autres ...");
          
    }
}
