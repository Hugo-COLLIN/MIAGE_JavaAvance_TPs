package pkg;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

import pkg.handlers.HelloWorldHandler;

public class WebServer {

    public static void main(String[] args) throws Exception {
        final HttpServer server = HttpServer.create(new InetSocketAddress(3000), 0);
        server.createContext("/", new HelloWorldHandler());
        // l'exécuteur par défaut exécute les requêtes dans le main thread
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
    }

}
