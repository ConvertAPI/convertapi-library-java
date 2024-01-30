package com.convertapi.client;

import com.convertapi.client.model.ConversionResponse;
import com.convertapi.client.model.RemoteUploadResponse;
import com.convertapi.client.model.User;
import com.google.gson.Gson;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("WeakerAccess")
public class ConvertApi {

    @SuppressWarnings("SpellCheckingInspection")
    private static final List<String> IGNORE_PARAMS = Arrays.asList("storefile", "async", "jobid");

    @SuppressWarnings("unused")
    public static CompletableFuture<ConversionResult> convert(String fromFormat, String toFormat, Param... params) {
        return convert(fromFormat, toFormat, params, Config.defaults());
    }

    public static CompletableFuture<ConversionResult> convert(String fromFormat, String toFormat, Param[] params, Config config) {
        CompletableFuture<ConversionResponse> completableResponse = CompletableFuture.supplyAsync(() -> {
            HttpUrl.Builder urlBuilder = Http.getUrlBuilder(config)
                .addPathSegment("convert")
                .addPathSegment(fromFormat)
                .addPathSegment("to")
                .addPathSegment(toFormat);

            for (Param param : params) {
                if (param.getName().equalsIgnoreCase("converter")) {
                    try {
                        urlBuilder = urlBuilder
                            .addPathSegment("converter")
                            .addPathSegment(param.getValue().get(0));
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            HttpUrl url = urlBuilder.addQueryParameter("storefile", "true").build();

            MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            HashMap<String, List<String>> paramValues = getParamValues(params);

            for (String name : paramValues.keySet()) {
                if (!IGNORE_PARAMS.contains(name)) {
                    List<String> values = paramValues.get(name);
                    if (paramValues.get(name).size() == 1) {
                        multipartBuilder.addFormDataPart(name, values.get(0));
                    } else {
                        for (int i = 0; i < values.size(); i++) {
                            multipartBuilder.addFormDataPart(name + "[" + i + "]", values.get(i));
                        }
                    }
                }
            }

            Request request = Http.getRequestBuilder()
                .url(url)
                .addHeader("Accept", "application/json")
                .post(multipartBuilder.build())
                .build();

            String bodyString;
            try (Response response = Http.getClient(config).newCall(request).execute()) {
                //noinspection ConstantConditions
                bodyString = response.body().string();
                if (response.code() != 200) {
                    throw new ConversionException(bodyString, response.code());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return new Gson().fromJson(bodyString, ConversionResponse.class);
        });

        return completableResponse.thenApply(ConversionResult::new);
    }

    @SuppressWarnings("unused")
    public static User getUser() {
        return getUser(Config.defaults());
    }

    public static User getUser(Config config) {
        HttpUrl url = Http.getUrlBuilder(config).addPathSegment("user").build();
        Request request = Http.getRequestBuilder()
            .url(url)
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

        return new Gson().fromJson(bodyString, User.class);
    }

    @SuppressWarnings("unused")
    public static CompletableFuture<ConversionResult> convertFile(Path fromFile, String toFormat, Param... params) throws IOException {
        return convertFile(fromFile, toFormat, Config.defaults(), params);
    }

    @SuppressWarnings("unused")
    public static CompletableFuture<ConversionResult> convertFile(Path fromFile, String toFormat, String secret, Param... params) throws IOException {
        return convertFile(fromFile, toFormat, Config.defaults(secret), params);
    }

    @SuppressWarnings("unused")
    public static CompletableFuture<ConversionResult> convertFile(Path fromFile, String toFormat, String token, String apiKey, Param... params) throws IOException {
        return convertFile(fromFile, toFormat, Config.defaults(token, apiKey), params);
    }

    public static CompletableFuture<ConversionResult> convertFile(Path fromFile, String toFormat, Config config, Param... params) throws IOException {
        Param[] fileParam = new Param[]{new Param("file", fromFile)};
        return convert(getFileExtension(fromFile), toFormat, Param.concat(fileParam, params), config);
    }

    @SuppressWarnings("unused")
    public static void convertFile(String fromPathToFile, String toPathToFile) {
        convertFile(fromPathToFile, toPathToFile, Config.defaults());
    }

    @SuppressWarnings("unused")
    public static void convertFile(String fromPathToFile, String toPathToFile, String secret) {
        convertFile(fromPathToFile, toPathToFile, Config.defaults(secret));
    }

    @SuppressWarnings("unused")
    public static void convertFile(String fromPathToFile, String toPathToFile, String token, String apiKey) {
        convertFile(fromPathToFile, toPathToFile, Config.defaults(token, apiKey));
    }

    public static void convertFile(String fromPathToFile, String toPathToFile, Config config) {
        try {
            Path fromPath = Paths.get(fromPathToFile);
            Path toPath = Paths.get(toPathToFile);
            convertFile(fromPath, getFileExtension(toPath), config).thenCompose(result -> result.saveFile(toPath)).get();
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unused")
    public static List<Path> convertFileToDir(String fromPathToFile, String toFormat, String outputDirectory, Param... params) {
        return convertFileToDir(fromPathToFile, toFormat, outputDirectory, Config.defaults(), params);
    }

    @SuppressWarnings("unused")
    public static List<Path> convertFileToDir(String fromPathToFile, String toFormat, String outputDirectory, String secret, Param... params) {
        return convertFileToDir(fromPathToFile, toFormat, outputDirectory, Config.defaults(secret), params);
    }

    @SuppressWarnings("unused")
    public static List<Path> convertFileToDir(String fromPathToFile, String toFormat, String outputDirectory, String token, String apiKey, Param... params) {
        return convertFileToDir(fromPathToFile, toFormat, outputDirectory, Config.defaults(token, apiKey), params);
    }

    public static List<Path> convertFileToDir(String fromPathToFile, String toFormat, String outputDirectory, Config config, Param... params) {
        try {
            Path fromPath = Paths.get(fromPathToFile);
            Path toPath = Paths.get(outputDirectory);
            return convertFile(fromPath, toFormat, config, params).get().saveFilesSync(toPath);
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unused")
    public static Path convertUrl(String url, String toPathToFile, Param... params) {
        return convertUrl(url, toPathToFile, Config.defaults(), params);
    }

    @SuppressWarnings("unused")
    public static Path convertUrl(String url, String toPathToFile, String secret, Param... params) {
        return convertUrl(url, toPathToFile, Config.defaults(secret), params);
    }

    @SuppressWarnings("unused")
    public static Path convertUrl(String url, String toPathToFile, String token, String apiKey, Param... params) {
        return convertUrl(url, toPathToFile, Config.defaults(token, apiKey), params);
    }

    public static Path convertUrl(String url, String toPathToFile, Config config, Param... params) {
        try {
            Path toPath = Paths.get(toPathToFile);
            Param[] urlParam = new Param[]{new Param("url", url)};
            return convert("web", getFileExtension(toPath), Param.concat(urlParam, params), config).get().saveFile(toPath).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unused")
    public static Path convertRemoteFile(String url, String toPathToFile, Param... params) {
        return convertRemoteFile(url, toPathToFile, Config.defaults(), params);
    }

    @SuppressWarnings("unused")
    public static Path convertRemoteFile(String url, String toPathToFile, String secret, Param... params) {
        return convertRemoteFile(url, toPathToFile, Config.defaults(secret), params);
    }

    @SuppressWarnings("unused")
    public static Path convertRemoteFile(String url, String toPathToFile, String token, String apiKey, Param... params) {
        return convertRemoteFile(url, toPathToFile, Config.defaults(token, apiKey), params);
    }

    public static Path convertRemoteFile(String url, String toPathToFile, Config config, Param... params) {
        RemoteUploadResponse response = Http.remoteUpload(url, config);
        try {
            Path toPath = Paths.get(toPathToFile);
            Param[] fileParam = new Param[]{new Param("file", response.FileId)};
            return convert(response.FileExt, getFileExtension(toPath), Param.concat(fileParam, params), config).get().saveFile(toPath).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unused")
    public static List<Path> convertRemoteFileToDir(String url, String toFormat, String outputDirectory, Param... params) {
        return convertRemoteFileToDir(url, toFormat, outputDirectory, Config.defaults(), params);
    }

    @SuppressWarnings("unused")
    public static List<Path> convertRemoteFileToDir(String url, String toFormat, String outputDirectory, String secret, Param... params) {
        return convertRemoteFileToDir(url, toFormat, outputDirectory, Config.defaults(secret), params);
    }

    @SuppressWarnings("unused")
    public static List<Path> convertRemoteFileToDir(String url, String toFormat, String outputDirectory, String token, String apiKey, Param... params) {
        return convertRemoteFileToDir(url, toFormat, outputDirectory, Config.defaults(token, apiKey), params);
    }

    public static List<Path> convertRemoteFileToDir(String url, String toFormat, String outputDirectory, Config config, Param... params) {
        RemoteUploadResponse response = Http.remoteUpload(url, config);
        try {
            Path toPath = Paths.get(outputDirectory);
            Param[] fileParam = new Param[]{new Param("file", response.FileId)};
            return convert(response.FileExt, toFormat, Param.concat(fileParam, params), config).get().saveFilesSync(toPath);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFileExtension(Path path) {
        String name = path.getFileName().toString();
        return name.substring(name.lastIndexOf(".") + 1);
    }

    private static HashMap<String, List<String>> getParamValues(Param[] params) {
        HashMap<String, List<String>> result = new HashMap<>();

        try {
            for (Param param : params) {
                List<String> values = result.computeIfAbsent(param.getName(), (v) -> new ArrayList<>());
                values.addAll(param.getValue());
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
