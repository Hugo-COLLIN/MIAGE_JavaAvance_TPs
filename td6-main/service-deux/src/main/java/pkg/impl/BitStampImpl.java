package pkg.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;

/**
 * Implémentation du service qui récupère le cours du bitcoin
 * en utilisant une api exposée par BitStamp.
 * @see <a href="https://www.bitstamp.net">BitStamp</a>
 */
public class BitStampImpl {

    /**
     * L'adresse du service. Le placeholder doit être remplacés par la devise (usd, eur ou gbp).
     */
    private static final String ENDPOINT_URL = "https://www.bitstamp.net/api/v2/ticker/btc%s/";
    
    /**
     * Le (dé)sérialiseur json.
     */
    private static final Gson gson = new GsonBuilder()
            .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
            .create();

    /** No-arg constructor. */
    public BitStampImpl() {}

    /**
     * Recherche le taux de conversion du bitcoin dans la devise spécifiée.
     * @param currency La devise demandée parmi "GBP", "USD" et "EUR".
     * @return Le taux de change pour la device demandée.
     * @throws IllegalArgumentException si la devise est incorrecte.
     */
    public BtcExchangeRate btcExchangeRate(String currency) {
        throw new UnsupportedOperationException(); // TODO
    }

    // structure (minimaliste) de la réponse obtenue de l'api BitStamp
    // utilisée pour la déserialisation du flux json reçu en beans
    private static record Payload(long timestamp, long last) {}

    /**
     * Données exposées au monde extérieur.
     * @param date_maj La date de mise-à-jour.
     * @param current_rate Le taux de conversion à la date.
     */
    public static record BtcExchangeRate(String date_maj, float current_rate) {}

}
