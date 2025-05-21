package pokemon.services;

import java.util.List;

import pokemon.domain.Pokemon;

public interface PokemonFetcher {
    List<Pokemon> fetch();
}