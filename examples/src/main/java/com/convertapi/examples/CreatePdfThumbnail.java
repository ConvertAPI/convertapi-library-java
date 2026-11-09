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
 * Example of converting the first page of a PDF to a JPG thumbnail.
 * https://www.convertapi.com/pdf-to-jpg
 */
public class CreatePdfThumbnail {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Config.setDefaultApiCredentials(getenv("API_TOKEN"));   // Get your api token at https://www.convertapi.com/a/authentication
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));

        System.out.println("Creating PDF thumbnail");

        CompletableFuture<ConversionResult> thumbnailResult = ConvertApi.convert("pdf", "jpg",
            new Param("file", Paths.get("files/test.pdf")),
            new Param("pagerange", "1"),
            new Param("imageheight", 300)
        );

        System.out.println("JPG thumbnail file saved to: " + thumbnailResult.get().saveFile(tempDir).get());
    }
}
