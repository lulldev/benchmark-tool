package javacore.benchmarktool.http;

import java.net.HttpURLConnection;
import java.time.Duration;

public class HttpRequestListenerImpl implements HttpRequestListener {

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
