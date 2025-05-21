package pkg.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.google.gson.reflect.TypeToken;

/**
 * Implémentation du service qui permet de récupérer une liste
 * de boisson caféinées en utilisant une api public.
 * @see <a href="https://api.sampleapis.com">Sample APIs</a>
 */
public class CoffeeListImpl {

    /**
     * L'adresse du service.
     */
    private static final String ENDPOINT_URL = "https://api.sampleapis.com/coffee/" /* + "hot" ou "cold" */;

    /**
     * Le (dé)sérialiseur json.
     */
    private static final Gson gson = new GsonBuilder()
            .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
            .create();

    /** No-arg constructor. */
    public CoffeeListImpl() {}

    /**
     * Renvoie la liste des boissons caféinées.
     * @param type Le type de boisson, parmi "HOT, et "COLD".
     * @return La liste.
     * @throws InvalidArgumentException si le type est incorrect.
     */
    public List<CaffeinatedBeverage> listCaffeinatedBeverage(String type) {
        throw new UnsupportedOperationException(); // TODO
    }

    // structure de la réponse obtenue de l'api 
    // utilisée pour la déserialisation
    private static record Element(String title, String description, String image, String[] ingredients, long id) {}

    /**
     * Données exposées au monde extérieur.
     * @param id L'identifiant de la boisson.
     * @param name Le nom de la boisson.
     * @param description La description de la boisson.
     * @param price Le prix de la boisson en euros.
     */
    public static record CaffeinatedBeverage(long id, String name, String description, String image, float price) {}

}
