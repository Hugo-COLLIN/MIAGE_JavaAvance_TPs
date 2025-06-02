
import java.util.List;

import pokemon.decorators.CacheDecorator;
import pokemon.domain.Pokemon;
import pokemon.services.PokemonFetcher;
import pokemon.services.PokemonFetcherFactory;

/**
 * Classe de test permettant de mettre en oeuvre les design-pattern :
 * <em>factory</em> pour l'instanciation du service, <em>decorator</em> pour la mise en cache.
 */
public class FetchAll {
    public static void main(String[] args) throws Exception {

        final PokemonFetcher pf = new CacheDecorator(PokemonFetcherFactory.build(PokemonFetcherFactory.Provider.API));
        final List<Pokemon> liste = pf.fetch();

        // affichage des 10 premiers
        liste.stream().limit(10).forEach(p -> System.out.println(p));
        System.out.println("+ " + (liste.size() - 10) + " autres ...");
          
    }
}
