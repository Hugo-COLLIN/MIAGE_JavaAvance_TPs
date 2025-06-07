package tld.domain;

public class Semaphore {

    Semaphore s;

    int numJetons;

    /**
     * Initialize un sémaphore avec n jetons.
     */
    public Semaphore(int numJetons) {
        if (s == null)
            Semaphore s = new Semaphore(numJetons);
    }

    /**
     * Renvoie le nombre de jetons encore disponibles.
     * @return Le nombre de jetons disponibles.
     */
    public int availablePermits() {
        // TODO
        return 0;
    }

    /**
     * Demande un jeton. S'il est disponible, il est réservé
     * pour le thread appelant et la méthode retourne immédiatement.
     * Sinon, le thread appelant est mis en attente jusqu'à ce qu'un
     * jeton soit à nouveau disponible.
     */
    public void acquire() throws InterruptedException {
        // TODO
    }

    /**
     * Libère un jeton précédemment obtenu au travers
     * de la méthode {@link #acquire()}. Notifie les threads
     * en attente qu'au moins un jeton est disponible.
     */
    public void release() {
        // TODO
    }

}
