package com.convertapi.examples;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.convertapi.Config;
import com.convertapi.ConversionResult;
import com.convertapi.ConvertApi;
import com.convertapi.Param;

/**
 * Example of HTTP client setup to use HTTP proxy server.
 */

public class Advanced {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Config.setDefaultSecret("YOUR API SECRET");    //Get your secret at https://www.convertapi.com/a

        // Advanced HTTP client setup
        Config.setDefaultHttpBuilder(builder -> {
            return builder
                    .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.1", 8888))) // Setting Proxy server
                    .connectTimeout(3, TimeUnit.SECONDS);    // Setting connect timeout
                    // More settings can be tuned here
        });

        // Conversion
        Param fileParam = new Param("file", "https://cdn.convertapi.com/cara/testfiles/presentation.pptx");
        System.out.println("Converting remote PPTX to PDF");
        CompletableFuture<ConversionResult> result = ConvertApi.convert("pptx", "pdf", new Param[]{fileParam});
        Path pdfFile = Paths.get(System.getProperty("java.io.tmpdir") + "/myfile.pdf");
        result.get().saveFile(pdfFile).get();

        // Leaving no files on convertapi.com server
        System.out.println("Deleting source file from convertapi.com server");
        fileParam.delete().get();
        System.out.println("Deleting result files from convertapi.com server");
        result.get().deleteSync();

        System.out.println("PDF file saved to: " + pdfFile.toString());
    }
}