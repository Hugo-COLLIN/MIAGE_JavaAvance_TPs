package mutex;
import java.util.concurrent.atomic.AtomicInteger;

public class Banque3 extends AbstractBanque {

    private static class Compte3 implements Compte {
        private final AtomicInteger solde;
        public Compte3(int i) { solde = new AtomicInteger(i); }
        public void deposer(int montant) { solde.addAndGet(montant); }
        public void retirer(int montant) { solde.addAndGet(-montant); }
        public int consulter() { return solde.intValue(); }
    }

    public static void main(String[] args) {
        runManyThreads(new Compte3(100));
        // le solde en sortie sera éal à 100
    }

}
