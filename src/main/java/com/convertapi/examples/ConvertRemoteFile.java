package com.convertapi.examples;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import com.convertapi.Config;
import com.convertapi.ConversionResult;
import com.convertapi.ConvertApi;
import com.convertapi.Param;

/**
 * Example of conversion remote file. Converting file must be accessible from the internet.
 */

public class ConvertRemoteFile {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Config.setDefaultSecret("YOUR API SECRET");    //Get your secret at https://www.convertapi.com/a

        Param fileParam = new Param("file", "https://cdn.convertapi.com/cara/testfiles/presentation.pptx");

        System.out.println("Converting remote PPTX to PDF");
        CompletableFuture<ConversionResult> result = ConvertApi.convert("pptx", "pdf", new Param[]{fileParam});

        Path pdfFile = Paths.get(System.getProperty("java.io.tmpdir") + "/myfile.pdf");
        result.get().saveFile(pdfFile);

        System.out.println("PDF file saved to: " + pdfFile.toString());
    }
}