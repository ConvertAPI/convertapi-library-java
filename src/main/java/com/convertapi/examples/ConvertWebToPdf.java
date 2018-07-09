package com.convertapi.examples;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import com.convertapi.Config;
import com.convertapi.ConversionResult;
import com.convertapi.ConvertApi;
import com.convertapi.Param;

/**
 * Example of converting Web Page URL to PDF file
 * https://www.convertapi.com/web-to-pdf
*/

public class ConvertWebToPdf {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Config.setDefaultSecret("YOUR API SECRET");    //Get your secret at https://www.convertapi.com/a

        System.out.println("Converting WEB to PDF");
        ConversionResult result = ConvertApi.convert("web", "pdf", new Param[]{
                new Param("url", "https://en.wikipedia.org/wiki/Data_conversion"),
                new Param("filename", "web-example")
        });

        Path tmpDir = Paths.get(System.getProperty("java.io.tmpdir"));
        Path pdfFile = result.saveFile(tmpDir);

        System.out.println("PDF file saved to: " + pdfFile.toString());
    }
}