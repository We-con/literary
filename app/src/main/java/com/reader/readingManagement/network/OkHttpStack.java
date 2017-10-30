package com.reader.readingManagement.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HurlStack;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import org.apache.http.HttpResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class OkHttpStack extends HurlStack {
    private final OkHttpClient client;

    public OkHttpStack() {
        this(new OkHttpClient());
    }

    public OkHttpStack(OkHttpClient client) {
        if (client == null) {
            throw new NullPointerException("Http Client must not be null");
        }
        this.client = client;
        this.client.setSslSocketFactory(new NoSSLv3Factory());
    }

    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {
        return new OkUrlFactory(client).open(url);
    }

    @Override
    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
        return super.performRequest(request, additionalHeaders);
    }

}
