package structures;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Kafka {

    public static class Messages {
        // l'ArrayList n'est pas thread safe et ne tolère pas les accès concurrents
        private List<String> messages = new ArrayList<String>(10);
        public void put(String mesg) { this.messages.add(mesg); }
        public String get() { return this.messages.remove(0); }
    }

    public static class SafeMessages {
        // la blockingQueue gère les accès concurrents
        private BlockingQueue<String> messages = new LinkedBlockingQueue<String>(10);
        public void put(String mesg) { this.messages.offer(mesg); }
        public String get() throws InterruptedException { return this.messages.take(); }
    }

    public static void main(String[] args) {
        final SafeMessages messages = new SafeMessages();
        // remplacez la ligne ci-dessus 👆 par la ligne ci-dessous 👇 pour "voir" le problème de concurrence
        // final Messages messages = new Messages();

        final ExecutorService executor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());        
        IntStream.range(0, 100).mapToObj(i -> (Runnable)() -> {
            try { Thread.sleep((int)Math.round(Math.random() * 100)); } catch(InterruptedException ignore) {}
            try {
                if(i % 2 == 0) {
                    // les threads pairs lisent les messages
                    System.out.println(messages.get());
                } else {
                    // les threads impairs écrivent les messages
                    messages.put(Double.toString(Math.random()));
                }
            } catch(Exception e) {
                // avec une ArrayList<>, on va avoir des valeurs nulles et des exceptions 👇
                // java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0
                e.printStackTrace(System.err);
            }
        }).forEach(r -> executor.submit(r));

        executor.shutdown();
        try { executor.awaitTermination(1, TimeUnit.MINUTES); } catch(InterruptedException ignore) {}
    }

}