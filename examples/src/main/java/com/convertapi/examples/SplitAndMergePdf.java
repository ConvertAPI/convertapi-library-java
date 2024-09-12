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
 * Example of extracting the first and the last page from PDF and then chaining merge conversion.
 * https://www.convertapi.com/pdf-to-extract
 * https://www.convertapi.com/pdf-to-merge
 */
public class SplitAndMergePdf {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Config.setDefaultApiCredentials(getenv("CONVERTAPI_CREDENTIALS"));   //Get your api credentials at https://www.convertapi.com/a
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));

        System.out.println("Creating PDF with the first and the last pages");

        CompletableFuture<ConversionResult> splitResult = ConvertApi.convert("pdf", "split",
            new Param("file", Paths.get("files/test.pdf"))
        );

        CompletableFuture<ConversionResult> mergeResult = ConvertApi.convert("pdf", "merge",
            new Param("files", splitResult, 0),
            new Param("files", splitResult, -1)
        );

        System.out.println("PDF file saved to: " + mergeResult.get().saveFile(tempDir).get());
    }
}
