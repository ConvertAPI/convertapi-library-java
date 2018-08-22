package com.convertapi;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

class Http {
    private static final OkHttpClient client = new OkHttpClient();

    static OkHttpClient getClient() {
        return client;
    }

    static OkHttpClient getClient(Config config) {
        return config.getHttpClientBuilder()
                .apply(getClient().newBuilder())
                .readTimeout(config.getTimeout() + 5, TimeUnit.SECONDS)
                .build();
    }

    static HttpUrl.Builder getUrlBuilder(Config config) {
        return new HttpUrl.Builder()
                .scheme(config.getScheme())
                .host(config.getHost())
                .addQueryParameter("timeout", String.valueOf(config.getTimeout()))
                .addQueryParameter("secret", config.getSecret());
    }

    static CompletableFuture<InputStream> requestGet(String url) {
        return CompletableFuture.supplyAsync(() -> {
            Request request = getRequestBuilder().url(url).build();
            Response response;
            try {
                response = getClient().newCall(request).execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //noinspection ConstantConditions
            return response.body().byteStream();
        });
    }

    static CompletableFuture<Void> requestDelete(String url) {
        return CompletableFuture.supplyAsync(() -> {
            Request request = getRequestBuilder().delete().url(url).build();
            try {
                getClient().newCall(request).execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }

    static Request.Builder getRequestBuilder() {
        String agent = String.format("ConvertAPI-Java/%.1f (%s)", 1.5, System.getProperty("os.name"));
        return new Request.Builder().header("User-Agent", agent);
    }
}