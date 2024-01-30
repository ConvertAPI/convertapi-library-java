package com.convertapi.client;

import okhttp3.OkHttpClient;

import java.util.function.Function;

public class Config {

    private static String defaultSecret;
    private static String defaultToken;
    private static String defaultApiKey;
    private static int defaultTimeout = 0;  // Infinite read waiting
    private static Function<OkHttpClient.Builder, OkHttpClient.Builder> defaultHttpClientBuilder = b -> b;
    private static final String SCHEME = "https";
    private static final String HOST = "v2.convertapi.com";
    private final String scheme;
    private final String host;
    private final String secret;
    private final String token;
    private final String apiKey;
    private final int timeout;
    private final Function<OkHttpClient.Builder, OkHttpClient.Builder> httpClientBuilder;

    @SuppressWarnings("unused")
    public Config(String secret, String scheme, String host, int timeout, Function<OkHttpClient.Builder, OkHttpClient.Builder> httpClientBuilder) {
        this.scheme = scheme;
        this.host = host;
        this.secret = secret;
        this.token = null;
        this.apiKey = null;
        this.timeout = timeout;
        this.httpClientBuilder = httpClientBuilder;
    }

    @SuppressWarnings("unused")
    public Config(String token, String apiKey, String scheme, String host, int timeout, Function<OkHttpClient.Builder, OkHttpClient.Builder> httpClientBuilder) {
        this.scheme = scheme;
        this.host = host;
        this.secret = null;
        this.token = token;
        this.apiKey = apiKey;
        this.timeout = timeout;
        this.httpClientBuilder = httpClientBuilder;
    }

    @SuppressWarnings("WeakerAccess")
    public static Config defaults() {
        if (Config.defaultSecret != null)
            return new Config(Config.defaultSecret, SCHEME, HOST, defaultTimeout, Config.defaultHttpClientBuilder);
        return new Config(Config.defaultToken, Config.defaultApiKey, SCHEME, HOST, defaultTimeout, Config.defaultHttpClientBuilder);
    }

    @SuppressWarnings("WeakerAccess")
    public static Config defaults(String secret) {
        return new Config(secret, SCHEME, HOST, defaultTimeout, Config.defaultHttpClientBuilder);
    }

    @SuppressWarnings("WeakerAccess")
    public static Config defaults(String token, String apiKey) {
        return new Config(token, apiKey, SCHEME, HOST, defaultTimeout, Config.defaultHttpClientBuilder);
    }

    @SuppressWarnings("WeakerAccess")
    public static Config defaults(String secret, Function<OkHttpClient.Builder, OkHttpClient.Builder> httpClientBuilder) {
        return new Config(secret, SCHEME, HOST, defaultTimeout, httpClientBuilder);
    }

    @SuppressWarnings("WeakerAccess")
    public static Config defaults(String token, String apiKey, Function<OkHttpClient.Builder, OkHttpClient.Builder> httpClientBuilder) {
        return new Config(token, apiKey, SCHEME, HOST, defaultTimeout, httpClientBuilder);
    }

    @SuppressWarnings("unused")
    public static void setDefaultSecret(String defaultSecret) {
        Config.defaultSecret = defaultSecret;
    }

    @SuppressWarnings("unused")
    public static void setDefaultToken(String defaultToken) {
        Config.defaultToken = defaultToken;
    }

    @SuppressWarnings("unused")
    public static void setDefaultApiKey(String defaultApiKey) {
        Config.defaultApiKey = defaultApiKey;
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

    String getSecret() {
        return secret;
    }

    String getToken() {
        return token;
    }

    String getApiKey() {
        return apiKey;
    }

    Function<OkHttpClient.Builder, OkHttpClient.Builder> getHttpClientBuilder() {
        return httpClientBuilder;
    }
}
