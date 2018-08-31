package com.convertapi.examples;

import com.convertapi.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.lang.System.getenv;

/**
 * Example of exception handling.
 * https://www.convertapi.com/pdf-to-pptx
 */

public class CreateExceptionHandling {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Config.setDefaultSecret(getenv("CONVERTAPI_SECRET"));    //Get your secret at https://www.convertapi.com/a
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));

        System.out.println("Creating an exception");
        CompletableFuture<ConversionResult> resultFuture = ConvertApi.convert("pdf", "pptx",
                new Param("file", Paths.get("test-files/test.pdf")),
                new Param("AutoRotate", "WrongParameter")
        ).exceptionally(t -> {
            if (t.getCause() instanceof ConversionException) {
                System.out.println("Status Code: " + ((ConversionException) t.getCause()).getHttpStatusCode());
            }
            System.out.println("Error message: " + t.getMessage());
            return null;
        });

        ConversionResult result = resultFuture.get();
        if (result != null) {
            result.saveFile(tempDir).get();
        }

        System.out.println("Finishing without exceptions");
    }
}