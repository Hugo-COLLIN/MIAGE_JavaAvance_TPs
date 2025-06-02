package mutex;

public class Banque1 extends AbstractBanque {

    private static class Compte1 implements Compte {
        private int solde;
        public Compte1(int solde) { this.solde = solde; }
        public void deposer(int montant) { solde = solde + montant; }
        public void retirer(int montant) { solde = solde - montant; }
        public int consulter() { return solde; }
    }

    public static void main(String[] args) {
        runManyThreads(new Compte1(100));
        // le solde en sortie sera aléatoire
    }

}
