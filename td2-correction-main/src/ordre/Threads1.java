package ordre;

public class Threads1 {

    private static class InheritedThread extends Thread {
        @Override
        public void run() {
            System.out.println("world");
        }
    }

    public static void main(String[] args) {
        new Thread(() -> { System.out.print("hello "); }).start();
        new InheritedThread().start();
    }
}