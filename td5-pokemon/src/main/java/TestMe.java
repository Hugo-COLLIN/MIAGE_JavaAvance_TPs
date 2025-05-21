
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import pokemon.domain.Pokemon;
import pokemon.services.PokemonFetcher;
import pokemon.services.impl.PokemonFileFetcher;
import pokemon.services.impl.PokemonRestFetcher;

public class TestMe {
    public static void main(String[] args) throws Exception {

        final PokemonFetcher pf = new PokemonFetcher() {
            @Override
            public List<Pokemon> fetch() {
                try {
                    PokemonRestFetcher prf = new PokemonRestFetcher();
                    return prf.fetch();
                }
                catch (IOException e) {
                    System.out.println("Erreur de connexion au serveur");
                    PokemonFileFetcher pff = new PokemonFileFetcher();
                    return pff.fetch();
                }
            }
        };
        final List<Pokemon> liste = pf.fetch();
        System.out.println(liste);

    }
}
