package javacore.benchmarktool.http;

import java.net.HttpURLConnection;
import java.net.URL;

public interface HttpConnection { HttpURLConnection open(URL url); }
