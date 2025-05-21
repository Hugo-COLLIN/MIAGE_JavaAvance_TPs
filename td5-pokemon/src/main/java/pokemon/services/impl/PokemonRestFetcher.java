package pokemon.services.impl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.google.gson.reflect.TypeToken;

import pokemon.domain.Pokemon;
import pokemon.domain.PokemonBuilder;
import pokemon.services.PokemonFetcher;

public class PokemonRestFetcher implements PokemonFetcher {

    @Override
    public List<Pokemon> fetch() {
        try {
            try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {
                final HttpGet httpget = new HttpGet("https://tyradex.vercel.app/api/v1/pokemon");

                final List<Pokemon> result = httpclient.execute(httpget, response -> {
                    if(response.getCode() != 200) throw new RuntimeException("erreur de récupération");
                    try(final InputStream inputStream = response.getEntity().getContent()) {
                        return unmarshall(inputStream);
                    }
                });

                return result;
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    protected List<Pokemon> unmarshall(InputStream inputStream) {
        //final Gson gson = new Gson();
        final Gson gson = new GsonBuilder()
                .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
                .create();

        final List<Map<String, Object>> liste = gson.fromJson(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8),
                new TypeToken<List<Map<String, Object>>>() {}.getType()
        );

        return liste.stream()
                .map(el -> {
                    String pokedex_id = el.get("pokedex_id").toString();
                    if("0".equals(pokedex_id)) return null;

                    final String frenchName = ((Map<String, Object>)el.get("name")).get("fr").toString();
                    final String type = ((Map<String, Object>)((List<?>)el.get("types")).get(0)).get("name").toString();
                    final int hitPoints = Integer.parseInt(((Map<String, Object>)el.get("stats")).get("hp").toString());
                    return PokemonBuilder.newPokemon().setNom(frenchName).setType(type).setPv(hitPoints).build();
                })
                .filter(el -> el != null)
                .collect(Collectors.toList());
    }

}
