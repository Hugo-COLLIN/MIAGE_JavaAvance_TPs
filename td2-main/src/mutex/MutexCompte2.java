package mutex;

import java.util.stream.IntStream;

public class MutexCompte2 {
    public static void main(String[] args) {
        final Compte compte = new Compte(100);

        System.out.println("solde début : " + compte.consulter());
        IntStream.range(0, 10000).mapToObj(i -> new Thread(() -> {
            if (i%2 == 0)
                compte.deposer(30);
            else
                compte.retirer(30);
        })).forEach(t -> t.start());
        System.out.println("solde fin : " + compte.consulter());

    }

    public static class Compte {
        private int solde;
        public Compte(int i) { solde = i; }
        public synchronized void deposer(int montant) { solde = solde + montant; }
        public synchronized void retirer(int montant) { solde = solde - montant; }
        public int consulter() { return solde; }
    }
}

// Le solde de fin devrait être identique au solde de début, soit 100.
// Or, il est variable (parfois positif parfois negatif, jamais le même) au fil des exécutions.
// Cela est dû au fait que les threads ne sont pas exécutés dans le bon ordre.

// Pour corriger, on ajoute synchronized

// le thread.sleep ne doit jamais résoudre des pb de synchro
