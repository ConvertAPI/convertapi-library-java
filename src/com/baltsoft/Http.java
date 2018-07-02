package com.baltsoft;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

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
}
