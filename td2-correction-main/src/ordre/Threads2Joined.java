package ordre;
import java.util.Arrays;

public class Threads2Joined {

    public static void main(String[] args) {

        /*
        final String[] str = "hello world :-)".split("");

        // on convertir la chaîne découpée en liste de threads
        // chauqe thread écrit une lettre sur la sortie standard
        final List<Thread> threads = Arrays.stream(str).map(c -> new Thread(() -> {
            // l'attente permet juste de visualiser la progression de l'affichage
            try { Thread.sleep(300); } catch(InterruptedException e) {}
            System.out.print(c);
        })).collect(Collectors.toList());

        // on démarre le premier thread
        threads.get(0).start();
        for(int k=1; k<str.length; k++) {
            // chaque thread attend son prédécesseur
            // avant de démmarrer à son tour
            try { threads.get(k-1).join(); } catch(InterruptedException e) {}
            threads.get(k).start();
        }
        */

        final Thread identity = new Thread();
        identity.start();

        // ici on crée tous les threads dès le début (mais sans les démarrer), et on va faire que chaque thread
        // attende la fin du précédent avant de démarrer à son tour (grâce à l'opérateur `reduce`)
        Arrays.stream("hello world :-)".split("")).map(c -> new Thread(() -> { 
            try { Thread.sleep(300); } catch(InterruptedException e) {}
            System.out.print(c);
        })).reduce(identity, (p, c) -> {
            // attente du précédent, démarrage du courant
            try { p.join(); } catch(InterruptedException e) {}
            c.start();
            return c;
        });

    }

}