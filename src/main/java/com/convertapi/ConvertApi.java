package com.convertapi;

import com.convertapi.model.ConversionResponse;
import com.convertapi.model.User;
import com.google.gson.Gson;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ConvertApi {
    private static final List<String> IGNORE_PARAMS = Arrays.asList( "storefile", "async", "jobid", "timeout");

    public static CompletableFuture<ConversionResult> convert(String fromFormat, String toFormat, Param[] params) {
        return convert(fromFormat, toFormat, params, Config.defaults());
    }

    public static CompletableFuture<ConversionResult> convert(String fromFormat, String toFormat, Param[] params, Config config) {
        CompletableFuture<ConversionResponse> completableResponse = CompletableFuture.supplyAsync(() -> {
            HttpUrl url = Http.getUrlBuilder(config)
                    .addPathSegment(fromFormat)
                    .addPathSegment("to")
                    .addPathSegment(toFormat)
                    .addQueryParameter("storefile", "true")
                    .build();

            MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
            for (Param param: params) {
                if (!IGNORE_PARAMS.contains(param.getName())) {
                    try {
                        if (param.getValue().size() == 1) {
                            multipartBuilder.addFormDataPart(param.getName(), param.getValue().get(0));
                        } else {
                            for (int i = 0; i < param.getValue().size(); i++) {
                                multipartBuilder.addFormDataPart(param.getName() + "[" + i + "]", param.getValue().get(i));
                            }
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Accept", "application/json")
                    .post(multipartBuilder.build())
                    .build();

            String bodyString;
            try {
                Response response = Http.getClient().newCall(request).execute();
                bodyString = response.body().string();
                if (response.code() != 200) {
                    throw new ConversionException(bodyString);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return new Gson().fromJson(bodyString, ConversionResponse.class);
        });

        return completableResponse.thenApply(r -> new ConversionResult(r));
    }

    public static User getUser() {
        return getUser(Config.defaults());
    }

    public static User getUser(Config config) {
        HttpUrl url = Http.getUrlBuilder(config).addPathSegment("user").build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json")
                .build();

        String bodyString;
        try {
            Response response = Http.getClient().newCall(request).execute();
            bodyString = response.body().string();
            if (response.code() != 200) {
                throw new ConversionException(bodyString);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new Gson().fromJson(bodyString, User.class);
    }
}