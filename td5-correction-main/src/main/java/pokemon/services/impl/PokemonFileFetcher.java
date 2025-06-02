package pokemon.services.impl;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.google.gson.reflect.TypeToken;

import pokemon.domain.Pokemon;
import pokemon.services.PokemonFetcher;

public class PokemonFileFetcher implements PokemonFetcher {
    
    @Override
    public List<Pokemon> fetch() {
        try {
            try(final InputStream inputStream = new FileInputStream("target/pokemons.json")) {

                final Gson gson = new GsonBuilder()
                    // désactive la conversion automatique en float
                    // au profit d'une conversion en int autant que possible
                    .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
                    .create();

                return gson.fromJson(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8),
                    new TypeToken<List<Pokemon>>() {}.getType()
                );

            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

}
