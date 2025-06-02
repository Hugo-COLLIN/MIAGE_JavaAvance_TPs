package semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SlidingWindow3 {

    // ici la sécurisation de l'évolution des index est assurée par
    // l'utilisation d'un AtomicInteger et de ses méthodes atomiques ^^
    
    private final Semaphore count = new Semaphore(0);
    private final Semaphore limit;
    
    private final char[] buffer;

    private AtomicInteger readIndex = new AtomicInteger(0);
    private AtomicInteger writeIndex = new AtomicInteger(0);

    public SlidingWindow3(int limit) {
        this.limit = new Semaphore(limit);
        this.buffer = new char[limit];
    }

    public void add(char c) throws InterruptedException {
        System.out.println("> writer is adding");
        this.limit.acquire();
        try {
            final int index = writeIndex.getAndUpdate((val) -> (++val % buffer.length));
            this.buffer[index] = c;
            System.out.println("> wrote " + c + " at " + writeIndex);
        } finally {
            this.count.release();
        }
        System.out.println("> writer is released");
    }

    public char remove() throws InterruptedException {
        System.out.println("< reader is blocked");
        this.count.acquire();
        try {
            final int index = readIndex.getAndUpdate((val) -> (++val % buffer.length));
            final char c = this.buffer[index];
            System.out.println("< read " + c + " at " + readIndex);
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
