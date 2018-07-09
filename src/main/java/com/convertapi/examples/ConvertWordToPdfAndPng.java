package com.convertapi.examples;

import com.convertapi.Config;
import com.convertapi.ConversionResult;
import com.convertapi.ConvertApi;
import com.convertapi.Param;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Example of saving Word docx to PDF and to PNG
 * Conversion is made by using same file parameter and processing two conversions simultaneously
 * https://www.convertapi.com/docx-to-pdf
 * https://www.convertapi.com/docx-to-png
 */

public class ConvertWordToPdfAndPng {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Config.setDefaultSecret("YOUR API SECRET"); //Get your secret at https://www.convertapi.com/a

        System.out.println("Converting DOCX to PDF and JPG in parallel");

        Param docxFileParam = new Param("file", Paths.get("test-files/test.docx"));

        ConversionResult pdfResult = ConvertApi.convert("docx", "pdf", new Param[]{docxFileParam});
        ConversionResult jpgResult = ConvertApi.convert("docx", "jpg", new Param[]{docxFileParam});

        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
        Path pdfPath = pdfResult.saveFile(tempDir);
        List<Path> jpgPaths = jpgResult.saveFiles(tempDir);

        System.out.println("PDF file saved to: " + pdfPath.toString());
        for (Path path: jpgPaths) {
            System.out.println("JPG file saved to: " + path.toString());
        }
    }
}