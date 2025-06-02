package ordre;
import java.util.stream.IntStream;

public class Threads2Counter {

    public static void main(String[] args) {

        // conserve le numéro du thread à exécuter
        // (on utilise un tableau à un élément pour
        // pouvoir l'utiliser dans les lambdas)
        final int[] counter = new int[] { 0 };
        
        final String[] str = "hello world :-)".split("");

        /*
        for(int k=0; k<str.length; k++) {
            final int threadNumber = k;
            new Thread(() -> {
                // attente active tant que le thread
                // qui doit s'exécuter n'est pas moi
                while(counter[0] != threadNumber) {
                    try { Thread.sleep(300); } catch(InterruptedException ignore) {}
                }
                System.out.print(str[threadNumber]);
                // au suivant !
                counter[0]++;
            }).start();
        }
        */

        IntStream.range(0, str.length).mapToObj(i -> new Thread(() -> {
            while(counter[0] != i) {
                try { Thread.sleep(300); } catch(InterruptedException ignore) {}
            }
            System.out.print(str[i]);
            counter[0]++;
        })).forEach(t -> { t.start(); });    
    }

}