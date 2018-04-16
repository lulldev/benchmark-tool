package javacore.benchmarktool.http;

import java.net.URL;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HttpRequestPool {
    
    private final HttpRequestListener reqListener;
    private final HttpConnection httpConnect;
    private final ExecutorService executorService;

    public HttpRequestPool(HttpConnection httpConnect, HttpRequestListener reqListener, int concurrency) {
        // todo: validate
//        assert concurrency > 0;
        this.httpConnect = httpConnect;
        this.reqListener = reqListener;
        this.executorService = Executors.newFixedThreadPool(concurrency);
    }

    public void runRequests(URL url, int requestCount) {
        final long startTimeNano = System.nanoTime();
        for (int i = 0; i < requestCount; ++i) {
            HttpRequestHandler task = new HttpRequestHandler(url, this.httpConnect, this.reqListener);
            this.executorService.execute(task);
        }
        await();

        this.reqListener.setTotalDuration(Duration.ofNanos(System.nanoTime() - startTimeNano));
    }

    private void await() {
        this.executorService.shutdown();
        try {
            this.executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            this.executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
