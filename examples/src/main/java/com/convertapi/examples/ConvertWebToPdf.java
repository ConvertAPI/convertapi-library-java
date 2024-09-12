package com.convertapi.examples;

import com.convertapi.client.Config;
import com.convertapi.client.ConversionResult;
import com.convertapi.client.ConvertApi;
import com.convertapi.client.Param;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.lang.System.getenv;

/**
 * Example of converting Web Page URL to PDF file
 * https://www.convertapi.com/web-to-pdf
 */
public class ConvertWebToPdf {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Config.setDefaultApiCredentials(getenv("CONVERTAPI_CREDENTIALS"));   //Get your api credentials at https://www.convertapi.com/a

        System.out.println("Converting WEB to PDF");
        CompletableFuture<ConversionResult> result = ConvertApi.convert("web", "pdf",
            new Param("url", "https://en.wikipedia.org/wiki/Data_conversion"),
            new Param("filename", "web-example")
        );

        Path tmpDir = Paths.get(System.getProperty("java.io.tmpdir"));
        CompletableFuture<Path> pdfFile = result.get().saveFile(tmpDir);

        System.out.println("PDF file saved to: " + pdfFile.get().toString());
    }
}
