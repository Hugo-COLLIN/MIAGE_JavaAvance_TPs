import lib.Json;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");

        System.out.println(new Json().write(42));
        System.out.println(new Json().write(3.14f));

    }
}
