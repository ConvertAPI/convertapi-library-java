package com.baltsoft;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

public class Http {
    private static OkHttpClient client = new OkHttpClient();

    public static OkHttpClient getClient() {
        return client;
    }

    public static HttpUrl.Builder getUrlBuilder(Config config) {
        return new HttpUrl.Builder()
                .scheme(config.getScheme())
                .host(config.getHost())
                .addQueryParameter("timeout", String.valueOf(config.getTimeout()))
                .addQueryParameter("secret", config.getSecret());
    }

    public static CompletableFuture<InputStream> requestGet(String url) {
        return CompletableFuture.supplyAsync(() -> {
            Request request = new Request.Builder().url(url).build();
            Response response = null;
            try {
                response = getClient().newCall(request).execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return response.body().byteStream();
        });
    }
}