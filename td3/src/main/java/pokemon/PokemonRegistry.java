package pokemon;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PokemonRegistry {

    private final String endpointUrl;

    public PokemonRegistry(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public List<Pokemon> fetch() throws URISyntaxException, IOException {
        try {
            final URL url = new URI(this.endpointUrl).toURL();
            try (final Reader reader = new InputStreamReader(url.openStream(), "UTF-8");
                 final CSVParser parser = CSVFormat.DEFAULT.builder()
                         .setSkipHeaderRecord(true)
                         .setDelimiter(';')
                         .build()
                         .parse(reader)) {

                // on zappe les deux premières lignes
                return parser.stream().skip(2).map(record -> {
                    final int hitPoints = Integer.parseInt(record.get(5));
                    return PokemonBuilder.newPokemon().setNom(record.get(1)).setPv(hitPoints).setType(record.get(2)).build();
                }).collect(Collectors.toList());
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
