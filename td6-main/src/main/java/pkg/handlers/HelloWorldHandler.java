package pkg.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Arrays;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class HelloWorldHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
        try {
            final String response = "Hello world :-)";
            //final String response = "Hello world 🙂";
            //final byte[] payload = response.getBytes(StandardCharsets.UTF_8);
            t.getResponseHeaders().put("Content-Type", Arrays.asList("text/plain; utf-8"));
            t.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
            //t.sendResponseHeaders(200, payload.length);
            if (!t.getRequestMethod().equals("HEAD")) {
                try (final OutputStream os = t.getResponseBody()) {
                    os.write(response.getBytes());
                    //os.write(payload);
                    os.close();
                }
            }
        } catch(Exception e) {
            e.printStackTrace(System.err);
            throw new IOException(e);
        }
    }
}
