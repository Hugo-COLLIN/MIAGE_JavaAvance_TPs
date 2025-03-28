package pokemon;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PokemonRegistryTest {

    private static String endpointUrl = "https://www.data.gouv.fr/fr/datasets/r/04df345d-03dd-45f3-aed2-bf3dd55201a2";

    private PokemonRegistry registry;
    private List<Pokemon> liste;

    @BeforeAll
    static void setup() {
        // nop
    }

    @BeforeEach
    void init() {
        registry = new PokemonRegistry(endpointUrl);
        liste = registry.fetch();
    }

    @Test
    @DisplayName("Check first pokemon")
    void testFirst() {
        assertEquals("Bulbasaur", liste.get(0).getNom());
    }

    @Test
    @DisplayName("Check last pokemon")
    void testLast() {
        assertEquals("Volcanion", liste.get(liste.size() - 1).getNom());
    }

    @AfterEach
    void tearDown() {
        // nop
    }

    @AfterAll
    static void done() {
        // nop
    }

}
