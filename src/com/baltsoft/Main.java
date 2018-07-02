package com.baltsoft;

import com.baltsoft.Model.ConversionResponse;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException, ExecutionException, InterruptedException {
        Config.setDefaultSecret("");
        CompletableFuture<ConversionResponse> rez = ConvertApi.convert("docx", "pdf", new Param[]{new Param("File", new File("/home/jon/trinti/test.docx"))});

        String s = ""; //f.get();
        String a = s;
        Thread.sleep(100000);


    }

}
