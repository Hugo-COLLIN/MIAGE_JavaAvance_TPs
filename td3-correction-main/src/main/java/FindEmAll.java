import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import pokemon.PokemonRegistry;

@Command(name = "find", description = "Search pokemons by (partial) name")
public class FindEmAll implements Callable<Integer> {

    @Parameters(index = "0", description = "Search filter")
    private String filter;

    @Override
    public Integer call() throws Exception {
        final Properties appProps = new Properties();
        try(final InputStream inputStream = getClass().getResourceAsStream("/configuration.properties")) {
            appProps.load(inputStream);

            new PokemonRegistry(appProps.get("endpoint").toString()).fetch().stream()
                .filter(p -> p.getNom().toLowerCase().contains(this.filter.toLowerCase()))
                .forEach(p -> { System.out.println(p); });

            return 0;
        }
    }

    public static void main(String... args) {
        System.exit( new CommandLine(new FindEmAll()).execute(args) );
    }
}