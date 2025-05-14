
import java.util.Collections;
import java.util.List;

import pokemon.domain.Pokemon;
import pokemon.services.PokemonFetcher;
import pokemon.services.PokemonRestFetcher;

public class TestMe {
    public static void main(String[] args) throws Exception {

        final PokemonFetcher pf = new PokemonFetcher() {
            @Override
            public List<Pokemon> fetch() {
                PokemonRestFetcher prf = new PokemonRestFetcher();


                return prf.fetch();
            }
        };
        final List<Pokemon> liste = pf.fetch();
        System.out.println(liste);

    }
}
