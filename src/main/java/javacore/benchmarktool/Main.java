package javacore.benchmarktool;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.time.Duration;
import java.net.URL;

import javacore.benchmarktool.http.DefaultConnectionFactory;
import javacore.benchmarktool.http.HttpRequestRunner;
import javacore.benchmarktool.http.HttpConnectionFactory;
import javacore.benchmarktool.http.RequestListener;

class Request implements RequestListener {

    @Override
    public void onRequestComplete(Duration timeSpent, long transmittedByteCount, int httpStatusCode) {
        final boolean succeed =
                (httpStatusCode >= HttpURLConnection.HTTP_OK && httpStatusCode < HttpURLConnection.HTTP_BAD_REQUEST);
        synchronized (this) {
            System.out.println("succeed");
//            this.report.addRequest(succeed, transmittedByteCount, timeSpent);
        }
    }

    @Override
    public void onRequestTimeout() {
        System.out.println("timeout");
//        report.addRequestKilledByTimeout();
    }

    @Override
    public void onRequestError(RuntimeException ex) {
        synchronized (this) {
            System.out.println("req err");
//            this.lastException = ex;
        }
    }

    @Override
    public void setTotalDuration(Duration duration) {
        System.out.println("total duration");
//        this.report.setTotalDuration(duration);
    }
}

class Main {

    public static void main(String[] arguments) {

        Duration timeoutMilliseconds = Duration.ZERO;
        String url = "https://www.test.com";

        try {
            URL targetUrl = new URL(url);

            final Request reqListener = new Request();
            final HttpConnectionFactory connectionFactory = new DefaultConnectionFactory(timeoutMilliseconds);
            final HttpRequestRunner runner = new HttpRequestRunner(connectionFactory, reqListener, 5);

            runner.requestUrlMultipleTimes(targetUrl, 10);
        } catch (MalformedURLException e) {
        } catch (Exception e) {
        }

    }
}
