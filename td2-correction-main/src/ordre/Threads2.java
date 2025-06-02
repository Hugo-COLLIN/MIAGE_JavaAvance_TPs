package ordre;
import java.util.Arrays;

public class Threads2 {

    public static void main(String[] args) {
        /*
        final String[] str = "hello world :-)".split("");
        for(int k=0; k<str.length; k++) {
            final String c = str[k];
            new Thread(() -> { System.out.print(c); }).start();
        }
        */        

        Arrays.stream( "hello world :-)".split("") )
            .map(c -> new Thread(() -> { 
                // on peut augmenter l'aléatoirisme en ajoutant une pause 👇
                //try { Thread.sleep(200); } catch(InterruptedException e) {}
                System.out.print(c);
            })).forEach(Thread::start);
        
    }

}