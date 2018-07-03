package com.baltsoft;

import com.baltsoft.Model.ConversionResponse;
import com.baltsoft.Model.ConversionResponseFile;
import okhttp3.*;
import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Param {
    private String name;
    private CompletableFuture<List<String>> values;
//    private InputStream streamValue;
    private MediaType fileContentType;
    private String fileExtension;
    private Path file;
    private Config config = Config.defaults();

    public Param(String name, String[] values) {
        this.name = name.toLowerCase();
        this.values = CompletableFuture.completedFuture(values);
    }

    public Param(String name, String value) {
        this(name, new String[]{value});
    }

    public Param(String name, int value) {
        this(name, String.valueOf(value));
    }

    public Param(String name, BigDecimal value) {
        this(name, String.valueOf(value));
    }

//    public Param(String name, InputStream value, String fileFormat) {
//    }

    public Param(String name, Path value) throws FileNotFoundException {
        this(name, value, Config.defaults());
    }

    public Param(String name, Path value, Config config) throws FileNotFoundException {
        this(name, new String[]{});
        file = value;
        String contentTypeString = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(file.toFile());
        fileContentType = MediaType.parse(contentTypeString);
        fileExtension = getFileExtension(file);
        values = upload(file, fileContentType, config);
    }

    public Param(String name, ConversionResult value) throws ExecutionException, InterruptedException {
        this(name, new String[]{});

        value.getResponseFuture().thenApplyAsync(r -> {
            String[] urls = Arrays.stream(r.Files).map(Param::apply).;
        });

        ConversionResponse conversionResponse = value.get();
        String[] urls = new String[conversionResponse.Files.length];
        for (int i = 0; i < conversionResponse.Files.length; i++) {
            urls[i] = conversionResponse.Files[i].Url;
        }
        this.values = CompletableFuture.completedFuture(urls);
    }

    private static CompletableFuture<String[]> upload(Path file, MediaType fileContentType, Config config) {
        return CompletableFuture.supplyAsync(() -> {
            Request request = new Request.Builder()
                    .url(Http.getUrlBuilder(config).addPathSegment("upload")
                            .addQueryParameter("filename", file.getFileName().toString())
                            .build())
                    .post(RequestBody.create(fileContentType, file.toFile()))
                    .build();

            String bodyString = null;
            try {
                bodyString = Http.getClient().newCall(request).execute().body().string();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return new String[] {bodyString};
        });
    }

    private static String apply(ConversionResponseFile f) {
        return f.Url;
    }

    public String getName() {
        return name;
    }

    public Param setConfig(Config config) {
        this.config = config;
        return this;
    }

    private String getFileExtension(Path file) {
        String name = file.getFileName().toString();
        try {
            return name.substring(name.lastIndexOf(".") + 2);
        } catch (Exception e) {
            return "";
        }
    }

    public MediaType getFileContentType() {
        return fileContentType;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public String[] getValues() throws ExecutionException, InterruptedException {
        return values.get();
    }
}
