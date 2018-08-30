package com.convertapi;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("WeakerAccess")
public class Param {
    private final String name;
    private CompletableFuture<List<String>> value;
    private boolean isUploadedFile = false;

    @SuppressWarnings("unused")
    private Param(String name) {
        this.name = name.toLowerCase();
    }

    @SuppressWarnings("unused")
    public Param(String name, String value) {
        this(name.toLowerCase());
        List<String> valueList = new ArrayList<>();
        valueList.add(value);
        this.value = CompletableFuture.completedFuture(valueList);
    }

    @SuppressWarnings("unused")
    public Param(String name, int value) {
        this(name, String.valueOf(value));
    }

    @SuppressWarnings("unused")
    public Param(String name, BigDecimal value) {
        this(name, String.valueOf(value));
    }

    @SuppressWarnings("unused")
    public Param(String name, byte[] value, String fileFormat) {
        this(name, value, fileFormat, Config.defaults());
    }

    @SuppressWarnings("WeakerAccess")
    public Param(String name, byte[] value, String fileFormat, Config config) {
        this(name);
        String fileName = "getFile." + fileFormat;
        this.value = upload(value, fileName, config);
        isUploadedFile = true;
    }

    public Param(String name, Path value) throws IOException {
        this(name, value, Config.defaults());
    }

    @SuppressWarnings("WeakerAccess")
    public Param(String name, Path value, Config config) throws IOException {
        this(name);
        this.value = upload(Files.readAllBytes(value), value.getFileName().toString(), config);
        isUploadedFile = true;
    }

    @SuppressWarnings("unused")
    public Param(String name, ConversionResult value) {
        this(name, value, 0);
        this.value = CompletableFuture.completedFuture(value.urls());
    }

    @SuppressWarnings("WeakerAccess")
    public Param(String name, ConversionResult value, int fileIndex) {
        this(name);
        List<String> valueList = new ArrayList<>();
        valueList.add(value.getFile(fileIndex).getUrl());
        this.value = CompletableFuture.completedFuture(valueList);
    }

    @SuppressWarnings("WeakerAccess")
    public Param(String name, CompletableFuture<ConversionResult> value, int fileIndex) {
        this(name);
        this.value = value.thenApply((res) -> Collections.singletonList(res.getFile(fileIndex).getUrl()));
    }

    @SuppressWarnings("unused")
    public Param(String name, CompletableFuture<ConversionResult> value) {
        this(name);
        this.value = value.thenApply(ConversionResult::urls);
    }

    public String getName() {
        return name;
    }

    public List<String> getValue() throws ExecutionException, InterruptedException {
        return this.value.get();
    }

    public CompletableFuture<Void> delete() {
        return isUploadedFile
                ? value.thenCompose(urls -> Http.requestDelete(urls.get(0)))
                : CompletableFuture.completedFuture(null);
    }

    private static CompletableFuture<List<String>> upload(byte[] data, String fileName, Config config) {
        return CompletableFuture.supplyAsync(() -> {
            Request request = Http.getRequestBuilder()
                    .url(Http.getUrlBuilder(config).addPathSegment("upload")
                            .addQueryParameter("filename", fileName)
                            .build())
                    .post(RequestBody.create(MediaType.parse("application/octet-stream"), data))
                    .build();
            try {
                String id = Http.getClient().newCall(request).execute().body().string();
                //noinspection ConstantConditions
                return Collections.singletonList(id);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static Param[] concat(Param[] a, Param[] b) {
        Param[] result = new Param[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}