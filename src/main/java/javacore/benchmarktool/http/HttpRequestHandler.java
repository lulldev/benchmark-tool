package javacore.benchmarktool.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.SocketTimeoutException;
import java.time.Duration;

class HttpRequestHandler implements Runnable {
    private final HttpConnection httpConnection;
    private final HttpRequestListener listener;
    private final URL url;
    private long readByteCount;
    private int httpStatusCode;

    HttpRequestHandler(URL url, HttpConnection httpConnection, HttpRequestListener listener) {
        this.url = url;
        this.httpConnection = httpConnection;
        this.listener = listener;
    }

    public void run() {
        HttpURLConnection conn = null;
        try {
            final long startTimeNano = System.nanoTime();
            conn = httpConnection.open(url);
            readResponse(conn);
            final long durationNano = System.nanoTime() - startTimeNano;
            listener.onRequestComplete(Duration.ofNanos(durationNano), this.readByteCount, this.httpStatusCode);
        } catch (SocketTimeoutException e) {
            listener.onRequestTimeout();
        } catch (IOException e) {
            listener.onRequestError(new RuntimeException("got IOException", e));
        } catch (RuntimeException e) {
            listener.onRequestError(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private void readResponse(HttpURLConnection conn) throws IOException {
        InputStream in = conn.getInputStream();
        long maxSkipSize = Long.MAX_VALUE;
        this.readByteCount = in.skip(maxSkipSize);
        this.httpStatusCode = conn.getResponseCode();
    }
}
