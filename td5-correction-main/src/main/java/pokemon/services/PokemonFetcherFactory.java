package pokemon.services;

import java.util.HashMap;
import java.util.Map;

import pokemon.services.impl.PokemonFileFetcher;
import pokemon.services.impl.PokemonRestFetcher;

public class PokemonFetcherFactory {

    private static final Map<Provider, Class<? extends PokemonFetcher>> providers = new HashMap<>();
    
    static {
        providers.put(Provider.API, PokemonRestFetcher.class);
        providers.put(Provider.FILE, PokemonFileFetcher.class);
    }

    /**
     * Version sans `if` ni `switch`.
     * @param provider Le provider.
     * @return L'instance.
     */
    public static PokemonFetcher build(Provider provider) {
        final Class<? extends PokemonFetcher> clazz = providers.get(provider);
        if(clazz == null) {
            throw new UnsupportedOperationException("provider inconnu");
        }
        try {
            // on utilise le constructeur par défaut (i.e. sans argument)
            return clazz.getDeclaredConstructor().newInstance();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*public static PokemonFetcher build(Provider provider) {
        switch(provider) {
            case API: 
                return new PokemonRestFetcher();
            case FILE:
                return new PokemonFileFetcher();
            default:
                throw new UnsupportedOperationException("provider inconnu");
        }
    }*/

    public static enum Provider {
        API, FILE
    }

}
