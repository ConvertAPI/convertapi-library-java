package com.convertapi;

public class Config {
    private static String defaultSecret;
    private static final String SCHEME = "https";
    private static final String HOST = "v2.convertapi.com";
    private static final int TIMEOUT = 180;
    private String scheme;
    private String host;
    private String secret;
    private int timeout;

    public static void setDefaultSecret(String defaultSecret) {
        Config.defaultSecret = defaultSecret;
    }

    public static Config defaults() {
        return new Config(Config.defaultSecret, SCHEME, HOST, TIMEOUT);
    }

    public Config(String secret, String scheme, String host, int timeout) {
        this.scheme = scheme;
        this.host = host;
        this.secret = secret;
        this.timeout = timeout;
    }

    public int getTimeout() {
        return timeout;
    }

    public String getScheme() {
        return scheme;
    }

    public String getHost() {
        return host;
    }

    public String getSecret() {
        return secret;
    }
}