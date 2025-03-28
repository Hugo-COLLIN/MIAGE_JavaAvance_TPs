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

    private static PokemonRegistry registry;
    private List<Pokemon> liste;
    
    @BeforeAll
    static void setup() {
        registry = new PokemonRegistry("https://www.data.gouv.fr/fr/datasets/r/04df345d-03dd-45f3-aed2-bf3dd55201a2");
    }

    @BeforeEach
    void init() {
        liste = registry.fetch();
    }

    // TODO ajouter les tests ici

    @AfterEach
    void tearDown() {
        // nop
    }

    @AfterAll
    static void done() {
        // nop
    }

}
