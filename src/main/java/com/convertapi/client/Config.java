package com.convertapi.client;

import okhttp3.OkHttpClient;

import java.util.function.Function;

public class Config {

    private static String defaultApiCredentials;
    private static int defaultTimeout = 0;  // Infinite read waiting
    private static Function<OkHttpClient.Builder, OkHttpClient.Builder> defaultHttpClientBuilder = b -> b;
    private static final String SCHEME = "https";
    private static final String HOST = "v2.convertapi.com";
    private final String scheme;
    private final String host;
    private final String apiCredentials;
    private final int timeout;
    private final Function<OkHttpClient.Builder, OkHttpClient.Builder> httpClientBuilder;

    @SuppressWarnings("unused")
    public Config(String apiCredentials, String scheme, String host, int timeout, Function<OkHttpClient.Builder, OkHttpClient.Builder> httpClientBuilder) {
        this.scheme = scheme;
        this.host = host;
        this.apiCredentials = apiCredentials;
        this.timeout = timeout;
        this.httpClientBuilder = httpClientBuilder;
    }

    @SuppressWarnings("WeakerAccess")
    public static Config defaults() {
        return new Config(Config.defaultApiCredentials, SCHEME, HOST, defaultTimeout, Config.defaultHttpClientBuilder);
    }

    @SuppressWarnings("WeakerAccess")
    public static Config defaults(String apiCredentials) {
        return new Config(apiCredentials, SCHEME, HOST, defaultTimeout, Config.defaultHttpClientBuilder);
    }

    @SuppressWarnings("WeakerAccess")
    public static Config defaults(String apiCredentials, Function<OkHttpClient.Builder, OkHttpClient.Builder> httpClientBuilder) {
        return new Config(apiCredentials, SCHEME, HOST, defaultTimeout, httpClientBuilder);
    }

    @SuppressWarnings("unused")
    public static void setDefaultApiCredentials(String defaultApiCredentials) {
        Config.defaultApiCredentials = defaultApiCredentials;
    }

    @SuppressWarnings("unused")
    public static void setDefaultTimeout(int defaultTimeout) {
        Config.defaultTimeout = defaultTimeout;
    }

    @SuppressWarnings("unused")
    public static void setDefaultHttpBuilder(Function<OkHttpClient.Builder, OkHttpClient.Builder> httpClientBuilder) {
        Config.defaultHttpClientBuilder = httpClientBuilder;
    }

    int getTimeout() {
        return timeout;
    }

    String getScheme() {
        return scheme;
    }

    String getHost() {
        return host;
    }

    String getApiCredentials() {
        return apiCredentials;
    }

    Function<OkHttpClient.Builder, OkHttpClient.Builder> getHttpClientBuilder() {
        return httpClientBuilder;
    }
}
