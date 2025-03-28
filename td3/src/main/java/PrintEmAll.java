import pokemon.PokemonRegistry;

import java.io.IOException;
import java.net.URISyntaxException;

public class PrintEmAll {
    public static void main(String[] args) throws URISyntaxException, IOException {
        PokemonRegistry pr = new PokemonRegistry("https://www.data.gouv.fr/fr/datasets/r/04df345d-03dd-45f3-aed2-bf3dd55201a2");
        //Afficher les 100 premiers
        pr.fetch().stream().limit(100).forEach(System.out::println);
    }
}
