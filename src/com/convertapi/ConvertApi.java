package com.convertapi;

import com.convertapi.model.ConversionResponse;
import com.convertapi.model.User;
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
    private static final List<String> IGNORE_PARAMS = Arrays.asList( "storefile", "async", "jobid", "timeout");

    @SuppressWarnings("unused")
    public static CompletableFuture<ConversionResult> convert(String fromFormat, String toFormat, Param... params) {
        return convert(fromFormat, toFormat, params, Config.defaults());
    }

    public static CompletableFuture<ConversionResult> convert(String fromFormat, String toFormat, Param[] params, Config config) {
        CompletableFuture<ConversionResponse> completableResponse = CompletableFuture.supplyAsync(() -> {
            @SuppressWarnings("SpellCheckingInspection")
            HttpUrl url = Http.getUrlBuilder(config)
                    .addPathSegment("convert")
                    .addPathSegment(fromFormat)
                    .addPathSegment("to")
                    .addPathSegment(toFormat)
                    .addQueryParameter("storefile", "true")
                    .build();

            MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
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
            try {
                Response response = Http.getClient(config).newCall(request).execute();
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
        try {
            Response response = Http.getClient().newCall(request).execute();
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
    public static CompletableFuture<ConversionResult> convert(Path fromFile, String toFormat) throws IOException {
        return convert(fromFile, toFormat, Config.defaults().getSecret());
    }

    public static CompletableFuture<ConversionResult> convert(Path fromFile, String toFormat, String secret) throws IOException {
        return convert(getFileExtension(fromFile), toFormat, new Param[]{new Param("file", fromFile)}, Config.defaults(secret));
    }

    @SuppressWarnings("unused")
    public static void convert(String fromPathToFile, String toPathToFile) {
        convert(fromPathToFile, toPathToFile, Config.defaults().getSecret());
    }

    public static void convert(String fromPathToFile, String toPathToFile, String secret) {
        try {
            Path fromPath = Paths.get(fromPathToFile);
            Path toPath = Paths.get(toPathToFile);
            convert(fromPath, getFileExtension(toPath), secret).get().saveFile(toPath).get();
        } catch (IOException | ExecutionException | InterruptedException e) {
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