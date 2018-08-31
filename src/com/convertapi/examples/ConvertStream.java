package com.convertapi.examples;

import com.convertapi.Config;
import com.convertapi.ConversionResult;
import com.convertapi.ConvertApi;
import com.convertapi.Param;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.lang.System.getenv;

/**
 * Example of converting HTML stream to PDF file
 * https://www.convertapi.com/html-to-pdf
 */

public class ConvertStream {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // Create stream from HTML string
        String streamContent = "<!DOCTYPE html><html><body><h1>My First Heading</h1><p>My first paragraph.</p></body></html>";
        InputStream stream = new ByteArrayInputStream(streamContent.getBytes());

        Config.setDefaultSecret(getenv("CONVERTAPI_SECRET"));    //Get your secret at https://www.convertapi.com/a

        System.out.println("Converting HTML stream to PDF");
        CompletableFuture<ConversionResult> result = ConvertApi.convert("html", "pdf",
                new Param("file", stream, "test.html")
        );

        Path tmpDir = Paths.get(System.getProperty("java.io.tmpdir"));
        CompletableFuture<Path> pdfFile = result.get().saveFile(tmpDir);

        System.out.println("PDF file saved to: " + pdfFile.get().toString());
    }
}