package com.baltsoft;

import com.baltsoft.Model.ConversionResponse;
import com.google.gson.Gson;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.internal.http.HttpHeaders;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ConvertApi {
    private static final List<String> IGNORE_PARAMS = Arrays.asList( "storefile", "async", "jobid", "timeout");

    public static ConversionResult convert(String fromFormat, String toFormat, Param[] params) {
        return convert(fromFormat, toFormat, params, Config.defaults());
    }

    public static ConversionResult convert(String fromFormat, String toFormat, Param[] params, Config config) {
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
                        String[] values = param.getValues();
                        if (values.length == 1) {
                            multipartBuilder.addFormDataPart(param.getName(), values[0]);
                        } else {
                            for (int i = 0; i < values.length; i++) {
                                multipartBuilder.addFormDataPart(param.getName() + "[" + i + "]", values[i]);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Accept", "application/json")
                    .post(multipartBuilder.build())
                    .build();

            String bodyString = null;
            try {
                bodyString = Http.getClient().newCall(request).execute().body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return new Gson().fromJson(bodyString, ConversionResponse.class);
        });

        return new ConversionResult(completableResponse);
    }
}