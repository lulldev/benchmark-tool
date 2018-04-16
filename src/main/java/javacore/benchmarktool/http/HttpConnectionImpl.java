package javacore.benchmarktool.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidParameterException;
import java.time.Duration;

public class HttpConnectionImpl implements HttpConnection {

    private final Duration timeout;

    public HttpConnectionImpl(Duration timeout) {
        this.timeout = timeout;
    }

    @Override
    public HttpURLConnection open(URL url) {
        final String protocol = url.getProtocol();

        if (!protocol.equals("http") && !protocol.equals("https")) {
            throw new InvalidParameterException("url must have http/https protocol: " + url.toString());
        }

        try {
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.setReadTimeout((int) timeout.toMillis());
            connect.setConnectTimeout((int) timeout.toMillis());
            connect.setRequestMethod("GET");
            connect.setUseCaches(false);
            return connect;
        } catch (IOException ex) {
            throw new RuntimeException("connection open error: " + ex.getMessage());
        }
    }
}
