import java.util.List;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import pokemon.domain.Pokemon;
import pokemon.services.PokemonFetcher;
import pokemon.services.PokemonFetcherFactory;

/**
 * Implémentation de la CLI vec picocli.
 */
@Command(name = "find", description = "Search pokemons by (partial) name")
public class CatchEmAll implements Runnable {
    
    @Parameters(index = "0", description = "Search filter")
    private String filter;

    @Option(names = {"-p", "--provider"}, description = "API (default) or FILE")
    private PokemonFetcherFactory.Provider provider = PokemonFetcherFactory.Provider.API;

    @Override
    public void run() {
        final PokemonFetcher pf = PokemonFetcherFactory.build(this.provider);
        final List<Pokemon> liste = pf.fetch();
        System.out.println("Search results for « " + this.filter + " » among " + liste.size() + " pokemons : ");
        liste.stream()
            .filter(p -> p.getNom().toLowerCase().contains(this.filter))
            .forEach((p) -> {
                System.out.println("* " + p.getNom() + " (" + p.getPv() + ")  -  " + p.getType()); 
            });
    }

    public static void main(String... args) {
        System.exit( new CommandLine(new CatchEmAll()).execute(args) );
    }
}
