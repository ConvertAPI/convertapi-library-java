package com.convertapi;

import okhttp3.*;
import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Param {
    private String name;
    private CompletableFuture<List<String>> value;

    private Param(String name) {
        this.name = name.toLowerCase();
    }

    public Param(String name, String value) {
        this(name.toLowerCase());
        List<String> valueList = new ArrayList();
        valueList.add(value);
        this.value = CompletableFuture.completedFuture(valueList);
    }

    public Param(String name, int value) {
        this(name, String.valueOf(value));
    }

    public Param(String name, BigDecimal value) {
        this(name, String.valueOf(value));
    }

    public Param(String name, byte[] value, String fileFormat) {
        this(name, value, fileFormat, Config.defaults());
    }

    public Param(String name, byte[] value, String fileFormat, Config config) {
        this(name);
        String fileName = "getFile." + fileFormat;
        String contentTypeString = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(fileName);
        this.value = upload(value, fileName, MediaType.parse(contentTypeString), config);
    }

    public Param(String name, Path value) throws IOException {
        this(name, value, Config.defaults());
    }

    public Param(String name, Path value, Config config) throws IOException {
        this(name);
        String contentTypeString = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(value.toFile());
        this.value = upload(Files.readAllBytes(value), value.getFileName().toString(), MediaType.parse(contentTypeString), config);
    }

    public Param(String name, ConversionResult value) throws ExecutionException, InterruptedException {
        this(name, value, 0);
        this.value = CompletableFuture.completedFuture(value.urls());
    }

    public Param(String name, ConversionResult value, int fileIndex) throws ExecutionException, InterruptedException {
        this(name);
        List<String> valueList = new ArrayList();
        valueList.add(value.getFile(fileIndex).getUrl());
        this.value = CompletableFuture.completedFuture(valueList);
    }

    public Param(String name, CompletableFuture<ConversionResult> value) throws ExecutionException, InterruptedException {
        this(name);
        this.value = value.thenApply(r -> r.urls());
    }

    public String getName() {
        return name;
    }

    public List<String> getValue() throws ExecutionException, InterruptedException {
        return this.value.get();
    }

    private static CompletableFuture<List<String>> upload(byte[] data, String fileName, MediaType fileContentType, Config config) {
        return CompletableFuture.supplyAsync(() -> {
            Request request = new Request.Builder()
                    .url(Http.getUrlBuilder(config).addPathSegment("upload")
                            .addQueryParameter("filename", fileName.toString())
                            .build())
                    .post(RequestBody.create(fileContentType, data))
                    .build();
            try {
                List<String> valueList = new ArrayList<String>();
                valueList.add(Http.getClient().newCall(request).execute().body().string());
                return valueList;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}