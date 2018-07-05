package com.baltsoft;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException, ExecutionException, InterruptedException {
        Config.setDefaultSecret("1234567890123456");
        System.out.println("1");
        ConversionResult rez = ConvertApi.convert("docx", "jpg", new Param[]{new Param("File", Paths.get("/home/jon/trinti/test.docx"))});
        System.out.println("2");
        ConversionResult rez1 = ConvertApi.convert("jpg", "pdf", new Param[]{new Param("File", rez)});
        System.out.println("3");
        rez1.saveFiles(Paths.get("/tmp"));
    }

}
