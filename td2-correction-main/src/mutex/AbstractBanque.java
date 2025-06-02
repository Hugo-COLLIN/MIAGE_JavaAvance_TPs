package mutex;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public abstract class AbstractBanque {

    public interface Compte {
        void deposer(int montant);
        void retirer(int montant);
        int consulter();
    }

    /** Execute 10000 opérations de dépôt/retrait sur le compte. */
    protected static void runManyThreads(Compte compte) {
        final long now = System.currentTimeMillis();
        System.out.println("solde début : " + compte.consulter());

        // on utilise l'exécuteur le plus polyvalment qui cherche avant tout
        // à réutiliser les threads, et en dernier recours en alloue un nouveau
        final ExecutorService executor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());
        // remplacez la ligne ci-dessus 👆 par la ligne ci-dessous 👇 pour voir l'effet d'une allocation de thread par tâche
        // final ExecutorService executor = Executors.newThreadPerTaskExecutor(Executors.defaultThreadFactory());

        IntStream.range(0, 10000).mapToObj(i -> (Runnable)() -> {
            // alétoirise l'ordre d'exécution des opérations de dépôt et de retrait
            try { Thread.sleep((int)Math.round(Math.random() * 100)); } catch(InterruptedException ignore) {}

            if(i % 2 == 0) {
                compte.retirer(30);
            } else {
                compte.deposer(30);
            }

        }).forEach(r -> executor.submit(r));

        executor.shutdown();
        try { executor.awaitTermination(1, TimeUnit.MINUTES); } catch(InterruptedException ignore) {}
        System.out.println("solde fin : " + compte.consulter());

        final long ellapsed = (System.currentTimeMillis() - now);
        System.out.println("temps (ms.) : " + ellapsed);
    }

}