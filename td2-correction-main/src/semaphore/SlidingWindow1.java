package semaphore;

import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SlidingWindow1 {

    // https://medium.com/@ashaymotiwala/tackling-the-bounded-buffer-problem-aka-producer-consumer-problem-using-semaphores-42b4b22f2448

    // pas de problème d'accès concurrent avec ce pattern car les deux threads
    // (lecture/écriture) n'accèdent jamais à la même case en même temps
    // en revanche, cette implémentation 👇 n'est pas thread safe !
    // la mise à jour des index n'étant pas atomique, on aura des collisions
    // si on utilise pliusieurs threads de lecture et/ou d'écriture
    
    private final Semaphore count = new Semaphore(0);
    private final Semaphore limit;
    
    private final char[] buffer;

    private int readIndex = 0;
    private int writeIndex = 0;

    public SlidingWindow1(int limit) {
        this.limit = new Semaphore(limit);
        this.buffer = new char[limit];
    }

    public void add(char c) throws InterruptedException {
        System.out.println("> writer is adding");
        this.limit.acquire();
        try {
            this.buffer[writeIndex] = c;
            System.out.println("> wrote " + c + " at " + writeIndex);
            //writeIndex = (++writeIndex % buffer.length);
            if(++writeIndex >= buffer.length) writeIndex = 0;
        } finally {
            // toujours dnas le bloc finally ^^
            this.count.release();
        }
        System.out.println("> writer is released");
    }

    public char remove() throws InterruptedException {
        System.out.println("< reader is blocked");
        this.count.acquire();
        try {
            char c = this.buffer[readIndex];
            System.out.println("< read " + c + " at " + readIndex);
            //readIndex = (++readIndex % buffer.length);
            if(++readIndex >= buffer.length) readIndex = 0;
            System.out.println("< reader is released");
            return c;
        } finally {
            // toujours dnas le bloc finally ^^
            this.limit.release();
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
