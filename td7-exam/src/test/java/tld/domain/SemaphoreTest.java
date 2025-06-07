package tld.domain;

import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SemaphoreTest {

    private static final int NB_JETONS = 4;

    private Semaphore sem;

    @BeforeEach
    public void nouveauSemaphore() {
        sem = new Semaphore(NB_JETONS);
    }

    @Test
    @DisplayName("Utilisation basique")
    public void testUtilisationBasique() {
        // tester qu'un appel à acquire() suivi d'un appel
        // à release() ne jette pas d'exception et 
        // réinitialise bien le compteur de jetons
    }

    @Test
    @DisplayName("Utilisation concurrente")
    public void testUtilisationConcurrente() {
        // tester l'acquisition et la libération de verrou par 16 threads en parallèle
        // chaque thread ayant obtenu un jeton doit attendre avant de le libérer
        // utiliser pour cela l'instruction « Thread.sleep((int)Math.floor(Math.random() * 100)); »
        // le compteur de jetons ne doit jamais descendre en dessous de zéro,
        // ne doit jamais passer au dessus du nombre initial, et doit revenir
        // à sa valeur initiale une fois que tous les threads sont terminés
        IntStream.range(0, 16).forEach(i -> {
            // ...
        });
    }

}
