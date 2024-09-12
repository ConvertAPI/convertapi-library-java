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
 * Example of conversion remote file. Converting file must be accessible from
 * the internet.
 */
public class ConvertRemoteFile {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Config.setDefaultApiCredentials(getenv("CONVERTAPI_CREDENTIALS"));   //Get your api credentials at https://www.convertapi.com/a

        System.out.println("Converting remote PPTX to PDF");
        CompletableFuture<ConversionResult> result = ConvertApi.convert("pptx", "pdf",
            new Param("file", "https://cdn.convertapi.com/cara/testfiles/presentation.pptx")
        );

        Path pdfFile = Paths.get(System.getProperty("java.io.tmpdir") + "/myfile.pdf");
        result.get().saveFile(pdfFile).get();

        System.out.println("PDF file saved to: " + pdfFile.toString());
    }
}
