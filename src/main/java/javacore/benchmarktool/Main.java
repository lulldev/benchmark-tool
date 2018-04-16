package javacore.benchmarktool;

import java.net.MalformedURLException;
import java.time.Duration;
import java.net.URL;

import javacore.benchmarktool.http.HttpConnection;
import javacore.benchmarktool.http.HttpConnectionImpl;
import javacore.benchmarktool.http.HttpRequestListener;
import javacore.benchmarktool.http.HttpRequestListenerImpl;
import javacore.benchmarktool.http.HttpRequestPool;

class Main {

    public static void main(String[] arguments) {

        Duration timeoutMilliseconds = Duration.ZERO;
        String url = "https://www.test.com";

        try {
            URL targetUrl = new URL(url);

            final HttpRequestListener reqListener = new HttpRequestListenerImpl();
            final HttpConnection httpConnect = new HttpConnectionImpl(timeoutMilliseconds);
            final HttpRequestPool requestPool = new HttpRequestPool(httpConnect, reqListener, 5);
            requestPool.runRequests(targetUrl, 10);
        } catch (MalformedURLException e) {
        } catch (Exception e) {
        }

    }
}
