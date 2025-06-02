package mutex;

public class Banque2 extends AbstractBanque {

    private static class Compte2 implements Compte {
        private int solde;
        public Compte2(int i) { solde = i; }
        // pensez à sécuriser tous les chemins d'accès à la modification de la variable
        public synchronized void deposer(int montant) { solde = solde + montant; }
        public synchronized void retirer(int montant) { solde = solde - montant; }
        public int consulter() { return solde; }
    }

    public static void main(String[] args) {
        runManyThreads(new Compte2(100));
        // le solde en sortie sera éal à 100
    }

}
