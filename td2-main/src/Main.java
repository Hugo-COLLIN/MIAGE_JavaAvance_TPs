public class Main {
    public static void main(String[] args) {
//        Thread hello = Thread.startVirtualThread(new Hello());
        Thread hello = new Thread(new Hello());

        Thread world = new World();

        hello.start();
        world.start();

    }
}


class Hello implements Runnable {
    @Override
    public void run() {
        System.out.println("hello");
    }
}

class World extends Thread {
    @Override
    public void run() {
        System.out.println("world");
    }
}
