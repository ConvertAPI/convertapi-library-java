package com.convertapi;

public class Config {
    private static String defaultSecret;
    private static final String SCHEME = "https";
    @SuppressWarnings("SpellCheckingInspection")
    private static final String HOST = "v2.convertapi.com";
    private static final int TIMEOUT = 180;
    private final String scheme;
    private final String host;
    private final String secret;
    private final int timeout;

    @SuppressWarnings("unused")
    public Config(String secret, String scheme, String host, int timeout) {
        this.scheme = scheme;
        this.host = host;
        this.secret = secret;
        this.timeout = timeout;
    }

    @SuppressWarnings("WeakerAccess")
    public static Config defaults() {
        return new Config(Config.defaultSecret, SCHEME, HOST, TIMEOUT);
    }

    @SuppressWarnings("WeakerAccess")
    public static Config defaults(String secret) {
        return new Config(secret, SCHEME, HOST, TIMEOUT);
    }

    @SuppressWarnings("unused")
    public static void setDefaultSecret(String defaultSecret) {
        Config.defaultSecret = defaultSecret;
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
}