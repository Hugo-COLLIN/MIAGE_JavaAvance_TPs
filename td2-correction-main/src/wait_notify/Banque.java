package wait_notify;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Banque {

    public static class Compte {
        protected int solde;
        public Compte(int solde) { this.solde = solde; }
        public synchronized void deposer(int montant) { solde = solde + montant; }
        public synchronized void retirer(int montant) { solde = solde - montant; }
        public int consulter() { return solde; }
    }

    // cette version utilise le même moniteur pour le dépôt et le retrait
    // ce qui réduit son efficacité : l'appel à notifyAll réveille
    // **tous** les threads, ceux qui attendent pour retirer aussi bien
    // que ceux qui attendent pour alimenter
    // dnas une version optimisée, on pourrait utiliser deux moniteurs différents,
    // un pour lier l'attente de dépôt au retrait, et l'autre pour lier
    // l'attente de retrtait au dépôt ... avis aux amateurs de défis ;-)

    public static class LivretA extends Compte {
        public LivretA(int solde) {
            super(solde);
        }
        public synchronized void deposer(int montant) {
            if(montant > 1000) {
                // le solde ne pouvant jamais devenir négatif,
                // on doit ajouter cette condition pour éviter de
                // rester bloqué sur la condition suivante (deadlock)
                throw new IllegalArgumentException("montant trop élevé");
            }
            // `if` devient `while` avec wait/notify
            while(solde + montant > 1000) {
                System.out.println("Solde trop haut, " + Thread.currentThread().getName() + " bloqué ! solde = " + solde);
                try { 
                    wait();
                } catch(InterruptedException e) { e.printStackTrace(); }
            }
            super.deposer(montant);
            // réveille tout le monde, auussi bien
            // ceux qui attendent pour déposer que
            // ceux aui attendent pour retirer
            notifyAll();
            System.out.println(Thread.currentThread().getName() + " a notifié ! solde = " + solde);
        }
        public synchronized void retirer(int montant) {
            while(solde < montant) {
                System.out.println("Solde trop bas, " + Thread.currentThread().getName() + " bloqué ! solde = " + solde);
                try { 
                    wait();
                } catch(InterruptedException e) { e.printStackTrace(); }
            }
            super.retirer(montant);
            notifyAll();
            System.out.println(Thread.currentThread().getName() + " exécuté ! solde = " + solde);
        }
    }

    public static void main(String[] args) {
        final Compte compte = new LivretA(20);
        System.out.println("solde début : " + compte.consulter());

        final ExecutorService executor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());

        executor.submit(() -> { 
            compte.retirer(30); 
        });
        executor.submit(() -> { 
            compte.retirer(30); 
        });
        executor.submit(() -> { 
            try { Thread.sleep(1000); } catch(InterruptedException e) {}
            compte.deposer(40);
        });

        executor.shutdown();
        try { executor.awaitTermination(1, TimeUnit.MINUTES); } catch(InterruptedException ignore) {}
        System.out.println("solde fin : " + compte.consulter());
        // le solde doit être à zéro
    }

}
