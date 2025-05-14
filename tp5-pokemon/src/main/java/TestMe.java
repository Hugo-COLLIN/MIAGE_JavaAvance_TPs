
import java.util.Collections;
import java.util.List;

import pokemon.domain.Pokemon;
import pokemon.services.PokemonFetcher;

public class TestMe {
    public static void main(String[] args) throws Exception {

        final PokemonFetcher pf = new PokemonFetcher() {
            @Override
            public List<Pokemon> fetch() {
                return Collections.emptyList();
            }
        };
        final List<Pokemon> liste = pf.fetch();
        System.out.println(liste);
          
    }
}
