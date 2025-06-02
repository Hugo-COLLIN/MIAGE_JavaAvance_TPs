package semaphore;

import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SlidingWindow2 {

    // ici la sécurisation de l'évolution des index est assurée
    // par un mutex (on aurait pu également utiliser un Semaphore(1))
    // ce qui fait que deux threads ne peuvent pas lire le même index
    // et donc pas modifier la même case
    
    private final Semaphore count = new Semaphore(0);
    private final Semaphore limit;
    
    private final char[] buffer;

    private int readIndex = 0;
    private int writeIndex = 0;

    public SlidingWindow2(int limit) {
        this.limit = new Semaphore(limit);
        this.buffer = new char[limit];
    }

    public void add(char c) throws InterruptedException {
        System.out.println("> writer is adding");
        this.limit.acquire();
        try {
            this.buffer[writeIndex] = c;
            System.out.println("> wrote " + c + " at " + writeIndex);
            synchronized(this) {
                writeIndex = (++writeIndex % buffer.length);
            }
        } finally {
            this.count.release();
        }
        System.out.println("> writer is released");
    }

    public char remove() throws InterruptedException {
        System.out.println("< reader is blocked");
        this.count.acquire();
        try {
            final char c = this.buffer[readIndex];
            System.out.println("< read " + c + " at " + readIndex);
            synchronized(this) {
                readIndex = (++readIndex % buffer.length);
            }
            return c;
        } finally {
            this.limit.release();
            System.out.println("< reader is released");
        }
    }

    public static void main(String[] args) {
        final SlidingWindow1 slidingWindow = new SlidingWindow1(5);

        new Thread(() -> {
            try {
                final StringBuilder buffer = new StringBuilder(32);
                char c = slidingWindow.remove();
                while(c != '\0') {
                    buffer.append(c);
                    c = slidingWindow.remove();
                }
                System.out.println(buffer);
            } catch(InterruptedException ignore) {}
        }).start();

        new Thread(() -> {
            final char[] characters = "hello world :-)\0".toCharArray();
            final Stream<Character> stream = IntStream.range(0, characters.length).mapToObj(i -> characters[i]);
            stream.forEach(c -> 
                {try { slidingWindow.add(c.charValue()); } catch(InterruptedException e) {}}
            );
        }).start();
    }

}
