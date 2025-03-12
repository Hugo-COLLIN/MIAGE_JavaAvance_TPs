public class Ordre2 {
    public static void main(String[] args) throws InterruptedException {
        String message = "hello world :-\\)";
        int n = message.length();

        Thread[] threads = new Thread[n];

        for (int i=0;i<n;i++) {
            final char letter = message.charAt(i);
            threads[i] = new Thread(() -> System.out.print(letter));
        }

        for (Thread thread: threads) {
            thread.start();
            thread.join();
        }
    }
}

// Ici, le résultat n'est pas consistant :
// il y a beaucoup plus de threads et on se sait pas comment l'OS les ordonnance.


// Pour avoir un affichage correct, on applique .join() sur le thread après l'avoir démarré.
