import lib.Json;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");

        System.out.println(new Json().write(42));
        System.out.println(new Json().write(3.14f));

        System.out.println(new Json().write("Hello, World!"));
        System.out.println(new Json().write(true));

        System.out.println(new Json().write(new Object[] { 1, 2, 3 }));
        System.out.println(new Json().write(new Object[] { "Hello", "World" }));
    }
}
