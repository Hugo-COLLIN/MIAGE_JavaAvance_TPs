
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;

import pokemon.decorators.CacheDecorator;
import pokemon.domain.Pokemon;
import pokemon.dto.ChartConfig;
import pokemon.dto.ChartConfig.ChartData;
import pokemon.dto.ChartConfig.ChartDataSet;
import pokemon.services.PokemonFetcher;
import pokemon.services.PokemonFetcherFactory;

/**
 * Classe de test pour la génération du graphe.
 */
public class GenerateGraph {
    public static void main(String[] args) throws Exception {

        final PokemonFetcher pf = new CacheDecorator(PokemonFetcherFactory.build(PokemonFetcherFactory.Provider.API));
        final List<Pokemon> liste = pf.fetch();

        // regroupement par type et dénombrement (Water = 112, Fighting = 27, ...)
        final Map<String, Long> byType = liste.stream().collect(Collectors.groupingBy(Pokemon::getType, Collectors.counting()));
    
        // connstitution de la _query string_
        final StringBuilder apiEndpoint = new StringBuilder(1024);
        apiEndpoint.append("https://quickchart.io/chart?c=");
    
        // récupération des clés en liste, puis des valeurs associées dans une seconde liste parallèle
        final List<String> labels = byType.keySet().stream().collect(Collectors.toList());
        final List<Long> values = labels.stream().map(l -> byType.get(l)).collect(Collectors.toList());

        final ChartConfig config = new ChartConfig(
            // type de graphe
            ChartConfig.ChartType.BAR,
            // données du graphe
            new ChartData(labels, Arrays.asList( new ChartDataSet("Par types", values) ))
        );

        // on utilise le fait que « List<String> l = Arrays.toList("a","b","c"); l.toString() = "[a, b, c]"; » :-)
        //sb.append("{type:'bar',data:{labels:[" + labels.stream().map(l -> "'"+l+"'").collect(Collectors.joining(",")) + "],datasets:[{label:'Types',data:" + values + "}]}}");

        final Gson gson = new GsonBuilder()
            .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
            .create();

        apiEndpoint.append( URLEncoder.encode(gson.toJson(config), "utf-8") );

        try (final BufferedInputStream in = new BufferedInputStream(new URI(apiEndpoint.toString()).toURL().openStream());
                final FileOutputStream fileOutputStream = new FileOutputStream("target/chart.png")) {

            final byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }

            String [] cmds = { "open", new File("target/chart.png").getAbsolutePath() };
	        Runtime.getRuntime().exec(cmds);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
          
    }
}
