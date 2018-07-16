package com.convertapi;

import okhttp3.OkHttpClient;

import java.util.function.Function;

public class Config {
    private static String defaultSecret;
    private static Function<OkHttpClient.Builder, OkHttpClient.Builder> defaultHttpClientBuilder = b -> b;
    private static final String SCHEME = "https";
    @SuppressWarnings("SpellCheckingInspection")
    private static final String HOST = "v2.convertapi.com";
    private static final int TIMEOUT = 180;
    private final String scheme;
    private final String host;
    private final String secret;
    private final int timeout;
    private final Function<OkHttpClient.Builder, OkHttpClient.Builder> httpClientBuilder;

    @SuppressWarnings("unused")
    public Config(String secret, String scheme, String host, int timeout, Function<OkHttpClient.Builder, OkHttpClient.Builder> httpClientBuilder) {
        this.scheme = scheme;
        this.host = host;
        this.secret = secret;
        this.timeout = timeout;
        this.httpClientBuilder = httpClientBuilder;
    }

    @SuppressWarnings("WeakerAccess")
    public static Config defaults() {
        return new Config(Config.defaultSecret, SCHEME, HOST, TIMEOUT, Config.defaultHttpClientBuilder);
    }

    @SuppressWarnings("WeakerAccess")
    public static Config defaults(String secret) {
        return new Config(secret, SCHEME, HOST, TIMEOUT, Config.defaultHttpClientBuilder);
    }

    @SuppressWarnings("WeakerAccess")
    public static Config defaults(String secret, Function<OkHttpClient.Builder, OkHttpClient.Builder> httpClientBuilder) {
        return new Config(secret, SCHEME, HOST, TIMEOUT, httpClientBuilder);
    }

    @SuppressWarnings("unused")
    public static void setDefaultSecret(String defaultSecret) {
        Config.defaultSecret = defaultSecret;
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

    String getSecret() {
        return secret;
    }

    Function<OkHttpClient.Builder, OkHttpClient.Builder> getHttpClientBuilder() {
        return httpClientBuilder;
    }
}