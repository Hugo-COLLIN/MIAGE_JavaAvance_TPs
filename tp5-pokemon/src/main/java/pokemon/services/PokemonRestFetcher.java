package pokemon.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.StatusLine;
import pokemon.domain.Pokemon;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class PokemonRestFetcher implements PokemonFetcher {
    private static final String API_URL = "https://tyradex.vercel.app/api/v1/pokemon";
    private final Gson gson = new Gson();

    @Override
    public List<Pokemon> fetch() {
        try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {
            final HttpGet httpget = new HttpGet(API_URL);

            System.out.println("Executing request " + httpget.getMethod() + " " + httpget.getUri());

            final Result result = httpclient.execute(httpget, response -> {
                System.out.println("----------------------------------------");
                System.out.println(httpget + "->" + new StatusLine(response));
                // Process response message and convert it into a value object
                return new Result(response.getCode(), EntityUtils.toString(response.getEntity()));
            });
            System.out.println(result);

            if (result.status == 200)
                return gson.fromJson(result.content, new TypeToken<List<Pokemon>>() {}.getType());
            else
                throw new RuntimeException("Failed to fetch Pokemon data. Status code: " + result.status);

            } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    record Result(int status, String content) {

    }
}
