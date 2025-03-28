import pokemon.PokemonRegistry;

import java.net.URISyntaxException;

public class PrintEmAll {
    public static void main(String[] args) throws URISyntaxException {
        PokemonRegistry pr = new PokemonRegistry("http://localhost:8080/pokemons");
        pr.fetch().forEach(System.out::println);
    }
}
