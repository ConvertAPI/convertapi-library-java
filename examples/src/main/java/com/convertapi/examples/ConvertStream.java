package com.convertapi.examples;

import com.convertapi.client.Config;
import com.convertapi.client.ConversionResult;
import com.convertapi.client.ConvertApi;
import com.convertapi.client.Param;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.lang.System.getenv;

/**
 * Example of the file conversion when data is passed as a stream.
 */
public class ConvertStream {

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        Config.setDefaultSecret(getenv("CONVERTAPI_SECRET"));    //Get your secret at https://www.convertapi.com/a

        // Creating file data stream
        InputStream stream = Files.newInputStream(new File("src/main/resources/test.docx").toPath());

        System.out.println("Converting stream of DOCX data to PDF");
        CompletableFuture<ConversionResult> result = ConvertApi.convert("docx", "pdf",
                new Param("file", stream, "test.docx")
        );

        Path pdfFile = Paths.get(System.getProperty("java.io.tmpdir") + "/myfile.pdf");
        result.get().saveFile(pdfFile).get();

        System.out.println("PDF file saved to: " + pdfFile.toString());
    }
}
