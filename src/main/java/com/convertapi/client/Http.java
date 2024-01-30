package com.convertapi.client;

import com.convertapi.client.model.RemoteUploadResponse;
import com.google.gson.Gson;
import okhttp3.*;

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
        int timeout = config.getTimeout() > 0 ? config.getTimeout() + 5 : 0;
        return config.getHttpClientBuilder()
            .apply(getClient().newBuilder())
            .readTimeout(timeout, TimeUnit.SECONDS)
            .build();
    }

    static HttpUrl.Builder getUrlBuilder(Config config) {
        HttpUrl.Builder urlBuilder = new HttpUrl.Builder()
            .scheme(config.getScheme())
            .host(config.getHost());

        if (config.getSecret() != null) {
            return urlBuilder.addQueryParameter("secret", config.getSecret());
        } else {
            return urlBuilder
                .addQueryParameter("token", config.getToken())
                .addQueryParameter("apikey", config.getApiKey());
        }
    }

    static CompletableFuture<InputStream> requestGet(String url) {
        return CompletableFuture.supplyAsync(() -> {
            Request request = getRequestBuilder().url(url).build();
            try {
                Response response = getClient().newCall(request).execute();
                ResponseBody body = response.body();
                if (body != null) {
                    if (response.code() != 200) {
                        throw new ConversionException(body.string(), response.code());
                    }
                    return body.byteStream();
                } else {
                    throw new ConversionException("Response body is empty", response.code());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    static CompletableFuture<Void> requestDelete(String url) {
        return CompletableFuture.supplyAsync(() -> {
            Request request = getRequestBuilder().delete().url(url).build();
            try (Response response = getClient().newCall(request).execute()) {
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    static Request.Builder getRequestBuilder() {
        String agent = String.format("ConvertAPI-Java/%s (%s)", Http.class.getPackage().getImplementationVersion(), System.getProperty("os.name"));
        return new Request.Builder().header("User-Agent", agent);
    }

    static RemoteUploadResponse remoteUpload(String urlToFile, Config config) {
        HttpUrl url = Http.getUrlBuilder(config)
            .addPathSegment("upload-from-url")
            .addQueryParameter("url", urlToFile)
            .build();

        Request request = Http.getRequestBuilder()
                .url(url)
                .method("POST", RequestBody.create("", null))
            .addHeader("Accept", "application/json")
            .build();

        String bodyString;
        try (Response response = Http.getClient().newCall(request).execute()) {
            //noinspection ConstantConditions
            bodyString = response.body().string();
            if (response.code() != 200) {
                throw new ConversionException(bodyString, response.code());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new Gson().fromJson(bodyString, RemoteUploadResponse.class);
    }
}
