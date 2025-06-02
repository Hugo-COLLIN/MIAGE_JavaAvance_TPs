
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import pokemon.PokemonRegistry;

public class PrintEmAll {
    public static void main(String[] args) {

        final Properties appProps = new Properties();
        try(final InputStream inputStream = PrintEmAll.class.getResourceAsStream("/configuration.properties")) {
            appProps.load(inputStream);

            new PokemonRegistry(appProps.get("endpoint").toString()).fetch().stream().limit(10).forEach((p) -> {
                System.out.println(p.getNom() + " (" + p.getPv() + ")  -  " + p.getType());
            });
        } catch(IOException e) {
            e.printStackTrace(System.err);
        }
    }
}
