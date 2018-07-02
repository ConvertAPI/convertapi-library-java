package com.baltsoft;

import com.baltsoft.Model.ConversionResponse;
import com.baltsoft.Model.ConversionResponseFile;
import okhttp3.*;
import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Param {
    private String name;
    private CompletableFuture<String[]> values;
//    private InputStream streamValue;
    private MediaType fileContentType;
    private String fileExtension;
    private File file;
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

    public Param(String name, File value) throws FileNotFoundException {
        this(name, value, Config.defaults());
    }

    public Param(String name, File value, Config config) throws FileNotFoundException {
        this(name, new String[]{});
        file = value;
        String contentTypeString = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(file);
        fileContentType = MediaType.parse(contentTypeString);
        fileExtension = getFileExtension(file);
        values = upload(file, fileContentType, config);
    }

    public Param(String name, CompletableFuture<ConversionResponse> value) throws ExecutionException, InterruptedException {
        this(name, new String[]{});
        ConversionResponse conversionResponse = value.get();
        String[] urls = new String[conversionResponse.Files.length];
        for (int i = 0; i < conversionResponse.Files.length; i++) {
            urls[i] = conversionResponse.Files[i].Url;
        }
        this.values = CompletableFuture.completedFuture(urls);
    }

    private static CompletableFuture<String[]> upload(File file, MediaType fileContentType, Config config) {
        return CompletableFuture.supplyAsync(() -> {
            Request request = new Request.Builder()
                    .url(Http.getUrlBuilder(config).addPathSegment("upload")
                            .addQueryParameter("filename", file.getName())
                            .build())
                    .post(RequestBody.create(fileContentType, file))
                    .build();

            String bodyString = null;
            try {
                bodyString = Http.getClient().newCall(request).execute().body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new String[] {bodyString};
        });
    }

    public String getName() {
        return name;
    }

    public Param setConfig(Config config) {
        this.config = config;
        return this;
    }

    private String getFileExtension(File file) {
        String name = file.getName();
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
