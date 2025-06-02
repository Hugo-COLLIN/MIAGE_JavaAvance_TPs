package pokemon.decorators;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;

import pokemon.domain.Pokemon;
import pokemon.services.PokemonFetcher;

public class CacheDecorator implements PokemonFetcher {

    private final PokemonFetcher implementation;
    
    public CacheDecorator(PokemonFetcher implementation) {
        this.implementation = implementation;
    }

    @Override
    public List<Pokemon> fetch() {
        final List<Pokemon> data = this.implementation.fetch();
        try {
            final Gson gson = new GsonBuilder().setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE).create();
            try(final OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("target/pokemons.json"), StandardCharsets.UTF_8)) {
                gson.toJson(data, writer);
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        return data;
    }
}
