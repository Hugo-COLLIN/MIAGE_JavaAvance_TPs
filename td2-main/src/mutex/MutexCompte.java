package mutex;

import java.util.stream.IntStream;

public class MutexCompte {
    public static void main(String[] args) {
        final Compte compte = new Compte(42);

        System.out.println("solde début : " + compte.consulter());
        IntStream.range(0, 42).mapToObj(i -> new Thread(() -> {
            // ... diverses opérations sur le compte ...
            compte.retirer(1);
        })).forEach(t -> t.start());
        System.out.println("solde fin : " + compte.consulter());

    }

    public static class Compte {
        private int solde;
        public Compte(int i) { solde = i; }
        public void deposer(int montant) { solde = solde + montant; }
        public void retirer(int montant) { solde = solde - montant; }
        public int consulter() { return solde; }
    }
}

// Les threads ne sont pas terminés et exécutés au même moment, certaines valeurs sont
// prises alors que le thread qui traitait la donnée n'a pas encore fini son opération.
